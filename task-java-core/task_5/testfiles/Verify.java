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

public class CodeCleaner {
	private static final String COMMENT_PATTERN = "(\\/\\/.*\\n)|(\\/\\*\\*?([\\s\\S](?!\\/\\*))*\\*\\/)";
	
	public static void clearFromComments(String openPath, String savePath) throws IOException {
		File f = new File(openPath);
		Scanner scan = new Scanner(f);
		
		StringBuffer strBuffer = new StringBuffer();
  	while(scan.hasNext()) {
			strBuffer.append(scan.nextLine()+"\n");
		}
		
		Pattern pattern = Pattern.compile(COMMENT_PATTERN);
		
		Matcher matcher = pattern.matcher(strBuffer.toString());
		
		String str = matcher.replaceAll("");
		
		File output = new File(savePath);
		System.out.println(str);
		
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
