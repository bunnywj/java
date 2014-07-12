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
			switch (HttpServer.sql.checkout(userName, password)) {
			case 0:
				strHtml = "<h3>用户不存在，请先注册！</h3><br /><br />"
						+ "<p><a href=\"http://localhost:8080/login.html\">登录界面</a></p>";
				break;
			case 1:
				strHtml = "<h3>您的用户名是："
						+ userName
						+ "</h3><br /><br />"
						+ "<p><a href=\"http://localhost:8080/modify.html\">修改密码</a></p>";
				response.setHeader("Set-Cookie", request.getCookies());
				break;
			case 2:
				strHtml = "<h3>密码错误，请输入正确密码！</h3><br /><br />"
						+ "<p><a href=\"http://localhost:8080/login.html\">登录界面</a></p>";
				break;
			}

			response.setStatus(200, "OK");
			response.setEncoding("text/html", request.getEncoding());
			response.setHeader("Content-Type", response.getEncoding());
			response.setResponse("<html><title>登录信息</title><body>" + strHtml
					+ "</body></html>");
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		} catch (UnsupportedEncodingException ex) {
			System.out.println(ex.getMessage());
		}
	}

}
