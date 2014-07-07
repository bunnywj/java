package com.localhost.sql;

public class Skip implements Servlet {
	@Override
	public void service(HttpRequest request, HttpResponse response) {
		response.setStatus(200, "OK");
		response.setEncoding("text/html", request.getEncoding());
		response.setResponse("<html><body><form method=\"GET\" "
				+ "action=\"http://localhost:8080/Main.html\">"
				+ "</body></html>");
		}
}
