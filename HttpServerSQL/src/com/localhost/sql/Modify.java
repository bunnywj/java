package com.localhost.sql;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Modify implements Servlet {

	@Override
	public void service(HttpRequest request, HttpResponse response) {
		try {
			String ecd = request.getEncoding();
			File file = new File("modify.txt");

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
			response.setCookie("username",
					request.getCookies().split("&")[0].split("=")[1].trim());
			response.setHeader("Set-Cookie", response.getCookies());
			response.setResponse(new String(buf, ecd));
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
	}
}
