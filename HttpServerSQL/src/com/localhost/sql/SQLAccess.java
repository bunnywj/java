package com.localhost.sql;

import java.sql.*;

public class SQLAccess {
	private String url = "jdbc:mysql://localhost:3306/myfirstdb";
	private String user = "root";
	private String password = "bunnywj";
	public ResultSet result;
	
	public void accessMySQL() {
		try {
		Connection conn = DriverManager.getConnection(this.url, this.user, this.password);
		if (conn.isClosed()) {
			throw new Exception("connection failure");
		}
		Statement statement = conn.createStatement();
		String sql = "select * from login";
		this.result = statement.executeQuery(sql);
		} catch (ClassNotFoundException ex) {
			System.out.println(ex.getMessage());
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
	}
}
