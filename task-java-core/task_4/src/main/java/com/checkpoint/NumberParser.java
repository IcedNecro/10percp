package com.checkpoint;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class NumberParser {
	private static String pattern = "(((\\+?\\d)|(\\d))\\s)?(((\\(\\d{3,4}\\))|(\\d{3,4}))\\s)?(((\\d{2,3}\\s){2})|((\\d{2,3}-){2}))\\d{2,3}"; 
	
	public static boolean validNumber(String number) {
		System.out.println("lol");
		if(number.matches(pattern))
			return true;
		else 
			return false;
	}
	
	public static boolean validNumberFromFile(String filename) {
		try {
			Scanner scan = new Scanner(new File("filename"));
			if(scan.nextLine().matches(pattern))
				return true;
			else 
				return false;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}