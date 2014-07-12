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
			// 从request中获取参数名称及参数值
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
			// 服务器响应浏览器的字符串信息
			String strHtml = "";
			if (HttpServer.sql.checkout(userName, password) == 0) {
				HttpServer.sql.insert(userName, password);
				strHtml = "<h3>注册成功，请登录</h3><br /><br />"
						+ "<p><a href=\"http://localhost:8080/login.html\">登录界面</a></p>";
			} else {
				strHtml = "<h3>用户已存在，请重新输入用户名！</h3><br /><br />"
						+ "<p><a href=\"http://localhost:8080/login.html\">登录界面</a></p>";
			}

			response.setStatus(200, "OK");
			response.setEncoding("text/html", request.getEncoding());
			response.setHeader("Content-Type", response.getEncoding());
			response.setHeader("Set-Cookie", request.getCookies());
			response.setResponse("<html><title>登录信息</title><body>" + strHtml
					+ "</body></html>");
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		} catch (UnsupportedEncodingException ex) {
			System.out.println(ex.getMessage());
		}

	}

}
