package com.localhost.sql;

//����HTTPЭ����Ӧͷ
public class HttpResponse {
	//�洢cookieֵ
	private String cookie;
	//�洢��Ӧ״̬
	private String status;
	//�洢��Ϣͷ����
	private String header;
	//�洢������Ϣ
	private String encoding;
	//�洢��Ӧͷ������
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
