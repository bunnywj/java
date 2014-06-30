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
		// 检查参数，如果参数不是端口号和html文件路径，则提示并抛出异常
		try {
			if (args.length != 1) {
				throw new IllegalArgumentException("请输入正确的参数:<Port>");
			}
		} catch (IllegalArgumentException ex) {
			System.out.println(ex.getMessage());
		}
		// 启动服务器
		ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]));
		System.out.println("服务器已启动...\n");
		// 循环监听客户端发来请求
		while (true) {
			// 建立socket，监听客户端发来请求
			System.out.println("监听端口号" + args[0] + "发来请求");
			Socket socket = serverSocket.accept();
			System.out.println("创建线程处理请求");
			// 创建线程
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
			//读入数据库的配置信息
			File file = new File("sqlConfig.txt");
			SQLAccess sqlAcc = null;
			if (file.exists()) {
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String url = reader.readLine();
				String user = reader.readLine();
				String password = reader.readLine();
				reader.close();
				//初始化，jdbc建立数据库连接
				sqlAcc = new SQLAccess(url, user, password);
				sqlAcc.initDB();
			} else {
				System.exit(1);
			}
			//定义socket输入/输出流，用于读写浏览器消息
			InputStream input = socket.getInputStream();
			OutputStream output = socket.getOutputStream();
			//解析浏览器发送的请求信息
			Request request = new Request(input);
			request.parse();
			//根据浏览器发送的请求信息进行响应
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
