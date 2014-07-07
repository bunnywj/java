package com.localhost.sql;

import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.File;

public class Response {
	private OutputStream output;
	private Request request;
	private String sendStr;
	private SQLAccess sqlAcc;

	public Response(OutputStream output) {
		this.output = output;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

	public void setSQLAccess(SQLAccess sqlAcc) {
		this.sqlAcc = sqlAcc;
	}

	public void send() {
		if ("GET".equalsIgnoreCase(this.request.getMethod())) {
			doGet();
		} else if ("POST".equalsIgnoreCase(this.request.getMethod())) {
			if ("main.html".equalsIgnoreCase(this.request.getUri())) {
				doPostIndex();	
			} else if ("modify.html".equalsIgnoreCase(this.request.getUri())){
				doPostModify();
			}
			
		}

		System.out
				.println("--------------------------服务器发送响应----------------------------");
		String ecd = this.request.getEncoding();
		try {
			this.output.write(this.sendStr.getBytes(ecd));
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}

		System.out.println("服务器响应的编码方式为：" + ecd);
		System.out.println();
		System.out.println("内容：" + this.sendStr);
	}

	// 如果浏览器发来的请求方式为GET，则读文件，显示首页内容
	private void doGet() {
		try {
			String ecd = this.request.getEncoding();
			File file = new File(this.request.getUri());

			if (file.exists()) {
				FileInputStream inFile = new FileInputStream(file);
				byte[] buf = new byte[inFile.available()];
				inFile.read(buf);
				inFile.close();
				this.sendStr = "HTTP/1.1 200 OK\r\n"
						+ "Content-Type: text/html;charset=" + ecd + "\r\n"
						+ "\r\n" + new String(buf, ecd);
			} else {
				this.sendStr = "HTTP/1.1 404 File NOT Fount\r\n"
						+ "Content-Type: text/html;charset=" + ecd + "\r\n"
						+ "\r\n" + "<h1>File Not Found</h1>";
			}
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
	}

	// 如果浏览器发来的请求方式为POST，则输出body中的内容
	private void doPostIndex() {
		String userName = this.request.getBody()[0];
		String password = this.request.getBody()[1];
		try {
			String strHtml = "";
			if (this.request.getSubmit()) {
				switch (this.sqlAcc.authentication(userName, password)) {
				case 0:
					strHtml = "<h3>用户不存在，请先注册！</h3>";
					break;
				case 1:
					strHtml = "<h3>您的用户名是：" + userName + "</h3><br /><br />"
							+ "<p><a href=\"http://localhost:8080/modify.html\">修改密码</a></p>";
					break;
				case 2:
					strHtml = "<h3>密码错误，请输入正确密码！</h3>";
				}
			} else if (this.request.getRegister()) {
				if (this.sqlAcc.authentication(userName, password) == 0) {
					this.sqlAcc.insert(userName, password);
					strHtml = "<h3>注册成功，请登录</h3>";
				} else {
					strHtml = "<h3>用户已存在，请重新输入用户名！</h3>";
				}
			}
			displayHtml(userName, strHtml);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	private void doPostModify() {
		String userName = this.request.getCookie().split("=")[1].trim();
		String oldPassword = this.request.getBody()[0];
		String newPassword = this.request.getBody()[1];
		String strHtml = "";
		if (this.sqlAcc.authentication(userName, oldPassword) == 2) {
			strHtml = "<h3>您输入的旧密码不正确！</h3>";
		} else {
			this.sqlAcc.update(userName, newPassword);
			strHtml = "<h3>您的用户名是："
					+ userName
					+ "</h3><br /><br />"
					+ "<p><a href=\"http://localhost:8080/modify.html\">修改密码</a></p>";
		}
		displayHtml(userName, strHtml);
	}
	
	private void displayHtml(String userName, String strHtml) {
		String ecd = this.request.getEncoding();
		this.sendStr = "HTTP/1.1 200 OK\r\n"
				+ "Content-Type: text/html;charset=" + ecd + "\r\n"
				+ "Set-Cookie: name=" + userName + "\r\n"
				+ "\r\n"
				+ "<html><title>登录信息</title><body>" + strHtml + "</body></html>";
	}
}
