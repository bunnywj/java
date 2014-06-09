package socket;

import java.net.*;
import java.io.*;

public class MySocketServer {

	public static void main(String[] args) {

		if (args.length != 1)
			throw new IllegalArgumentException("Parameter(s): <Port>");

		int serverPort = Integer.parseInt(args[0]);
		// int serverPort = 12345;
		try {
			int clientCount = 1;

			ServerSocket serverSocket = new ServerSocket(serverPort);

			while (true) {
				Socket socket = serverSocket.accept();
				InetAddress clientAddress = socket.getInetAddress();
				System.out.println("client" + clientCount +"'s homename is " + clientAddress.getHostName());
				System.out.println("client" + clientCount +"'s IP Address is " + clientAddress.getHostAddress());
				
				HandleAClient task = (new MySocketServer()).new HandleAClient(
						socket);
				new Thread(task).start();

				++clientCount;
			}
		} catch (IOException ex) {
			System.err.println(ex);
		}
	}

	public class HandleAClient implements Runnable {
		private Socket socket;

		public HandleAClient(Socket socket) {
			this.socket = socket;
		}

		public void run() {
			try {
				DataInputStream inputFromClient = new DataInputStream(
						socket.getInputStream());
				DataOutputStream outputToClient = new DataOutputStream(
						socket.getOutputStream());
				while (true) {
					String revClient = inputFromClient.readUTF();
					System.out.println("Received client: " + revClient);

					socket.setSoTimeout(5 * 1000);
					
					System.out.print("Respond client, Please Enter: \t ");
					String sedServer = new BufferedReader(
							new InputStreamReader(System.in)).readLine();
					outputToClient.writeUTF(sedServer);
					outputToClient.flush();
				}
			} catch (IOException ex) {
				System.err.println(ex);
			}
		}
	}
}
