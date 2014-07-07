package com.localhost.sql;

import java.sql.*;

public class SQLAccess {
	private Connection conn;

	public ResultSet result;
	
	SQLAccess() {
		this.conn = null;
	}
	
	public void query() {
		try {
			String sql = "select * from TEST_LOGIN";
			PreparedStatement preStatement = conn.prepareStatement(sql);
			this.result = preStatement.executeQuery(sql);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	public void insert(String userName, String password) {
		try {
			String sql = "insert into TEST_LOGIN (username, password) values (?, ?)";
			PreparedStatement preStatement = conn.prepareStatement(sql);
			preStatement.setString(1, userName);
			preStatement.setString(2, password);
			preStatement.execute();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	public void update(String userName, String password) {
		try {
			String sql = "update TEST_LOGIN set password=? where username=?";
			PreparedStatement preStatement = conn.prepareStatement(sql);
			preStatement.setString(1, password);
			preStatement.setString(2, userName);
			preStatement.execute();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	public void initDB(String driver, String url, String user, String password) {
		try {
			Class.forName(driver);
			this.conn = DriverManager.getConnection(url, user, password);
			if (this.conn.isClosed()) {
				throw new Exception("connection failure");
			}
			
			if (!hasTable("TEST_LOGIN")) {
				createTable();
				System.out.println("在数据库中建表TEST_LOGIN");
			}

		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
	}
	
	public void connClose() {
		try {
			this.conn.close();
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
	}

	public String displayTableData(String encoding) {
		String str = "";
		try {
			str = "HTTP/1.1 200 OK\r\n"
					+ "Content-Type: text/html;charset="
					+ encoding
					+ "\r\n"
					+ "\r\n"
					+ "<html><title>数据库表信息</title><body><table><tr><td>用户名</td><td>密码</td></tr>";
			while (this.result.next()) {
				str = str + "<tr><td>" + this.result.getString(1) + "</td><td>"
						+ this.result.getString(2) + "</td></tr>";
			}
			str = str + "</h3></body></html>";
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
		return str;
	}

	public boolean hasTable(String tableName) {
		boolean flag = false;
		try {
			DatabaseMetaData dmd = this.conn.getMetaData();
			ResultSet rs = dmd.getTables(null, null, tableName,
					new String[] { "TABLE" });
			if (rs.next()) {
				flag = true;
			}
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
		return flag;
	}

	public void createTable() {
		try {
			String createSql = "create table TEST_LOGIN (username varchar(45) not null, "
					+ "password varchar(45) not null, primary key (username))";
			PreparedStatement preparedStatement = conn.prepareStatement(createSql);
			preparedStatement.execute();
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
	}

	public int authentication(String userName, String password) {
		int flag = 0;
		try {
			query();
			while (this.result.next()) {
				if (this.result.getString(1).equals(userName)) {
					if (this.result.getString(2).equals(password)) {
						flag = 1;
						break;
					} else {
						flag = 2;
						break;
					}
				}
			}
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
		return flag;
	}
}
