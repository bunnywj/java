package com.localhost.sql;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Login implements Servlet {
	@Override
	public void service(HttpRequest request, HttpResponse response) {
		try {
			String ecd = request.getEncoding();
			File file = new File("login.txt");

			if (!file.exists()) {
				throw new IOException();
			}

			FileInputStream inFile = new FileInputStream(file);
			byte[] buf = new byte[inFile.available()];
			inFile.read(buf);
			inFile.close();
			response.setStatus(200, "OK");
			response.setEncoding("text/html", ecd);
			response.setHeader("Content-Type", response.getEncoding());
			response.setHeader("Set-Cookie", request.getCookies());
			response.setResponse(new String(buf, ecd));
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
	}
}
