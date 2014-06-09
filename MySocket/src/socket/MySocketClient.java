package socket;

import java.net.*;
import java.io.*;

public class MySocketClient {

	public static void main(String[] args) {

		if ((args.length < 1) || (args.length > 2)) {
			throw new IllegalArgumentException(
					"Parameter(s): <Server> [<Port>]");
		}

		String server = args[0];
		int serverPort = (args.length == 2) ? Integer.parseInt(args[1]) : 12345;

		System.out.println("Connected to server...sending string. ");
		System.out.println("The client does not stop until input \"exit\".");

		try {

			Socket socket = new Socket(server, serverPort);

			DataInputStream inputFromServer = new DataInputStream(
					socket.getInputStream());
			DataOutputStream outputToServer = new DataOutputStream(
					socket.getOutputStream());
			
			while (true) {
				System.out.print("Please Enter: \t ");
				String data = new BufferedReader(new InputStreamReader(
						System.in)).readLine();
				outputToServer.writeUTF(data);
				outputToServer.flush();
				System.out.println("Client sending: " + data);

				socket.setSoTimeout(5 * 1000);

				String rev = inputFromServer.readUTF();
				System.out.println("Server feedback: " + rev);

				if (rev.equals("exit")) {
					System.out
							.println("The connect will be closed by client. ");
					break;
				}

			}
		} catch (IOException ex) {
			System.err.println(ex);
		}
	}
}
