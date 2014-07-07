package com.localhost.sql;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Config {
	private String driver;
	private String url;
	private String user;
	private String password;

	Config() {
		this.driver = null;
		this.url = null;
		this.user = null;
		this.password = null;
	}

	public String getDriver() {
		return this.driver;
	}
	
	public String getUrl() {
		return this.url;
	}
	
	public String getUser() {
		return this.user;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public void fromFile(String fileName) {
		try {
			File file = new File(fileName);
			if (file.exists()) {
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String line = null;
				while ((line = reader.readLine()) != null) {
					String paraName = line.split("=")[0].trim();
					String paraContent = line.split("=")[1].trim();
					if ("driver".equalsIgnoreCase(paraName)) {
						this.driver = paraContent;
					}
					if ("url".equalsIgnoreCase(paraName)) {
						this.url = paraContent;
					}
					if ("user".equalsIgnoreCase(paraName)) {
						this.user = paraContent;
					}
					if ("password".equalsIgnoreCase(paraName)) {
						this.password = paraContent;
					}					
				}
				reader.close();
				System.out.println("已读入SQL配置文件。");
			} else {
				System.exit(1);
			}

		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
	}
}
