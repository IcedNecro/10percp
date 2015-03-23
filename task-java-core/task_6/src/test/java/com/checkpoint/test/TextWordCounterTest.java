package com.checkpoint.test;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import com.checkpoint.*;

import org.junit.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static com.checkpoint.TextWordCounter.*;

public class TextWordCounterTest {
	@Test 
	public void test() throws InterruptedException {
		
		System.out.println(TextWordCounter.getWordEntriesCounter("test/test.txt", "for", 1000));
		System.out.println(TextWordCounter.getWordEntriesCounter("test/test.txt", 1).get("for"));
		
	//	assertEquals(TextWordCounter.getWordEntriesCounter("test/test.txt", 10000).get("Romeo"), 128);
	}
}
