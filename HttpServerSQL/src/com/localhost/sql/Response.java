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
				.println("--------------------------������������Ӧ----------------------------");
		String ecd = this.request.getEncoding();
		try {
			this.output.write(this.sendStr.getBytes(ecd));
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}

		System.out.println("��������Ӧ�ı��뷽ʽΪ��" + ecd);
		System.out.println();
		System.out.println("���ݣ�" + this.sendStr);
	}

	// ������������������ʽΪGET������ļ�����ʾ��ҳ���ݮ�
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

	// ������������������ʽΪPOST�������body�е����ݮ�
	private void doPostIndex() {
		String userName = this.request.getBody()[0];
		String password = this.request.getBody()[1];
		try {
			String strHtml = "";
			if (this.request.getSubmit()) {
				switch (this.sqlAcc.authentication(userName, password)) {
				case 0:
					strHtml = "<h3>�û������ڣ�����ע�ᣡ</h3>";
					break;
				case 1:
					strHtml = "<h3>�����û����ǣ�" + userName + "</h3><br /><br />"
							+ "<p><a href=\"http://localhost:8080/modify.html\">�޸�����</a></p>";
					break;
				case 2:
					strHtml = "<h3>���������������ȷ���룡</h3>";
				}
			} else if (this.request.getRegister()) {
				if (this.sqlAcc.authentication(userName, password) == 0) {
					this.sqlAcc.insert(userName, password);
					strHtml = "<h3>ע��ɹ������¼</h3>";
				} else {
					strHtml = "<h3>�û��Ѵ��ڣ������������û�����</h3>";
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
			strHtml = "<h3>������ľ����벻��ȷ��</h3>";
		} else {
			this.sqlAcc.update(userName, newPassword);
			strHtml = "<h3>�����û����ǣ�"
					+ userName
					+ "</h3><br /><br />"
					+ "<p><a href=\"http://localhost:8080/modify.html\">�޸�����</a></p>";
		}
		displayHtml(userName, strHtml);
	}
	
	private void displayHtml(String userName, String strHtml) {
		String ecd = this.request.getEncoding();
		this.sendStr = "HTTP/1.1 200 OK\r\n"
				+ "Content-Type: text/html;charset=" + ecd + "\r\n"
				+ "Set-Cookie: name=" + userName + "\r\n"
				+ "\r\n"
				+ "<html><title>��¼��Ϣ</title><body>" + strHtml + "</body></html>";
	}
}
