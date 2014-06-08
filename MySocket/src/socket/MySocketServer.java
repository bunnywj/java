package socket;

import java.net.*;
import java.io.*;

public class MySocketServer {

	public static void main(String[] args) throws IOException {

		if (args.length != 1)
			throw new IllegalArgumentException("Parameter(s): <Port>");

		int serverPort = Integer.parseInt(args[0]);
		//int serverPort = 12345;
		ServerSocket serverSocket = new ServerSocket(serverPort);
		
		while (true) {
			Socket socket = serverSocket.accept();

			SocketAddress clientAddress = socket.getRemoteSocketAddress();
			System.out.println("Handling client at " + clientAddress);

			DataInputStream in = new DataInputStream(socket.getInputStream());
			String revClient = in.readUTF();
			System.out.println("Received client: " + revClient);

			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			System.out.print("Respond client, Please Enter: \t ");
			String sedServer = new BufferedReader(new InputStreamReader(
					System.in)).readLine();
			out.writeUTF(sedServer);
			
			in.close();
			out.close();
			//socket.close();
		}
	}
}
