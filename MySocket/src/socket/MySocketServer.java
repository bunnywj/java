package socket;

import java.net.*;
import java.io.*;

public class MySocketServer {

	static int serverPort = 0;

	public static void main(String[] args) {

		if (args.length != 1)
			throw new IllegalArgumentException("Parameter(s): <Port>");

		serverPort = Integer.parseInt(args[0]);

		System.out.println("The server starts...");
		MySocketServer server = new MySocketServer();
		server.init();
	}

	public void init() {
		try {
//			int clientCount = 1;
			//服务器端Socket
			ServerSocket serverSocket = new ServerSocket(serverPort);

			while (true) {
				//监听客户端消息
				Socket socket = serverSocket.accept();
				//输出客户端地址，有问题，会导致阻塞
//				InetAddress clientAddress = socket.getInetAddress();
//				System.out.println("client" + clientCount + "'s homename is "
//						+ clientAddress.getHostName());
//				System.out.println("client" + clientCount + "'s IP Address is "
//						+ clientAddress.getHostAddress());

				new HandleAClient(socket);

//				++clientCount;
			}
		} catch (IOException ex) {
			System.err.println(ex);
		}
	}

	private class HandleAClient implements Runnable {
		private Socket socket;

		public HandleAClient(Socket socket) {
			this.socket = socket;
			new Thread(this).start();
		}

		public void run() {
			try {
				// 读取客户端数据
				DataInputStream inputFromClient = new DataInputStream(
						socket.getInputStream());
				String revClient = inputFromClient.readUTF();
				//查看客户端消息
				System.out.println("Received client: " + revClient);

				// socket.setSoTimeout(5 * 1000);
				// 向客户端回复信息
				DataOutputStream outputToClient = new DataOutputStream(
						socket.getOutputStream());
				System.out.print("Respond client, Please Enter: \t ");
				String sedServer = new BufferedReader(new InputStreamReader(
						System.in)).readLine();
				outputToClient.writeUTF(sedServer);
				
				//关闭输入输出流和socket
				inputFromClient.close();
				outputToClient.close();
				socket.close();
			} catch (IOException ex) {
				System.err.println(ex);
			}
		}
	}
}
