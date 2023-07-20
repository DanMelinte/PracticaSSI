package com.ssn.practica.work.utils;

import java.util.Scanner;

public class KeyManager {
	private Scanner scan = new Scanner(System.in);

	public int getInt(String message) {
		System.out.print(message);
		return Integer.parseInt(scan.nextLine());
	}

	public String getString(String message) {
		System.out.print(message);
		return scan.nextLine();
	}
}
