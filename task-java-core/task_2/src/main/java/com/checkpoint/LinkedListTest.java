package com.checkpoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.checkpoint.test.MyLinkedList;


public class LinkedListTest {

	private MyLinkedList<Integer> list;
	private List<Integer> containsAllTrue;
	private List<Integer> containsAllFalse;
	private List<Integer> checkAddAll;
	
	@Before
	public void init() {

		this.list = new MyLinkedList<Integer>();
		for(int i = 0; i<10; i++) {
			this.list.add(new Integer(i));
		}
		
		this.containsAllTrue = Arrays.asList(new Integer(4),new Integer(0),new Integer(9));
		this.containsAllFalse = Arrays.asList(new Integer(-1),new Integer(2),new Integer(5));
		this.checkAddAll = Arrays.asList(new Integer(13),new Integer(12),new Integer(11));
	}
	
	
	
	@Test
	public void testAdd() {
		System.out.println(this.list+" ls");
		Assert.assertEquals(this.list.size(), 10);
	}
	
	
	@Test 
	public void testIndexualAdd(){
		this.list.add(0,new Integer(11));
		this.list.add(3,new Integer(12));
		this.list.add(5,new Integer(13));
		this.list.add(this.list.size()-1,new Integer(14));
		
		System.out.println(this.list);
		Assert.assertEquals(14,this.list.size());
	}
	
	@Test
	public void testGet() {
		Assert.assertEquals(new Integer(0),this.list.get(0));
		Assert.assertEquals(new Integer(4),this.list.get(4));
		Assert.assertEquals(new Integer(9),this.list.get(9));
	}
	
	@Test
	public void testSet(){
		this.list.set(0, new Integer(15));
		this.list.set(3, new Integer(16));
		this.list.set(this.list.size()-1, new Integer(17));
		System.out.println(this.list + " result");
		Assert.assertEquals(10,this.list.size());
	}
	
	@Test
	public void testClear(){
		this.list.clear();
		System.out.println(this.list + " result");
		Assert.assertEquals(0,this.list.size());
	}
	
	@Test
	public void testContains() {
		Assert.assertEquals(true,this.list.containsAll(this.containsAllTrue));
		Assert.assertEquals(false,this.list.containsAll(this.containsAllFalse));		
	}
	
	@Test
	public void testContainsSingle() {
		Assert.assertEquals(true,this.list.contains(new Integer(4)));
		Assert.assertEquals(false,this.list.contains(new Integer(11)));		
	}
	
	@Test
	public void testIndexOf() {
		Assert.assertEquals(4,this.list.indexOf(new Integer(4)));
		Assert.assertEquals(0,this.list.indexOf(new Integer(0)));
		Assert.assertEquals(9,this.list.indexOf(new Integer(9)));
		Assert.assertEquals(-1,this.list.indexOf(new Integer(11)));		
	}
	
	@Test
	public void testAddAll() {
		this.list.addAll(1,checkAddAll);
		System.out.println(this.list);
	}
	
	@Test
	public void testLastIndex() {
		this.list.add(6,new Integer(3));
		Assert.assertEquals(6, this.list.lastIndexOf(new Integer(3)));
		Assert.assertEquals(-1, this.list.lastIndexOf(new Integer(11)));
		Assert.assertEquals(1, this.list.lastIndexOf(new Integer(1)));
	}
	
	@Test 
	public void testRemove() {
		this.list.remove(new Integer(0));
		this.list.remove(new Integer(4));
		this.list.remove(new Integer(this.list.size()-1));
		System.out.println("after removal " + this.list);
	}
	
	@Test 
	public void testRemoveAll(){
		this.list.removeAll(this.containsAllTrue);
		System.out.println(this.list+" remove all");
	}
	
	@Test 
	public void testRetainAll(){
		this.list.retainAll(this.containsAllTrue);
		System.out.println(this.list+" remove all");
	}
}