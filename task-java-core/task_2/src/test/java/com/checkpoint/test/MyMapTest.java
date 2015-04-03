package com.checkpoint.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.checkpoint.MyMap;

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
	public void keySet() {
		for(int i=0; i<map.size(); i++)
			assertEquals(true, map.keySet().contains("key"+i));
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
	}
	
	@Test
	public void temporalRemovement() throws InterruptedException {	
		
		MyMap<Integer, String> map = new MyMap<Integer,String>();
		Set<Integer> keys= new HashSet<Integer>();
		
		for(int i=6; i<15;i++)
			keys.add(new Integer(i));
				
		for(int i = 0; i<15; i++) {
			map.put(new Integer(i), "key"+i);
			
			Thread.sleep(1000);
		}		
		Thread.sleep(200);
		assertEquals(keys, map.keySet());
	}
}
