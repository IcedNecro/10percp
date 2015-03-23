package com.checkpoint.test;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

import com.checkpoint.*;

import org.junit.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static com.checkpoint.TextWordCounter.*;

public class TextWordCounterTest {
	private HashMap<String, Integer> results;
	
	@Before 
	public void init() throws InterruptedException {	
		this.results = TextWordCounter.getWordEntriesCounter("test/test.txt", 1000);
	}
	
	@Test 
	public void test() throws InterruptedException {
		
		assertEquals(new Integer(509), this.results.get("word"));
		assertEquals(new Integer(2567), this.results.get("come"));
		assertEquals(new Integer(1177), this.results.get("most"));
		assertEquals(new Integer(22409), this.results.get("i"));
		assertEquals(new Integer(5026), this.results.get("will"));
		assertEquals(new Integer(9271), this.results.get("is"));
		assertEquals(new Integer(128), this.results.get("romeo"));
		assertEquals(new Integer(1338), this.results.get("where"));
		
	}
}
