package socket;

import java.net.*;
import java.io.*;

public class MySocketClient {

	public static void main(String[] args) {

		// ����̨���������IP�Ͷ˿ںţ�Ĭ�϶˿ں�Ϊ12345
		if ((args.length < 1) || (args.length > 2)) {
			throw new IllegalArgumentException(
					"Parameter(s): <Server> [<Port>]");
		}
		// ������IP�ַ���
		String server = args[0];
		// �������˿ںţ���ѡ�Ĭ��12345
		int serverPort = (args.length == 2) ? Integer.parseInt(args[1]) : 12345;

		System.out.println("Connected to server...sending string. ");
		System.out.println("The client does not stop until input \"exit\".");

		while (true) {
			Socket socket = null;
			try {
				// �����ͻ���Socket������������˿ںź�IP
				socket = new Socket(server, serverPort);
				//�������������
				DataInputStream inputFromServer = new DataInputStream(
						socket.getInputStream());
				DataOutputStream outputToServer = new DataOutputStream(
						socket.getOutputStream());
				//��������������Ϣ
				System.out.print("Please Enter: \t ");
				String data = new BufferedReader(new InputStreamReader(
						System.in)).readLine();
				outputToServer.writeUTF(data);
				//outputToServer.flush();

				// socket.setSoTimeout(5 * 1000);
				//���շ��������ص���Ϣ
				String rev = inputFromServer.readUTF();
				System.out.println("Server feedback: " + rev);

				if (rev.equals("exit")) {
					System.out
							.println("The connect will be closed by client. ");
					break;
				}
				//�ر������������socket
				outputToServer.close();
				inputFromServer.close();
				socket.close();
			} catch (IOException ex) {
				System.err.println(ex);
			} 
		}
	}
}