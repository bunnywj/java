package com.localhost.sql;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.sql.SQLException;
import java.util.Hashtable;
import java.io.IOException;

public class HttpServer {

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
		SQL sql = new SQL();
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
				Runnable process = new threadProcess(socket, sql);
				new Thread(process).start();
			}
		} finally {
			sql.connClose();
			serverSocket.close();
		}
	}
}

class threadProcess implements Runnable {
	private Socket socket;
	private SQL sql;

	threadProcess(Socket socket, SQL sql) {
		this.socket = socket;
		this.sql = sql;
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

			Hashtable<String, String> actionTable = this.xml.getActionTable();
			String actionClass = actionTable.get(request.getRequestURI());
			if ("Login".equalsIgnoreCase(actionClass)) {
				new Login().service(request, response);
			} else if ("Skip".equalsIgnoreCase(actionClass)) {
				new Skip().service(request, response);
			} else if ("Main".equalsIgnoreCase(actionClass)) {
				new Main(this.sql).service(request, response);
			} else if ("Modify".equalsIgnoreCase(actionClass)) {
				if ("GET".equalsIgnoreCase(request.getMethod())) {
					new Login().service(request, response);
				} else if ("POST".equalsIgnoreCase(request.getMethod())) {
					new Modify(this.sql).service(request, response);
				}
			}

			

			
			
			
			//���������ͻ��˷�����Ϣ
			try {
				output.write(response.getResponse().getBytes(request.getEncoding()));
			} catch (IOException ex) {
				System.out.println(ex.getMessage());
			}
			input.close();
			output.close();
			//����̨�����������
			consoleDisplay(request.getEncoding(), response.getResponse());
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
//		} catch (SQLException ex) {
//			System.out.println(ex.getMessage());
		}finally {
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
