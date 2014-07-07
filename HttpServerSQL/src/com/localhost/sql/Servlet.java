package com.localhost.sql;

public interface Servlet {
	public HttpRequest request = null;
	public HttpResponse response = null;
	
	public void service(HttpRequest request, HttpResponse response);
}
