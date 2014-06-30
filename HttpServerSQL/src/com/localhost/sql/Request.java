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
					.println("--------------------------�������������----------------------------");
			String line = reader.readLine();
			System.out.println("��������������������ݣ�\n" + line);
			System.out.println();
			if (line != null) {
				this.uri = parseUri(line);
				this.method = parseMethod(line);

				System.out
						.println("���������и�ʽ\"Method Request-URI HTTP-Version CRLF\","
								+ "�õ���Request-URI���ݣ� " + uri);
				System.out.println();
				// ѭ�������Ϣͷ������
				System.out.println("�����Ϣͷ�����ݣ�");
				int bodyLen = 0;
				while (!(line = reader.readLine()).equals("")) {
					System.out.println(line);
					// ���������˷��͵���Ϣ�а���Accept-Charset.���Ƿ����Ĭ�ϱ��룬����������������������Accept-Charset���͵ı��뷽ʽ��
					// �ȼ���ߵķ�ʽ����
					parseCharset(line);
					// ����ΪPOSTʱ����ȡbody�ĳ���
					if (line.startsWith("Content-Length")) {
						bodyLen = Integer.parseInt(line.split(":")[1].trim());
					}
				}
				if (bodyLen != 0) {
					// �ж��а�����body���ݣ����ճ��ȶ�ȡbody�ֽ���
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
		// ������Ϣ�����ݣ���ȡcharset����
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
