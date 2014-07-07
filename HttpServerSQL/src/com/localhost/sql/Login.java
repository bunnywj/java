package com.localhost.sql;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Login implements Servlet {
	@Override
	public void service(HttpRequest request, HttpResponse response) {
		try {
			String ecd = request.getEncoding();
			File file = new File(request.getRequestURI());

			if (file.exists()) {
				FileInputStream inFile = new FileInputStream(file);
				byte[] buf = new byte[inFile.available()];
				inFile.read(buf);
				inFile.close();
				response.setStatus(200, "OK");
				response.setEncoding("text/html", ecd);
				response.setResponse(new String(buf, ecd));
			} else {
				response.setStatus(404, "File NOT Fount");
				response.setEncoding("text/html", ecd);
				response.setResponse("<html><body><h1>File Not Found</h1></body></html>");
			}
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
	}
}
