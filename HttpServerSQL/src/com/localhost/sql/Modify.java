package com.localhost.sql;

import java.sql.SQLException;
import java.util.Enumeration;

public class Modify implements Servlet {
	private SQL sql;

	Modify(SQL sql) {
		this.sql = sql;
	}

	@Override
	public void service(HttpRequest request, HttpResponse response) {
		try {
			String userName = request.getCookies().split("=")[1].trim();

			String oldPassword = null;
			String newPassword = null;
			Enumeration<String> parameter = request.getParameterNames();
			while (parameter.hasMoreElements()) {
				String parameterName = parameter.nextElement();
				if ("oldPassword".equalsIgnoreCase(parameterName)) {
					oldPassword = request.getParameterValue(parameterName);
				}
				if ("newPassword".equalsIgnoreCase(parameterName)) {
					newPassword = request.getParameterValue(parameterName);
				}
			}

			String strHtml = "";
			if (this.sql.checkout(userName, oldPassword) == 2) {
				strHtml = "<h3>������ľ����벻��ȷ��</h3>";
			} else {
				this.sql.updata(userName, newPassword);
				strHtml = "<h3>�����û����ǣ�"
						+ userName
						+ "</h3><br /><br />"
						+ "<p><a href=\"http://localhost:8080/modify.html\">�޸�����</a></p>";
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
