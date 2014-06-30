package com.localhost.sql;

import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.File;
import java.sql.*;

public class Response {
	private OutputStream output;
	private Request request;
	private String sendStr;
	private SQLAccess sqlAcc;

	public Response() {
		this.output = null;
		this.request = new Request();
	}

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
			doGET();
		} else if ("POST".equalsIgnoreCase(this.request.getMethod())) {
			doPOST();
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
	private void doGET() {
		try {
			String ecd = this.request.getEncoding();
			File file = new File("text.html");

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
	private void doPOST() {
		try {
			String display = "";
			if (this.request.getSubmit()) {
				switch(verification()) {
				case 0:
					display = "用户不存在，请先注册！";
					break;
				case 1:
					display = "登录成功！";
					break;
				case 2:
					display = "密码错误，请输入正确密码！";
				}
			} else if (this.request.getRegister()) {
				if (verification() == 0){
					this.sqlAcc.updata(this.request.getBody()[0], this.request.getBody()[1]);
					display = "注册成功，请登录";
				} else {
					display = "用户已存在，请重新输入用户名！";
				}
			}
			outputHtml(display);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	private int verification() {
		int flag = 0;
		try {
			this.sqlAcc.query();
			while (this.sqlAcc.result.next()) {
				if (sqlAcc.result.getString(1)
						.equals(this.request.getBody()[0])) {
					if (sqlAcc.result.getString(2).equals(
							this.request.getBody()[1])) {
						flag = 1;
						break;
					} else {
						flag = 2;
						break;
					}
				}
			}
			this.sqlAcc.connClose();
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
		return flag;
	}

	private void outputHtml(String str) {
		String ecd = this.request.getEncoding();

		this.sendStr = "HTTP/1.1 200 OK\r\n"
				+ "Content-Type: text/html;charset=" + ecd + "\r\n" + "\r\n"
				+ "<html><title>信息反馈</title><body><h3>" + str
				+ "</h3></body></html>";
	}
}
