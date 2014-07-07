package com.localhost.sql;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ReadFile {
	public String driver;
	public String url;
	public String user;
	public String password;

	ReadFile() {
		this.driver = null;
		this.url = null;
		this.user = null;
		this.password = null;
	}

	public void readFromFile(String fileName) {
		File file = new File(fileName);
		try {
			if (file.exists()) {
				BufferedReader reader = new BufferedReader(new FileReader(file));
				this.driver = reader.readLine();
				this.url = reader.readLine();
				this.user = reader.readLine();
				this.password = reader.readLine();
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
