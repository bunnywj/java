package com.localhost.sql;

import java.sql.SQLException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class Main implements Servlet {
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
			switch (HttpServer.sql.checkout(userName, password)) {
			case 0:
				strHtml = "<h3>�û������ڣ�����ע�ᣡ</h3><br /><br />"
						+ "<p><a href=\"http://localhost:8080/login.html\">��¼����</a></p>";
				break;
			case 1:
				strHtml = "<h3>�����û����ǣ�"
						+ userName
						+ "</h3><br /><br />"
						+ "<p><a href=\"http://localhost:8080/modify.html\">�޸�����</a></p>";
				response.setHeader("Set-Cookie", request.getCookies());
				break;
			case 2:
				strHtml = "<h3>���������������ȷ���룡</h3><br /><br />"
						+ "<p><a href=\"http://localhost:8080/login.html\">��¼����</a></p>";
				break;
			}

			response.setStatus(200, "OK");
			response.setEncoding("text/html", request.getEncoding());
			response.setHeader("Content-Type", response.getEncoding());
			response.setResponse("<html><title>��¼��Ϣ</title><body>" + strHtml
					+ "</body></html>");
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		} catch (UnsupportedEncodingException ex) {
			System.out.println(ex.getMessage());
		}
	}

}
