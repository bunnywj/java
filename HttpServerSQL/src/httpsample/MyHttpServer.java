package httpsample;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.util.HashMap;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.io.BufferedReader;

public class MyHttpServer {
	public static HashMap<String, String> mapUser = new HashMap<String, String>();
	
	public static void main(String[] args) throws Exception {
		// 检查参数，如果参数不是端口号和html文件路径，则提示并抛出异常
		try {
			if (args.length != 1) {
				throw new IllegalArgumentException("请输入正确的参数:<Port>");
			}
		} catch (IllegalArgumentException ex) {
			System.out.println(ex.getMessage());
		}
		
		uploadUserInfo(mapUser, "user.txt");
		
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

	private static void uploadUserInfo(HashMap<String, String> user, String fileName){
		File file = new File(fileName);
		try {
			if (!file.exists()) {
				throw new IllegalArgumentException("用户信息文件未正确加载。");
			}
			BufferedReader bufReader = new BufferedReader(new FileReader(file));
			String line = null;
			while ((line = bufReader.readLine()) != null) {
				user.put(line.split("[\f\t\r ]")[0].trim(), line.split("[\f\t\r ]")[1].trim());
			}	
			bufReader.close();
		} catch (IllegalArgumentException ex) {
			System.out.println(ex.getMessage());
		} catch (FileNotFoundException ex) {
			System.out.println(ex.getMessage());
		} catch (IOException ex){
			System.out.println(ex.getMessage());
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
			InputStream input = socket.getInputStream();
			OutputStream output = socket.getOutputStream();

			Request request = new Request(input);
			request.parse();

			Response response = new Response(output);
			response.setRequest(request);
			response.send();

			input.close();
			output.close();
		} catch (IOException e) {
			System.out.println(e);
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
