package com.ssn.practica.work.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/*TODO
 * se considera o baza de date in care avem:
articol (id, nume)
magazin (id, nume)
o tabela cu preturile articolelor la magazine diferite (article_id, magazin_id, pret)
1. creati tabelele corespunzatoare in SQL developer
2. inserati date de test in cele 3 tabele din Java
3. faceti un query unde se poate vedea pentru fiecare articol la ce magazin are pretul cel mai mic si afisati rezultatele din Java
 */

public class ArticolExample {
	private Scanner scan = new Scanner(System.in);
	private Connection con;

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		ArticolExample articolExample = new ArticolExample();
		articolExample.startApplication();
	}

	private void startApplication() throws ClassNotFoundException {
		con = connect();

		int opt = -1;

		while (opt != 0) {

			showMenu();

			opt = getInt("Insert option :");

			switch (opt) {
			case 1: {
				int id = getInt("Insert id: ");
				String nume = getString("Nume: ");
				add("Articol", new Object[] { id, nume });
				break;
			}

			case 2: {
				int id = getInt("Insert id: ");
				String nume = getString("Nume: ");
				add("Magazin", new Object[] { id, nume });
				break;
			}

			case 3: {
				int articleId = getInt("Insert Articol_id : ");
				int magazinId = getInt("Insert Magazin_id : ");
				int pret = getInt("Insert pret : ");
				add("Preturi", new Object[] { articleId, magazinId, pret });
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
				System.out.println("Bye.");
			}

		}
	}

	private void showMenu() {
		System.out.println("\nMenu :");
		System.out.println("0. Exit");
		System.out.println("1. Add Articol");
		System.out.println("2. Add Magazin");
		System.out.println("3. Add Preturi");
		System.out.println("4. Low price");
	}

	private Connection connect() throws ClassNotFoundException {
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
		return con;
	}

	public void add(String table, Object[] params) {

		try (PreparedStatement ps = con
				.prepareStatement("insert into " + table + " values (" + generatePlaceHolders(params) + ")")) {

			for (int i = 0; i < params.length; i++) {
				if (params[i] instanceof Integer) {
					ps.setInt(i + 1, (Integer) params[i]);
				} else if (params[i] instanceof String) {
					ps.setString(i + 1, (String) params[i]);
				}
			}

			int nrRanduri = ps.executeUpdate();
			System.out.println("Numar randuri afectate de adaugare=" + nrRanduri);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private String generatePlaceHolders(Object[] params) {
		String s = "?";
		for (int i = 1; i < params.length; i++) {
			s += ",?";
		}
		return s;
	}

	private int getInt(String message) {
		System.out.print(message);
		return Integer.parseInt(scan.nextLine());
	}

	private String getString(String message) {
		System.out.print(message);
		return scan.nextLine();
	}

}
