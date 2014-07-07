package com.localhost.sql;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.io.IOException;

public class HttpServer {

	public static void main(String[] args) throws Exception {
		// 检查参数，如果参数不是端口号和html文件路径，则提示并抛出异常
		try {
			if (args.length != 1) {
				throw new IllegalArgumentException("请输入正确的参数:<Port>");
			}
		} catch (IllegalArgumentException ex) {
			System.out.println(ex.getMessage());
		}
		// 读入数据库的配置信息
		ReadFile readFile = new ReadFile();
		readFile.readFromFile("sqlConfig.txt");
		// 初始化，jdbc建立数据库连接
		SQLAccess sqlAcc = new SQLAccess();
		sqlAcc.initDB(readFile.driver, readFile.url, readFile.user,
				readFile.password);
		System.out.println("JDBC建立数据库连接");

		// 启动服务器
		ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]));
		System.out.println("服务器已启动...\n");
		try {
			// 循环监听客户端发来请求
			while (true) {
				// 建立socket，监听客户端发来请求
				System.out.println("监听端口号" + args[0] + "发来请求");
				Socket socket = serverSocket.accept();

				System.out.println("创建线程处理请求");
				// 创建线程
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
			// 定义socket输入/输出流，用于读写浏览器信息
			InputStream input = this.socket.getInputStream();
			OutputStream output = this.socket.getOutputStream();
			// 解析浏览器发送的请求信息
			Request request = new Request(input);
			request.parse();
			// 服务器响应浏览器的请求信息
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