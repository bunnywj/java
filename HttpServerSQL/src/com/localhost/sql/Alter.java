package com.localhost.sql;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.SQLException;

public class Alter implements Servlet {
	private SQL sql;

	Alter(SQL sql) {
		this.sql = sql;
	}

	@Override
	public void service(HttpRequest request, HttpResponse response) {
		try {
			String userName = null;
			String oldPassword = null;
			String newPassword = null;

			String decodeCookie = URLDecoder.decode(request.getCookies(),
					request.getEncoding());
			String[] cookieInfo = decodeCookie.split("&");
			for (int i = 0; i < cookieInfo.length; ++i) {
				String name = cookieInfo[i].split("=")[0].trim();
				String value = cookieInfo[i].split("=")[1].trim();
				if ("username".equalsIgnoreCase(name)) {
					userName = value;
				}
				if ("oldPassword".equalsIgnoreCase(name)) {
					oldPassword = value;
				}
				if ("newPassword".equalsIgnoreCase(name)) {
					newPassword = value;
				}
			}

			String strHtml = "";
			if (this.sql.checkout(userName, oldPassword) == 2) {
				strHtml = "<html><title>登录信息</title><body><h3>"
						+ "<h3>您输入的旧密码不正确！请重新输入</h3><br /><br />"
						+ "<p><a href=\"http://localhost:8080/modify.html\">修改密码</a></p></body></html>";
			} else {
				this.sql.updata(userName, newPassword);
				strHtml = "<html><head><meta http-equiv=\"refresh\" content=\"0;"
						+ "url=http://localhost:8080/main.html\"></head><title>登录信息</title><body></body></html>";
				response.setCookie("password", newPassword);
			}
			// 设置HTTP协议响应头
			response.setStatus(200, "OK");
			response.setEncoding("text/html", request.getEncoding());
			response.setHeader("Content-Type", response.getEncoding());
			response.setCookie("username", userName);
			response.setHeader("Set-Cookie", response.getCookies());
			response.setResponse(strHtml);

		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		} catch (UnsupportedEncodingException ex) {
		}
	}

}
