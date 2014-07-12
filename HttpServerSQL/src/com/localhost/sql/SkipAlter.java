package com.localhost.sql;

public class SkipAlter extends Skip implements Servlet{

	public void service(HttpRequest request, HttpResponse response) {
		super.url = "alter.html";
		super.service(request, response);	
	}
}
