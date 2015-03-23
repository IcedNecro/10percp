package com.checkpoint;

import static org.junit.Assert.*;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.checkpoint.test.MyMap;

public class MyMapTest {
	private MyMap<String, Integer> emptyMap;
	private MyMap<String, Integer> map;
	private Set<Entry<String, Integer>> entries;
	
	
	@Before
	public void before() {
		this.map = new MyMap<String, Integer>();
		this.emptyMap = new MyMap<String, Integer>();
		
		for(int i=0; i<15; i++) {
			this.map.put("key"+i, i);
		}
		
		this.entries = map.entrySet();
	}
	
	@Test
	public void testPut() {
		assertEquals(new Integer(1), this.map.get("key1"));
		assertEquals(new Integer(2), this.map.get("key2"));
		assertEquals(new Integer(3), this.map.get("key3"));
	}
	
	
	@Test
	public void testAlloc() {
		
		assertEquals(new Integer(5), this.map.get("key5"));
		assertEquals(new Integer(14), this.map.get("key14"));

	}
	
	@Test
	public void showEntries() {
		System.out.println(this.entries);
	}

	@Test
	public void testEmpty(){
		assertEquals(true, this.emptyMap.isEmpty());
		assertEquals(false, this.map.isEmpty());
	}
	
	@Test
	public void testSize() {
		assertEquals(15, this.map.size());
		assertEquals(0, this.emptyMap.size());
	}
	
	@Test 
	public void testRemove() {
		this.map.remove("key10");
		
		assertEquals(null, this.map.get("key10"));
		System.out.println(this.map.entrySet()+" set");
	}
	
	@Test
	public void temporalRemovement() throws InterruptedException {
	//	MyMap<String, Integer> map = new MyMap<String, Integer>();
		
	//	MyMap map = mock(MyMap.class);
		
		
		for(int i = 0; i<2000; i++) {
			map.put("key"+i, new Integer(i));
			//System.out.println(map.keySet());
			if(i%3==0) map.remove("key"+1);
			if(i%4==0) map.size();
			if(i%5==0) map.values();
			if(i%6==0) map.entrySet();
			
			Thread.sleep(10);
		}
		
		
		System.out.println("key "+map.keySet());
		
	}
}
