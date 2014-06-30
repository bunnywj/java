package httpsample;

import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.File;

public class Response {
	private OutputStream output;
	private Request request;
	private String sendStr;

	public Response() {
		this.output = null;
		this.request = new Request();
	}

	public Response(OutputStream output) {
		this.output = output;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

	public void send() {
		if ("GET".equalsIgnoreCase(this.request.getMethod())) {
			doGET();
		} else if ("POST".equalsIgnoreCase(this.request.getMethod())) {
			doPOST();
		}

		System.out
				.println("--------------------------服务器发送响应----------------------------");
		String ecd = this.request.getEncoding();
		try {
			this.output.write(this.sendStr.getBytes(ecd));
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}

		System.out.println("服务器响应的编码方式为：" + ecd);
		System.out.println();
		System.out.println("内容：" + this.sendStr);
	}

	// 如果浏览器发来的请求方式为GET，则读文件，显示首页内容
	private void doGET() {
		try {
			String ecd = this.request.getEncoding();
			File file = new File("text.html");

			if (file.exists()) {
				FileInputStream inFile = new FileInputStream(file);
				byte[] buf = new byte[inFile.available()];
				inFile.read(buf);
				inFile.close();
				this.sendStr = "HTTP/1.1 200 OK\r\n"
						+ "Content-Type: text/html;charset=" + ecd + "\r\n"
						+ "\r\n" + new String(buf, ecd);
			} else {
				this.sendStr = "HTTP/1.1 404 File NOT Fount\r\n"
						+ "Content-Type: text/html;charset=" + ecd + "\r\n"
						+ "\r\n" + "<h1>File Not Found</h1>";
			}
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
	}

	// 如果浏览器发来的请求方式为POST，则输出body中的内容
	private void doPOST() {
		String tmpStr = "<html><title>send from server</title><body><h3>服务器接收到的消息如下：</h3><p>"
				+ this.request.getBody().trim() + "</p></body></html>";
		this.sendStr = "HTTP/1.1 200 OK\r\n"
				+ "Content-Type: text/html;charset="
				+ this.request.getEncoding() + "\r\n" + "\r\n" + tmpStr;
	}

}
