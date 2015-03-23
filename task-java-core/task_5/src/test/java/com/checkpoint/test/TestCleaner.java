package com.checkpoint.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

import com.checkpoint.CodeCleaner;

public class TestCleaner {

	private String inputFilePath = "testfiles/CodeCleaner.java";
	private String outputFilePath = "testfiles/TestClass.java";
	private String textToVerify;
	
	@Before 
	public void init() throws FileNotFoundException {
		Scanner scan = new Scanner(new File("testfiles/Verify.java"));
		
		StringBuffer buffer = new StringBuffer();
		
		while(scan.hasNext())
			buffer.append(scan.next());
		this.textToVerify = buffer.toString();
	}
	
	@Test
	public void test() throws IOException {
		CodeCleaner.clearFromComments(this.inputFilePath, this.outputFilePath);
		
		Scanner scan = new Scanner(new File(this.outputFilePath));
		
		StringBuffer toCompare = new StringBuffer();
		
		while(scan.hasNext()) 
			toCompare.append(scan.next());
		
		assertEquals(this.textToVerify, toCompare.toString());
	}

}
