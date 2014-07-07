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
			// 从request中获取参数名称及参数值
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
			// 服务器响应浏览器的字符串信息
			String strHtml = "";
			if (submit) {
				switch (this.sql.checkout(userName, password)) {
				case 0:
					strHtml = "<h3>用户不存在，请先注册！</h3>";
					break;
				case 1:
					strHtml = "<h3>您的用户名是："
							+ userName
							+ "</h3><br /><br />"
							+ "<p><a href=\"http://localhost:8080/modify.html\">修改密码</a></p>";
					break;
				case 2:
					strHtml = "<h3>密码错误，请输入正确密码！</h3>";
				}
			}
			if (register) {
				if (this.sql.checkout(userName, password) == 0) {
					this.sql.insert(userName, password);
					strHtml = "<h3>注册成功，请登录</h3>";
				} else {
					strHtml = "<h3>用户已存在，请重新输入用户名！</h3>";
				}
			}
			
			response.setStatus(200, "OK");
			response.setEncoding("text/html", request.getEncoding());
			response.setCookie(userName);
			response.setResponse("<html><title>登录信息</title><body><h3>"
					+ strHtml + "</h3></body></html>");

		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
	}

}
