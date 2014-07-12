package com.localhost.sql;

//设置HTTP协议响应头
public class HttpResponse {
	//存储cookie值
	private String cookie;
	//存储响应状态
	private String status;
	//存储消息头内容
	private String header;
	//存储编码信息
	private String encoding;
	//存储响应头的内容
	private String reponseStr;
	
	HttpResponse() {
		this.cookie = "";
		this.status = "";
		this.header = "";
		this.encoding = "";
		this.reponseStr = "";
	}

	public void setCookie(String name, String value) {
		this.cookie = this.cookie + name + "=" + value + "&";
	}
	
	public void setStatus(int statusCode, String message) {
		this.status = String.valueOf(statusCode) + " " + message + "\r\n";
	}

	public void setHeader(String name, String value) {
		this.header = this.header + name + ":" + value + "\r\n";
	}

	public void setEncoding(String contentType, String encoding) {
		this.encoding = contentType + ";charset=" + encoding;
	}

	public String getCookies() {
		return this.cookie;
	}
	
	public String getEncoding() {
		return this.encoding;
	}
	
	public void setResponse(String strHtml) {
		this.reponseStr = "HTTP/1.1 " + this.status + this.header
				 + "\r\n" + strHtml;
	}
	
	public String getResponse(){
		return this.reponseStr;
	}
}
