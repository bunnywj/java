package com.localhost.sql;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
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
		ReadFile readFile = new ReadFile();
		readFile.readFromFile("sqlConfig.txt");
		// ��ʼ����jdbc�������ݿ�����
		SQLAccess sqlAcc = new SQLAccess();
		sqlAcc.initDB(readFile.driver, readFile.url, readFile.user,
				readFile.password);
		System.out.println("JDBC�������ݿ�����");

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
				Runnable process = new Process(socket, sqlAcc);
				new Thread(process).start();
			}
		} finally {
			serverSocket.close();
			sqlAcc.connClose();
		}
	}
}

class Process implements Runnable {
	private Socket socket;
	private SQLAccess sqlAcc;

	Process(Socket socket, SQLAccess sqlAcc) {
		this.socket = socket;
		this.sqlAcc = sqlAcc;
	}

	public void run() {
		try {
			// ����socket����/����������ڶ�д�������Ϣ
			InputStream input = this.socket.getInputStream();
			OutputStream output = this.socket.getOutputStream();
			// ������������͵�������Ϣ
			Request request = new Request(input);
			request.parse();
			// ��������Ӧ�������������Ϣ
			Response response = new Response(output);
			response.setSQLAccess(sqlAcc);
			response.setRequest(request);
			response.send();

			input.close();
			output.close();
		} catch (IOException e) {
			System.out.println(e);
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

}