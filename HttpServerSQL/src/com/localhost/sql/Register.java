package com.localhost.sql;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.SQLException;

public class Register implements Servlet {
	@Override
	public void service(HttpRequest request, HttpResponse response) {
		try {
			String userName = null;
			String password = null;
			// ��request�л�ȡ�������Ƽ�����ֵ
			String decodeCookie = URLDecoder.decode(request.getCookies(),
					request.getEncoding());
			String[] cookieInfo = decodeCookie.split("&");
			for (int i = 0; i < cookieInfo.length; ++i) {
				String name = cookieInfo[i].split("=")[0].trim();
				String value = cookieInfo[i].split("=")[1].trim();
				if ("username".equalsIgnoreCase(name)) {
					userName = value;
				}
				if ("password".equalsIgnoreCase(name)) {
					password = value;
				}
			}
			// ��������Ӧ��������ַ�����Ϣ
			String strHtml = "";
			if (HttpServer.sql.checkout(userName, password) == 0) {
				HttpServer.sql.insert(userName, password);
				strHtml = "<h3>ע��ɹ������¼</h3><br /><br />"
						+ "<p><a href=\"http://localhost:8080/login.html\">��¼����</a></p>";
			} else {
				strHtml = "<h3>�û��Ѵ��ڣ������������û�����</h3><br /><br />"
						+ "<p><a href=\"http://localhost:8080/login.html\">��¼����</a></p>";
			}

			response.setStatus(200, "OK");
			response.setEncoding("text/html", request.getEncoding());
			response.setHeader("Content-Type", response.getEncoding());
			response.setHeader("Set-Cookie", request.getCookies());
			response.setResponse("<html><title>��¼��Ϣ</title><body>" + strHtml
					+ "</body></html>");
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		} catch (UnsupportedEncodingException ex) {
			System.out.println(ex.getMessage());
		}

	}

}
