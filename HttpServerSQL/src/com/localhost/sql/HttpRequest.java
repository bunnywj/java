/*
 * html的格式： 请求行
 * 			消息头
 * 			空行
 * 			post提交的参数内容
 */

package com.localhost.sql;

import java.io.InputStream;
import java.net.URLDecoder;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.Enumeration;

public class HttpRequest {
	//从inputStream中一次读取的请求最大字节数为65535
	final int MAX_VALUE = 65535;
	//用来存储html的消息头，消息头的名称，消息头的内容
	private Hashtable<String, String> headers;
	//用来存储html的请求行
	private String header;
	//请求行中的uri
	private String URI;
	//请求行中的method
	private String method;
	//消息头中的字符集
	private String encoding;
	//消息头中的cookie信息
	private String cookies;
	//post提交的参数值，参数名称，参数值
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
				.println("--------------------------浏览器发送请求----------------------------");
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
		// 划分消息的内容，获取charset编码
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
