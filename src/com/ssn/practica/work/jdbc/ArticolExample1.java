package com.ssn.practica.work.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class ArticolExample1 {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		Scanner scan = new Scanner(System.in);

		Connection con = null;
		Class.forName("oracle.jdbc.driver.OracleDriver");
		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "dbUser", "dbUser");
			if (con != null) {
				System.out.println("Success");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		int opt = -1;

		while (opt != 0) {

			System.out.println("\nMenu :");
			System.out.println("0. Exit");
			System.out.println("1. Add Articol");
			System.out.println("2. Add Magazin");
			System.out.println("3. Add Preturi");
			System.out.println("4. Low price");

			System.out.print("\nInsert option : ");
			opt = Integer.parseInt(scan.nextLine());

			switch (opt) {
			case 1: {
				add(con, "Articol");
				break;
			}

			case 2: {
				add(con, "Magazin");
				break;
			}

			case 3: {
				add(con, "Preturi");
				break;
			}
			case 4: {
				try {
					Statement s = con.createStatement();
					ResultSet rs = s.executeQuery(
							"select a.nume, min(p.pret) from articol a left join preturi p on a.id = p.article_id where p.pret is not null group by a.nume");
					while (rs.next()) {
						System.out.println(rs.getString(1) + " " + rs.getInt(2));
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}

				break;
			}

			default:
				scan.close();
				System.out.println("Bye.");
			}

		}
	}

	public static void add(Connection con, String table) {
		Scanner scan = new Scanner(System.in);
		if (table.equals("Preturi")) {
			System.out.print("Insert Articol_id : ");
			int id_a = Integer.parseInt(scan.nextLine());
			System.out.print("Insert Magazin_id : ");
			int id_m = Integer.parseInt(scan.nextLine());
			System.out.print("Insert pret : ");
			int pret = Integer.parseInt(scan.nextLine());

			try (PreparedStatement ps = con.prepareStatement("insert into " + table + " values (?, ?, ?)")) {
				ps.setInt(1, id_a);
				ps.setInt(2, id_m);
				ps.setInt(3, pret);
				int nr_randuri = ps.executeUpdate();
				System.out.println("\nNumar randuri afectate de adaugare=" + nr_randuri);
			} catch (SQLException e) {
				e.printStackTrace();
			}

		} else {

			System.out.print("Insert id : ");
			int id = Integer.parseInt(scan.nextLine());
			System.out.print("Insert nume : ");
			String nume = scan.nextLine().toString();

			try (PreparedStatement ps = con.prepareStatement("insert into " + table + " values (?, ?)")) {
				ps.setInt(1, id);
				ps.setString(2, nume);
				int nr_randuri = ps.executeUpdate();
				System.out.println("\nNumar randuri afectate de adaugare=" + nr_randuri);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

}
