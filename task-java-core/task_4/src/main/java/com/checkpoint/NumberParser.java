package com.checkpoint;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Task 4 Java Core
 * 
 * Validating phone number
 * 
 * @author roman
 *
 */

public class NumberParser {
	private static String pattern = "(((\\+?\\d)|(\\d))\\s)?(((\\(\\d{3,4}\\))|(\\d{3,4}))\\s)?(((\\d{2,3}\\s){2})|((\\d{2,3}-){2}))\\d{2,3}"; 
	
	/**
	 * Validates string as number
	 * 
	 * @param number - String that represents line to validate as number
	 * @return true if line matches, false if not
	 */
	public static boolean validNumber(String number) {
		if(number.matches(pattern))
			return true;
		else 
			return false;
	}
	
	/**
	 * Reads line from file and check it as phone number
	 * @param filename  
	 * @return true if line matches, false if not
	 */
	public static boolean validNumberFromFile(String filename) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			if(br.readLine().matches(pattern))
				return true;
			else 
				return false;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static void main(String args[]) {
		if (args.length==2){
			System.out.println("Is number in file valid?:\t"+validNumberFromFile(args[1]));
		} else {
			System.out.println("Is number \""+args[0]+"\" valid?:\t"+validNumber(args[0]));
		}
	}
}