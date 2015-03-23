package com.checkpoint;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class TextWordCounter {

	public static int getWordEntriesCounter(String fileName, String word, int numOfThreads) throws InterruptedException {
		try {
			ArrayList<Runnable> arrList = new ArrayList<>();
			
			Scanner scan = new Scanner(new File(fileName));
			StringBuffer strBuf = new StringBuffer();
			while(scan.hasNext())
			{
				String str = scan.nextLine();
				strBuf.append(str);
			}
			List<String> words =Arrays.asList(strBuf.toString().split("[\\s\\W]+"));
			System.out.println(words.size() + " sz");
			
			int splitTextInterval = words.size()/numOfThreads;
			for(int i=0; i<numOfThreads; i++) {
				arrList.add(new InnerThread(words.subList(i*splitTextInterval,(i+1)*splitTextInterval), word));
			}
			Long begin = System.currentTimeMillis();
	
			for(int i=0; i<numOfThreads; i++) {
				((InnerThread)arrList.get(i)).getThread().join();
			}
			
			Integer answer = 0;
			
			for(int i=0; i<arrList.size(); i++)
				answer += ((InnerThread) arrList.get(i)).getCount();
			
			Long end = System.currentTimeMillis();
			
			System.out.println("finished with time delay "+(end-begin));
			scan.close();
			return answer;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return 1;
		} 
	}
	
	
	private static HashMap<String,Integer> map;
	
	public static HashMap<String, Integer> getWordEntriesCounter(String fileName, int numOfThreads) throws InterruptedException {
		try {
			ArrayList<Runnable> arrList = new ArrayList<>();
			
			map = new HashMap<String, Integer>();
	
			Scanner scan = new Scanner(new File(fileName));
			StringBuffer strBuf = new StringBuffer();
			
			while(scan.hasNext())
			{
				String str = scan.nextLine();
				strBuf.append(str);
			}
			List<String> words =Arrays.asList(strBuf.toString().split("[\\s\\W]+"));
			System.out.println(words.size() + " sz");
			
			int splitTextInterval = words.size()/numOfThreads;
			
			for(int i=0; i<numOfThreads; i++) {
				arrList.add(new InnerThread2(words.subList(i*splitTextInterval,(i+1)*splitTextInterval)));
			}
			
			
			Long begin = System.currentTimeMillis();
			
			for(int i=0; i<numOfThreads; i++) {
				((InnerThread2)arrList.get(i)).getThread().join();
			}
			
			Long end = System.currentTimeMillis();
			
			System.out.println("finished with time delay "+(end-begin));
				
			return map;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} 
	}
	
	private static class InnerThread implements Runnable {

		private Integer count = 0;
		
		private Thread t;
		private List<String> words;
		private String word;
		
		public InnerThread(List<String> words, String word) {
			this.t = new Thread(this);
			this.words = words;
			this.word = word;
			t.start();
		}
		
		@Override
		public void run() {
			for (int i=0; i<this.words.size(); i++) {
				if(this.words.get(i).toLowerCase().equals(this.word.toLowerCase())) {
					this.count++;
				}
			}
		}
		
		public Integer getCount() {
			return this.count;
		}
	
		public Thread getThread() {
			return this.t;
		}
	}
	
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
			for (int i=0; i<this.words.size(); i++) {
				String key = this.words.get(i).toLowerCase();
				synchronized(map){
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
}