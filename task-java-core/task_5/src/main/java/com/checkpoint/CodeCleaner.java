package com.checkpoint;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Task 5 Java Core
 * 
 * This class parses a java source file and removes any java-style comments from it 
 * 
 * @author roman
 *
 */
public class CodeCleaner {
	/*
	 * Regular Expression Pattern, that is used to identify java-style comments in the input file
	 * It has two parts:
	 * 1. (\\/\\/.*\\n) - this finds all single line comment
	 * 2. (\\/\\*\\*?([\\s\\S](?!\\/\\*))*\\*\\/) - identifies multiline comments (like this one you are reading)
	 *  and javadoс documentation 
	 */
	private static final String COMMENT_PATTERN = "(\\/\\/.*\\n)|(\\/\\*\\*?([\\s\\S](?!\\/\\*))*\\*\\/)";
	
	
	/**
	 * Static method, that receives 2 String parameters - input file name and output file name
	 * This method looks and removes all java-style comments in the input file and writes a re-
	 * sult to output file 
	 * @param openPath - path to the input file
	 * @param savePath - path to the output file
	 * @throws IOException
	 */
	public static void clearFromComments(String openPath, String savePath) throws IOException {
		File f = new File(openPath);
		//Creating scanner for reading from file
		Scanner scan = new Scanner(f);
		
		StringBuffer strBuffer = new StringBuffer();

		//file reading loop
		while(scan.hasNext()) {
			strBuffer.append(scan.nextLine()+"\n");
		}
		
		//Compiling pattern
		Pattern pattern = Pattern.compile(COMMENT_PATTERN);
		
		//receiving matcher to input data
		Matcher matcher = pattern.matcher(strBuffer.toString());
		
		//removing comments
		String str = matcher.replaceAll("");
		
		File output = new File(savePath);
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(output));
		writer.write(str);
		writer.close();
	}
	
	public static void main(String[] args) throws IOException {
		try {
			clearFromComments(args[0], args[1]);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
