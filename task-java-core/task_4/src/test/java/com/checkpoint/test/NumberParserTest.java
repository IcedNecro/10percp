package com.checkpoint.test;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.junit.*;

import static org.junit.Assert.*;
import static com.checkpoint.NumberParser.*;

public class NumberParserTest {
	
	@Test
	public void testValidParser() throws FileNotFoundException {
		Scanner scan = new Scanner(new File("testfiles/valid"));

		while(scan.hasNext()){
			
			String str = scan.nextLine();
			assertEquals(validNumber(str), true);			
		}
	}
	
	@Test
	public void testInvalidParser() throws FileNotFoundException {
		Scanner scan = new Scanner(new File("testfiles/invalid"));
		
		while(scan.hasNext()){
			
			String str = scan.nextLine();
			assertEquals(validNumber(str), false);
			
		}
	}
}
