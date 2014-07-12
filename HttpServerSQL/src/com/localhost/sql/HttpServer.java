package com.localhost.sql;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.util.Hashtable;
import java.io.IOException;

public class HttpServer {
	public static SQL sql;

	public static void main(String[] args) throws Exception {
		// ������������������Ƕ˿ںź�html�ļ�·��������ʾ���׳��쳣
		try {
			if (args.length != 1) {
				throw new IllegalArgumentException("��������ȷ�Ĳ���:<Port>");
			}
		} catch (IllegalArgumentException ex) {
			System.out.println(ex.getMessage());
		}
		// �������ݿ��������Ϣ
		Config config = new Config();
		config.fromFile("sqlConfig.txt");

		// ��ʼ����jdbc�������ݿ�����
		sql = new SQL();
		sql.initDB(config.getDriver(), config.getUrl(), config.getUser(),
				config.getPassword());

		// ����XML������Ϣ
		ParseXML xml = new ParseXML();
		xml.parseXML("action.xml");

		// ����������
		ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]));
		System.out.println("������������...\n");
		try {
			// ѭ�������ͻ��˷�������
			while (true) {
				// ����socket�������ͻ��˷�������
				System.out.println("�����˿ں�" + args[0] + "��������");
				Socket socket = serverSocket.accept();

				System.out.println("�����̴߳�������");
				// �����߳�
				Runnable process = new threadProcess(socket, xml);
				new Thread(process).start();
			}
		} finally {
			sql.connClose();
			serverSocket.close();
			System.exit(1);
		}
	}
}

class threadProcess implements Runnable {
	private Socket socket;
	private ParseXML xml;

	threadProcess(Socket socket, ParseXML xml) {
		this.socket = socket;
		this.xml = xml;
	}

	public void run() {
		try {
			// ����socket����/����������ڶ�д�������Ϣ
			InputStream input = this.socket.getInputStream();
			OutputStream output = this.socket.getOutputStream();
			// ������������͵�������Ϣ
			HttpRequest request = new HttpRequest();
			request.parseFromStream(input);
			HttpResponse response = new HttpResponse();
			// ��ѯ�����ĸ�����ҳ��

			Hashtable<String, Object> objectTable = this.xml.getObjectTable();
			Servlet servlet = (Servlet) objectTable
					.get(request.getRequestURI());
			if (servlet == null) {
				response.setStatus(404, "File NOT Fount");
				response.setEncoding("text/html", request.getEncoding());
				response.setHeader("Content-Type", response.getEncoding());
				response.setCookie("Set-Cookie", request.getCookies());
				response.setResponse("<html><body><h1>File Not Found</h1></body></html>");
			} else {
				servlet.service(request, response);
			}
			
			// ���������ͻ��˷�����Ϣ
			try {
				output.write(response.getResponse().getBytes(
						request.getEncoding()));
			} catch (IOException ex) {
				System.out.println(ex.getMessage());
			}
			input.close();
			output.close();
			// ����̨�����������
			consoleDisplay(request.getEncoding(), response.getResponse());
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		} finally {
			if (this.socket != null) {
				try {
					this.socket.close();

				} catch (IOException e) {
					this.socket = null;
					System.out.println(e.getMessage());
				}
			}
		}
	}

	private void consoleDisplay(String ecd, String sendStr) {
		System.out
				.println("--------------------------������������Ӧ----------------------------");
		System.out.println("��������Ӧ�ı��뷽ʽΪ��" + ecd);
		System.out.println();
		System.out.println("���ݣ�" + sendStr);
	}
}
