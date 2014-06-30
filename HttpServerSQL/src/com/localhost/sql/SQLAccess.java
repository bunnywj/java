package com.localhost.sql;

import java.sql.*;

public class SQLAccess {
	private String url;
	private String user;
	private String password;
	private Connection conn;

	public ResultSet result;
	
	SQLAccess(String url, String user, String password) {
		this.url = url;
		this.user = user;
		this.password = password;
	}
	
	public void query() {
		try {
			String sql = "select * from login";
			PreparedStatement preStatement = conn.prepareStatement(sql);
			this.result = preStatement.executeQuery(sql);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	public void updata(String userName, String password) {
		try {
			String sql = "insert into TEST_LOGIN (username, password) values (?, ?)";
			PreparedStatement preStatement = conn.prepareStatement(sql);
			preStatement.setString(1, userName);
			preStatement.setString(2, password);
			this.result = preStatement.executeQuery();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	public void initDB() {
		try {
			this.conn = DriverManager.getConnection(this.url, this.user,
					this.password);
			if (this.conn.isClosed()) {
				throw new Exception("connection failure");
			}
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
	}
	
	public void connClose(){
		try {
			this.conn.close();		
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
	}
}
