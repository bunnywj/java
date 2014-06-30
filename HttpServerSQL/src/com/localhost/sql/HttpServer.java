package com.localhost.sql;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.io.BufferedReader;

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
		// ����������
		ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]));
		System.out.println("������������...\n");
		// ѭ�������ͻ��˷�������
		while (true) {
			// ����socket�������ͻ��˷�������
			System.out.println("�����˿ں�" + args[0] + "��������");
			Socket socket = serverSocket.accept();
			System.out.println("�����̴߳�������");
			// �����߳�
			new threadProcess(socket);
		}
	}
}

class threadProcess implements Runnable {
	private Socket socket;

	threadProcess(Socket socket) {
		this.socket = socket;
		new Thread(this).start();
	}

	public void run() {
		try {
			//�������ݿ��������Ϣ
			File file = new File("sqlConfig.txt");
			SQLAccess sqlAcc = null;
			if (file.exists()) {
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String url = reader.readLine();
				String user = reader.readLine();
				String password = reader.readLine();
				reader.close();
				//��ʼ����jdbc�������ݿ�����
				sqlAcc = new SQLAccess(url, user, password);
				sqlAcc.initDB();
			} else {
				System.exit(1);
			}
			//����socket����/����������ڶ�д�������Ϣ
			InputStream input = socket.getInputStream();
			OutputStream output = socket.getOutputStream();
			//������������͵�������Ϣ
			Request request = new Request(input);
			request.parse();
			//������������͵�������Ϣ������Ӧ
			Response response = new Response(output);
			response.setRequest(request);
			response.setSQLAccess(sqlAcc);
			response.send();

			input.close();
			output.close();
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					socket = null;
					System.out.println(e.getMessage());
				}
			}
		}
	}
}
