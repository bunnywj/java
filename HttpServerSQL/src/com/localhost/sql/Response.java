package com.localhost.sql;

import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.File;
import java.sql.SQLException;

public class Response {
	private OutputStream output;
	private Request request;
	private String sendStr;

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

	public void send() {
		if ("GET".equalsIgnoreCase(this.request.getMethod())) {
			doGET();
		} else if ("POST".equalsIgnoreCase(this.request.getMethod())) {
			doPOST();
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

	// ������������������ʽΪPOST�������body�е����ݮ�
	private void doPOST() {
		String ecd = this.request.getEncoding();

		this.sendStr = "HTTP/1.1 200 OK\r\n"
				+ "Content-Type: text/html;charset="
				+ this.request.getEncoding() + "\r\n" + "\r\n";

		String userName = this.request.getBody()[0];
		String password = this.request.getBody()[1];
		if (verification()) {
			this.sendStr = this.sendStr
					+ "<html><title>��֤���</title><body><table border=\"1\" align=\"center\">"
					+ "<tr><td>�û���</td><td>����</td></tr>" + "<tr><td>"
					+ userName + "</td><td>" + password
					+ "</td></tr></table></body></html>";
		} else {
			this.sendStr = this.sendStr
					+ "<html><title>��֤���</title><body><h3>��¼ʧ��</h3></body></html>";
		}
	}

	private boolean verification() {
		boolean flag = false;
		try {
			SQLAccess sqlAcc = new SQLAccess();
			sqlAcc.accessMySQL();

			while (sqlAcc.result.next()) {
				if (sqlAcc.result.getString(1)
						.equals(this.request.getBody()[0])
						&& sqlAcc.result.getString(2).equals(
								this.request.getBody()[1])) {
					flag = true;
					break;
				}
			}
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
		return flag;
	}
}
