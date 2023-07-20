package com.ssn.practica.work.App;

import java.util.List;

import com.ssn.practica.work.utils.DataManager;
import com.ssn.practica.work.utils.KeyManager;

public class Application {

	private DataManager dataManager = new DataManager();
	private KeyManager keyManager = new KeyManager();

	public static void main(String[] args) throws Exception {
		Application demo = new Application();
		demo.run();
	}

	private void run() throws Exception {
		menu();
	}

	private void menu() {

		int opt = -1;

		while (opt != 0) {

			System.out.println("\nMenu :");
			System.out.println("0. Exit");
			System.out.println("1. Add Article");
			System.out.println("2. Add Store");
			System.out.println("3. Add Price");
			System.out.println("4. Show Statistics");

			opt = keyManager.getInt("Insert option : ");

			switch (opt) {
			case 1: {
				String name = keyManager.getString("Name: ");
				dataManager.newArticle(name);
				break;
			}

			case 2: {
				String name = keyManager.getString("Name: ");
				dataManager.newStore(name);
				break;
			}

			case 3: {
				String articleName = keyManager.getString("name article : ");
				String storeName = keyManager.getString("name store : ");
				int price = keyManager.getInt("price : ");
				dataManager.newPrice(price, storeName, articleName);
				break;
			}
			case 4: {
				List<Price> a = dataManager.getLowCostArticles();
				for (Price price : a) {
					System.out.println(price);
				}
				break;
			}

			default:
				System.out.println("Bye.");
			}
		}

	}
}
