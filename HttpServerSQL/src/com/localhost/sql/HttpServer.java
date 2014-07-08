package com.localhost.sql;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.sql.SQLException;
import java.util.Hashtable;
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
		Config config = new Config();
		config.fromFile("sqlConfig.txt");

		// 初始化，jdbc建立数据库连接
		SQL sql = new SQL();
		sql.initDB(config.getDriver(), config.getUrl(), config.getUser(),
				config.getPassword());

		// 读入XML配置信息
		ParseXML xml = new ParseXML();
		xml.parseXML("action.xml");

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
			// 定义socket输入/输出流，用于读写浏览器信息
			InputStream input = this.socket.getInputStream();
			OutputStream output = this.socket.getOutputStream();
			// 解析浏览器发送的请求信息
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

			

			
			
			
			//服务器给客户端发送信息
			try {
				output.write(response.getResponse().getBytes(request.getEncoding()));
			} catch (IOException ex) {
				System.out.println(ex.getMessage());
			}
			input.close();
			output.close();
			//控制台输出发送内容
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
				.println("--------------------------服务器发送响应----------------------------");
		System.out.println("服务器响应的编码方式为：" + ecd);
		System.out.println();
		System.out.println("内容：" + sendStr);
	}
}
