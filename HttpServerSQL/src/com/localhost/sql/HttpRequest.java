/*
 * html�ĸ�ʽ�� ������
 * 			��Ϣͷ
 * 			����
 * 			post�ύ�Ĳ�������
 */

package com.localhost.sql;

import java.io.InputStream;
import java.net.URLDecoder;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.Enumeration;

public class HttpRequest {
	//��inputStream��һ�ζ�ȡ����������ֽ���Ϊ65535
	final int MAX_VALUE = 65535;
	//�����洢html����Ϣͷ����Ϣͷ�����ƣ���Ϣͷ������
	private Hashtable<String, String> headers;
	//�����洢html��������
	private String header;
	//�������е�uri
	private String URI;
	//�������е�method
	private String method;
	//��Ϣͷ�е��ַ���
	private String encoding;
	//��Ϣͷ�е�cookie��Ϣ
	private String cookies;
	//post�ύ�Ĳ���ֵ���������ƣ�����ֵ
	private Hashtable<String, String> parameter;

	public HttpRequest() {
		this.headers = null;
		this.header = null;
		this.URI = null;
		this.method = null;
		this.encoding = "UTF-8";
		this.cookies = null;
		this.parameter = null;
	}

	public String getRequestURI() {
		return this.URI;
	}

	public String getMethod() {
		return this.method;
	}

	public String getHeader() {
		return this.header;
	}

	public String getEncoding() {
		return this.encoding;
	}

	public String getCookies() {
		return this.cookies;
	}

	public Enumeration<String> getHeaderNames() {
		return this.headers.keys();
	}

	public String getHeader(String name) {
		return this.headers.get(name);
	}

	public Enumeration<String> getParameterNames() {
		return this.parameter.keys();
	}

	public String getParameterValue(String name) {
		return this.parameter.get(name);
	}
	
	private String[] readHttpMsg(InputStream input) throws IOException {
		byte[] byteStream = new byte[MAX_VALUE];
		int httpMsgLen = input.read(byteStream);
		String httpMsg = new String(byteStream, 0, httpMsgLen);
		System.out
				.println("--------------------------�������������----------------------------");
		System.out.println(httpMsg);

		return httpMsg.split("\r\n");
	}

	public void parseFromStream(InputStream input) {
		try {
			String[] arrMsg = readHttpMsg(input);

			this.header = arrMsg[0];

			this.headers = new Hashtable<String, String>();
			for (int i = 1; i < arrMsg.length && !arrMsg[i].isEmpty(); ++i) {
				this.headers.put(arrMsg[i].split(":")[0],
						arrMsg[i].split(":")[1]);
			}

			parseURI();
			parseMethod();

			if (this.headers.get("Accept-Charset") != null) {
				parseCharset();
			}
			if (this.headers.get("cookie") != null) {
				parseCookie();
			}
			if (this.headers.get("Content-Length") != null) {
				parseBody(arrMsg[arrMsg.length - 1]);
			}
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
	}

	private void parseURI() {
		String tmpUri = this.header.split(" ")[1].trim();
		this.URI = tmpUri.substring(1, tmpUri.length()).trim();
	}

	private void parseMethod() {
		this.method = this.header.split(" ")[0].trim();
	}

	private void parseCharset() {
		boolean flag = false;
		// ������Ϣ�����ݣ���ȡcharset����
		String[] charset = this.headers.get("Accept-Charset").split(",");
		for (int i = 0; i < charset.length; ++i) {
			int idx = 0;
			if ((idx = charset[i].indexOf(";")) != -1) {
				charset[i] = charset[i].substring(0, idx);
			}
			if (charset[i].startsWith(this.encoding)) {
				flag = true;
			}
		}
		if (!flag) {
			this.encoding = charset[0];
		}
	}

	private void parseCookie() {
		this.cookies = this.headers.get("cookie").split(":")[1].trim();
	}

	private void parseBody(String body) throws UnsupportedEncodingException {
		this.parameter = new Hashtable<String, String>();
		String[] arrStr = body.split("&");
		for (int i = 0; i < arrStr.length; ++i) {
			String name = arrStr[i].split("=")[0].trim();
			String value = URLDecoder.decode(arrStr[i].split("=")[1].trim(),
					this.encoding);
			this.parameter.put(name, value);
		}
	}
}
