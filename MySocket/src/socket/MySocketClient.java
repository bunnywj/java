package socket;

import java.net.*;
import java.io.*;

public class MySocketClient {

	public static void main(String[] args) throws IOException {

		if ((args.length < 1) || (args.length > 2)) {
			throw new IllegalArgumentException(
					"Parameter(s): <Server> [<Port>]");
		}

		String server = args[0]; 
		int serverPort = (args.length == 2) ? Integer.parseInt(args[1]) : 12345;

		System.out.println("Connected to server...sending string. ");
		System.out
				.println("The client does not stop until input \" exit \".");

		while (true) {
			Socket socket = new Socket(server, serverPort);

			DataInputStream in = new DataInputStream(socket.getInputStream());
			DataOutputStream out = new DataOutputStream(
					socket.getOutputStream());

			System.out.print("Please Enter: \t ");
			String data = new BufferedReader(new InputStreamReader(System.in))
					.readLine();
			out.writeUTF(data);
			System.out.println("Client sending: " + data);
			String rev = in.readUTF();
			System.out.println("Server feedback: " + rev);

			if (rev.equals("exit")) {
				System.out.println("The connect will be closed by client. ");
				break;
			}
			in.close();
			out.close();
	//		socket.close();
		}
	}
}
