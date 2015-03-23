package com.checkpoint.test;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import com.checkpoint.*;

import org.junit.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static com.checkpoint.NumberParser.*;

public class NumberParserTest {
	
	@Test
	public void testValidParser() throws FileNotFoundException {
		Scanner scan = new Scanner(new File("testfiles/valid"));

		System.out.println(getClass().getResource("/valid"));
		
		System.out.println("Valid");
		
		while(scan.hasNext()){
			
			String str = scan.nextLine();
		//	System.out.println(str);
			assertEquals(validNumber(str), true);
			
		}
	}
	
	@Test
	public void testInvalidParser() throws FileNotFoundException {
		Scanner scan = new Scanner(new File("testfiles/invalid"));

		System.out.println("InValid");
		
		while(scan.hasNext()){
			
			String str = scan.nextLine();
		//	System.out.println(str);
			assertEquals(validNumber(str), false);
			
		}
	}
}
