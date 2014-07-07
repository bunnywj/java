package com.localhost.sql;

import java.sql.*;

public class SQL {
	private Connection conn;

	SQL() {
		this.conn = null;
	}

	public void initDB(String driver, String url, String user, String password) {
		try {
			Class.forName(driver);
			this.conn = DriverManager.getConnection(url, user, password);
			if (this.conn.isClosed()) {
				throw new Exception("connection ");
			}

			if (!hasTable("TEST_LOGIN")) {
				createTable();
				System.out.println("在数据库中建表TEST_LOGIN");
			}
			System.out.println("JDBC建立数据库连接");
		} catch (Exception ex) {
			System.out.println(ex.toString());
			System.exit(1);
		}
	}

	public void createTable() throws SQLException {
		String createSql = "create table TEST_LOGIN (username varchar(45) not null, "
				+ "password varchar(45) not null, primary key (username))";
		PreparedStatement preparedStatement = conn.prepareStatement(createSql);
		preparedStatement.execute();
	}

	public ResultSet query() throws SQLException {
		String querySql = "select * from TEST_LOGIN";
		PreparedStatement preStatement = conn.prepareStatement(querySql);
		ResultSet result = preStatement.executeQuery();
		if (result.next())
		{
			System.out.println("数据库查询成功");	
		} else {
			System.out.println("数据库查询不成功");
			System.exit(1);
		}

		return result;
	}

	public ResultSet insert(String userName, String password)
			throws SQLException {
		String insertSql = "insert into TEST_LOGIN (username, password) values (?, ?)";
		PreparedStatement preStatement = conn.prepareStatement(insertSql);
		preStatement.setString(1, userName);
		preStatement.setString(2, password);
		if (preStatement.executeUpdate() > 0) {
			System.out.println("数据库插入数据成功");
		} else {
			System.out.println("数据库插入数据不成功");
			System.exit(1);
		}
		return query();
	}

	public ResultSet updata(String userName, String password)
			throws SQLException {
		String updateSql = "update TEST_LOGIN set password = ? where username = ?";
		PreparedStatement preStatement = conn.prepareStatement(updateSql);
		preStatement.setString(1, password);
		preStatement.setString(2, userName);
		if (preStatement.executeUpdate() > 0) {
			System.out.println("数据库更新数据成功");
		} else {
			System.out.println("数据库更新数据不成功");
			System.exit(1);
		}
		return query();
	}

	public void connClose() throws SQLException {
		this.conn.close();
	}

	public boolean hasTable(String tableName) throws SQLException {
		boolean flag = false;
		DatabaseMetaData dmd = this.conn.getMetaData();
		ResultSet rs = dmd.getTables(null, null, tableName,
				new String[] { "TABLE" });
		if (rs.next()) {
			flag = true;
		}
		return flag;
	}

	public int checkout(String userName, String password) throws SQLException {
		int flag = 0;

		ResultSet result = query();
		while (result.next()) {
			if (result.getString(1).equals(userName)) {
				if (result.getString(2).equals(password)) {
					flag = 1;
					break;
				} else {
					flag = 2;
					break;
				}
			}
		}

		return flag;
	}

	public String displayTableData(ResultSet result, String encoding) {
		String str = "";
		try {
			str = "HTTP/1.1 200 OK\r\n"
					+ "Content-Type: text/html;charset="
					+ encoding
					+ "\r\n"
					+ "\r\n"
					+ "<html><title>数据库表信息</title><body><table><tr><td>用户名</td><td>密码</td></tr>";
			while (result.next()) {
				str = str + "<tr><td>" + result.getString(1) + "</td><td>"
						+ result.getString(2) + "</td></tr>";
			}
			str = str + "</h3></body></html>";
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
		return str;
	}

}
