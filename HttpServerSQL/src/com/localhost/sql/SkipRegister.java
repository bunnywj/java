package com.localhost.sql;

public class SkipRegister extends Skip implements Servlet{

	public void service(HttpRequest request, HttpResponse response) {
		super.url = "register.html";
		super.service(request, response);	
	}
}
