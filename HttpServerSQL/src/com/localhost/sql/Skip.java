package com.localhost.sql;

public class Skip implements Servlet {
	private String url;

	Skip(String url) {
		this.url = url;
	}
	public void service(HttpRequest request, HttpResponse response) {
		extractInfo(request.getCookies(), response);
		extractInfo(request.getParameter(), response);
		response.setStatus(200, "OK");
		response.setEncoding("text/html", request.getEncoding());
		response.setHeader("Content-Type", response.getEncoding());
		response.setHeader("Set-Cookie", response.getCookies());
		response.setResponse("<html><head><meta http-equiv=\"refresh\" content=\"0;url=http://localhost:8080/"
				+ this.url + "\">" + "</head><body></body></html>");
	}

	// 如果cookie中有内容或者post参数有值时设置cookie
	private void extractInfo(String info, HttpResponse response) {
		if (info != null && info != "") {
			String[] cookieInfo = info.split("&");
			for (int i = 0; i < cookieInfo.length; ++i) {
				String name = cookieInfo[i].split("=")[0].trim();
				String value = cookieInfo[i].split("=")[1].trim();
				response.setCookie(name, value);
			}
		}
	}}
