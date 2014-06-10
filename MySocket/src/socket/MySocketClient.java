package socket;

import java.net.*;
import java.io.*;

public class MySocketClient {

	public static void main(String[] args) {

		// 控制台输入服务器IP和端口号，默认端口号为12345
		if ((args.length < 1) || (args.length > 2)) {
			throw new IllegalArgumentException(
					"Parameter(s): <Server> [<Port>]");
		}
		// 服务器IP字符串
		String server = args[0];
		// 服务器端口号，可选项，默认12345
		int serverPort = (args.length == 2) ? Integer.parseInt(args[1]) : 12345;

		System.out.println("Connected to server...sending string. ");
		System.out.println("The client does not stop until input \"exit\".");

		while (true) {
			Socket socket = null;
			try {
				// 创建客户端Socket，输入服务器端口号和IP
				socket = new Socket(server, serverPort);
				//定义输入输出流
				DataInputStream inputFromServer = new DataInputStream(
						socket.getInputStream());
				DataOutputStream outputToServer = new DataOutputStream(
						socket.getOutputStream());
				//给服务器发送消息
				System.out.print("Please Enter: \t ");
				String data = new BufferedReader(new InputStreamReader(
						System.in)).readLine();
				outputToServer.writeUTF(data);
				//outputToServer.flush();

				// socket.setSoTimeout(5 * 1000);
				//接收服务器返回的消息
				String rev = inputFromServer.readUTF();
				System.out.println("Server feedback: " + rev);

				if (rev.equals("exit")) {
					System.out
							.println("The connect will be closed by client. ");
					break;
				}
				//关闭输入输出流和socket
				outputToServer.close();
				inputFromServer.close();
				socket.close();
			} catch (IOException ex) {
				System.err.println(ex);
			} 
		}
	}
}