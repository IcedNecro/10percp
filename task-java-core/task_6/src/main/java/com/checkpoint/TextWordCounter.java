package com.checkpoint;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * Task 6 Java Core
 * 
 * TextWordCounter is class that counts all entries of each word in the file
 * 
 * @author roman
 *
 */
public class TextWordCounter {	
	
	/*
	 * Hashmap where the key is a word and a value is a count of entries that refers to this words
	 */
	private static HashMap<String,Integer> map;
	
	
	/**
	 * This methods launches a process of calculating. The prinicipe is simple - 
	 * we split whole text into words array, then create {@code numOfThreads} Threads,
	 * and each thread calculates words entries in the sub-array of general words array.
	 * Result of each thread's calculation merges into result HashMap
	 * IMPORTANT - All words are stored in lower case - it means that you couldn't get 
	 * a count of entries of word "Word", but you can get it with "word" key
	 * @param fileName - name of input file
	 * @param numOfThreads - number of threads that would be used
	 * @return hashmap with a words as keys and number of entries as values
	 * @throws InterruptedException
	 */
	public static HashMap<String, Integer> getWordEntriesCounter(String fileName, int numOfThreads) throws InterruptedException {
		try {
			ArrayList<Runnable> arrList = new ArrayList<>();
			
			map = new HashMap<String, Integer>();
			
			// initializing scanner for reading file
			Scanner scan = new Scanner(new File(fileName));
			StringBuffer strBuf = new StringBuffer();
			
			// file reading loop
			while(scan.hasNext())
			{
				String str = scan.nextLine();
				strBuf.append(str);
			}
			
			// Splitting whole text to a List of separate words
			List<String> words =Arrays.asList(strBuf.toString().split("[\\s\\W]+"));
			
			int splitTextInterval = (int)(words.size()/numOfThreads);
			
			int lastSplitIndex = 0;
			// splitting general array and creating of threads that process specific subarrays
			for(int i=1; i<numOfThreads; i++) {
				lastSplitIndex+=splitTextInterval;
				
				arrList.add(new InnerThread2(words.subList((i-1)*splitTextInterval,i*splitTextInterval)));
			}
			
			arrList.add(new InnerThread2(words.subList(lastSplitIndex,words.size())));
			
			//wait while all threads finish their calculations 
			for(int i=0; i<numOfThreads; i++) {
				((InnerThread2)arrList.get(i)).getThread().join();
			}
			
			Long end = System.currentTimeMillis();	
			
			return map;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} 
	}

	/**
	 * Runnable to perform calculations of word entries
	 * 
	 * @author roman
	 *
	 */
	private static class InnerThread2 implements Runnable {

		private Thread t;
		private List<String> words;
		
		public InnerThread2(List<String> words) {
			this.t = new Thread(this);
			this.words = words;
			t.start();
		}
		
		@Override
		public void run() {
			// loop through words subarray
			for (int i=0; i<this.words.size(); i++) {
				String key = this.words.get(i).toLowerCase();
				// synchronizing result map to avoid concurrency
				synchronized(map){
					// if map contains current word, just increment its entries count, else put
					// it into map and set entry count as 1
					if(map.containsKey(key))
						map.put(key, map.get(key)+1);
					else 
						map.put(key,1);
				}
			}
		}
		
		public HashMap<String, Integer> getAnswer() {
			return map;
		}
		
		public Thread getThread() {
			return this.t;
		}		
	}
	
	public static void main(String[] args) throws NumberFormatException, InterruptedException {
		System.out.println("Count of entries of word \""+args[2]+"\" :"+TextWordCounter.getWordEntriesCounter(args[0], Integer.parseInt(args[1])).get(args[2]));
		
	}
}