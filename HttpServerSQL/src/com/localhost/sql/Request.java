package com.localhost.sql;

import java.io.InputStream;
import java.net.URLDecoder;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class Request {
	private InputStream input;
	private String uri;
	private String method;
	private String[] body;
	private String encoding;
	private boolean submit;
	private boolean register;

	public Request() {
		this.input = null;
		this.uri = "";
		this.method = "";
		this.encoding = "UTF-8";
	}

	public Request(InputStream input) {
		this.input = input;
		this.uri = "";
		this.method = "";
		this.encoding = "UTF-8";
	}

	public String[] getBody() {
		return this.body;
	}

	public String getUri() {
		return this.uri;
	}

	public String getMethod() {
		return this.method;
	}

	public String getEncoding() {
		return this.encoding;
	}

	public boolean getSubmit() {
		return this.submit;
	}

	public boolean getRegister() {
		return this.register;
	}

	public void parse() {
		try {

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					this.input));

			System.out
					.println("--------------------------浏览器发送请求----------------------------");
			String line = reader.readLine();
			System.out.println("浏览器发来的请求行内容：\n" + line);
			System.out.println();
			if (line != null) {
				this.uri = parseUri(line);
				this.method = parseMethod(line);

				System.out
						.println("根据请求行格式\"Method Request-URI HTTP-Version CRLF\","
								+ "得到的Request-URI内容： " + uri);
				System.out.println();
				// 循环输出消息头的内容
				System.out.println("输出消息头的内容：");
				int bodyLen = 0;
				while (!(line = reader.readLine()).equals("")) {
					System.out.println(line);
					// 如果浏览器端发送的消息中包含Accept-Charset.看是否包含默认编码，如果不包含，则服务器按照Accept-Charset发送的编码方式中
					// 等级最高的方式发送
					parseCharset(line);
					// 请求为POST时，读取body的长度
					if (line.startsWith("Content-Length")) {
						bodyLen = Integer.parseInt(line.split(":")[1].trim());
					}
				}
				if (bodyLen != 0) {
					// 判断有包含的body内容，则按照长度读取body字节流
					byte[] bodyContent = new byte[bodyLen];
					for (int i = 0; i < bodyLen; ++i) {
						bodyContent[i] = (byte) reader.read();
					}
					String tmpBody = new String(bodyContent, this.encoding);
					System.out.println(tmpBody);
					String[] arrStr = tmpBody.split("&");

					int len = arrStr.length - 1;
					String name = arrStr[len].split("=")[0].trim();
					if ("Submit".equals(name)) {
						this.submit = true;
					}
					if ("Register".equals(name)) {
						this.register = true;
					}

					this.body = new String[len];
					for (int i = 0; i < len; ++i) {
						this.body[i] = arrStr[i].split("=")[1].trim();
						this.body[i] = URLDecoder.decode(this.body[i],
								this.encoding);
						System.out.println(this.body[i]);
					}
				}
				System.out.println();
			}
		} catch (UnsupportedEncodingException ex) {
			System.out.println(ex.getMessage());
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
	}

	private String parseUri(String requestLine) {
		return requestLine.split(" ")[1].trim();
	}

	private String parseMethod(String requestLine) {
		return requestLine.split(" ")[0].trim();
	}

	private void parseCharset(String line) {
		boolean flag = false;
		// 划分消息的内容，获取charset编码
		int idx1 = line.indexOf(":");
		if (idx1 != -1
				&& "Accept-Charset".equals(line.substring(0, idx1).trim())) {
			String[] charset = line.split(",");
			for (int i = 0; i < charset.length; ++i) {
				int idx = 0;
				if ((idx = charset[i].indexOf(";")) != -1) {
					charset[i] = charset[i].substring(0, idx);
				}
				if (charset[i].startsWith(encoding)) {
					flag = true;
				}
			}
			if (!flag) {
				encoding = charset[0];
			}
		}
	}

}
