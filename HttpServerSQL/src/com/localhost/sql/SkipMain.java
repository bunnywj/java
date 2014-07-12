package com.localhost.sql;

public class SkipMain extends Skip implements Servlet{

	public void service(HttpRequest request, HttpResponse response) {
		super.url = "main.html";
		super.service(request, response);	
	}
}
