package com.localhost.sql;

import java.sql.SQLException;
import java.util.Enumeration;

public class Main implements Servlet {
	private SQL sql;

	Main(SQL sql) {
		this.sql = sql;
	}

	@Override
	public void service(HttpRequest request, HttpResponse response) {
		try {
			String userName = null;
			String password = null;
			boolean submit = false;
			boolean register = false;
			// ��request�л�ȡ�������Ƽ�����ֵ
			Enumeration<String> parameter = request.getParameterNames();
			while (parameter.hasMoreElements()) {
				String parameterName = parameter.nextElement();
				if ("username".equalsIgnoreCase(parameterName)) {
					userName = request.getParameterValue(parameterName);
				}
				if ("password".equalsIgnoreCase(parameterName)) {
					password = request.getParameterValue(parameterName);
				}
				if ("submit".equalsIgnoreCase(parameterName)) {
					submit = true;
				}
				if ("register".equalsIgnoreCase(parameterName)) {
					register = true;
				}
			}
			// ��������Ӧ��������ַ�����Ϣ
			String strHtml = "";
			if (submit) {
				switch (this.sql.checkout(userName, password)) {
				case 0:
					strHtml = "<h3>�û������ڣ�����ע�ᣡ</h3>";
					break;
				case 1:
					strHtml = "<h3>�����û����ǣ�"
							+ userName
							+ "</h3><br /><br />"
							+ "<p><a href=\"http://localhost:8080/modify.html\">�޸�����</a></p>";
					break;
				case 2:
					strHtml = "<h3>���������������ȷ���룡</h3>";
				}
			}
			if (register) {
				if (this.sql.checkout(userName, password) == 0) {
					this.sql.insert(userName, password);
					strHtml = "<h3>ע��ɹ������¼</h3>";
				} else {
					strHtml = "<h3>�û��Ѵ��ڣ������������û�����</h3>";
				}
			}
			
			response.setStatus(200, "OK");
			response.setEncoding("text/html", request.getEncoding());
			response.setCookie(userName);
			response.setResponse("<html><title>��¼��Ϣ</title><body><h3>"
					+ strHtml + "</h3></body></html>");

		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
	}

}
