package com.localhost.sql;

public class HttpResponse {
	private String cookie;
	private String status;
	private String header;
	private String encoding;

	private String reponseStr;

	HttpResponse() {
		this.cookie = null;
		this.status = null;
		this.header = null;
		this.encoding = null;
		this.reponseStr = null;
	}

	public void setCookie(String name) {
		this.cookie = "Set-Cookie: name=" + name + "; expiry=-1";
	}

	public void setStatus(int statusCode, String message) {
		this.status = String.valueOf(statusCode) + " " + message;
	}

	public void setHeader(String name, String value) {
		this.header = name + ":" + value;
	}

	public void setEncoding(String contentType, String encoding) {
		this.encoding = "Content-Type: " + contentType + ";charset=" + encoding;
	}

	public void setResponse(String strHtml) {
		this.reponseStr = "HTTP/1.1 " + this.status + "\r\n" + this.header
				+ "\r\n" + this.cookie + "\r\n" + this.encoding + "\r\n"
				+ "\r\n" + strHtml;
	}
	
	public String getResponse(){
		return this.reponseStr;
	}
}
