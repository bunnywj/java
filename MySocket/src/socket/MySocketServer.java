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
			//��������Socket
			ServerSocket serverSocket = new ServerSocket(serverPort);

			while (true) {
				//�����ͻ�����Ϣ
				Socket socket = serverSocket.accept();
				//����ͻ��˵�ַ�������⣬�ᵼ������
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
				// ��ȡ�ͻ�������
				DataInputStream inputFromClient = new DataInputStream(
						socket.getInputStream());
				String revClient = inputFromClient.readUTF();
				//�鿴�ͻ�����Ϣ
				System.out.println("Received client: " + revClient);

				// socket.setSoTimeout(5 * 1000);
				// ��ͻ��˻ظ���Ϣ
				DataOutputStream outputToClient = new DataOutputStream(
						socket.getOutputStream());
				System.out.print("Respond client, Please Enter: \t ");
				String sedServer = new BufferedReader(new InputStreamReader(
						System.in)).readLine();
				outputToClient.writeUTF(sedServer);
				
				//�ر������������socket
				inputFromClient.close();
				outputToClient.close();
				socket.close();
			} catch (IOException ex) {
				System.err.println(ex);
			}
		}
	}
}
