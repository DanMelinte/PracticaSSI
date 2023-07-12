package com.ssn.practica.work.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCExample {
	public static void main(String[] args) throws ClassNotFoundException, SQLException {

		Class.forName("oracle.jdbc.driver.OracleDriver");

		try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "dbUser", "dbUser")) {
			if (conn != null) {
				System.out.println("Success");
			}

			Statement statement = conn.createStatement();
			ResultSet resultSet = statement.executeQuery("select * from student");

			while (resultSet.next()) {
				System.out.println(resultSet.getInt(1) + resultSet.getString(2) + resultSet.getString(3));
			}

			resultSet.close();
		}

	}
}
