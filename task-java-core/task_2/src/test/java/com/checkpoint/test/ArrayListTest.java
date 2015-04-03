package com.checkpoint.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.checkpoint.MyArrayList;
import com.checkpoint.MyLinkedList;


public class ArrayListTest {

	private MyArrayList<Integer> list;
	private List<Integer> containsAllTrue;
	private List<Integer> containsAllFalse;
	private List<Integer> checkAddAll;
	
	@Before
	public void init() {

		this.list = new MyArrayList<Integer>();
		for(int i = 0; i<10; i++) {
			this.list.add(new Integer(i));
		}
		
		this.containsAllTrue = Arrays.asList(new Integer(0),new Integer(4),new Integer(9));
		this.containsAllFalse = Arrays.asList(new Integer(-1),new Integer(2),new Integer(5));
		this.checkAddAll = Arrays.asList(new Integer(13),new Integer(12),new Integer(11));
	}
	
	@Test
	public void temporalRemovement() throws InterruptedException {	
		
		MyArrayList<Integer> ls = new MyArrayList<Integer>();
		
		for(int i = 0; i<15; i++) {
			ls.add(i);
			Thread.sleep(1000);
		}
		
		MyArrayList<Integer> toCompare = new MyArrayList<Integer>();
		
		for(int i=6; i<15; i++)
			toCompare.add(new Integer(i));
		
		Thread.sleep(200);
		assertEquals(toCompare, ls);
	}
	
	@Test
	public void testAdd() {
		Assert.assertEquals(this.list.size(), 10);
	}
	
	
	@Test 
	public void testIndexualAdd(){
		this.list.add(0,new Integer(11));
		this.list.add(3,new Integer(12));
		this.list.add(5,new Integer(13));
		this.list.add(this.list.size(),new Integer(14));

		Assert.assertEquals(14,this.list.size());
		assertEquals(new Integer(11),this.list.get(0));
		assertEquals(new Integer(12),this.list.get(3));
		assertEquals(new Integer(13),this.list.get(5));

		assertEquals(new Integer(14),this.list.get(this.list.size()-1));
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
		Assert.assertEquals(new Integer(16),this.list.get(3));
		Assert.assertEquals(new Integer(15),this.list.get(0));
		Assert.assertEquals(new Integer(17),this.list.get(this.list.size()-1));

	}

	
	@Test
	public void testClear(){
		this.list.clear();
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
		for(int i=1, j=0; i<4; i++, j++)
			assertEquals(this.checkAddAll.get(j),this.list.get(i));
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
	}
	
	@Test 
	public void testRemoveIndex() {
		this.list.remove((int)0);
		this.list.remove((int)4);
		this.list.remove((int)(this.list.size()-1));
		
		List<Integer> l = Arrays.asList(new Integer(0),new Integer(5),new Integer(9));
		
		for(int i=0; i<this.list.size(); i++) {
			assertEquals(false,l.contains(this.list.get(i)));
		}
	}
	
	
	@Test 
	public void testRemoveAll(){
		this.list.removeAll(this.containsAllTrue);
		for(int i =0; i<this.list.size(); i++)
			assertEquals(false,this.containsAllTrue.contains(this.list.get(i)));
	}
	
	@Test 
	public void testRetainAll(){
		this.list.retainAll(this.containsAllTrue);
		for(int i=0; i<this.containsAllTrue.size();i++)
			assertEquals(this.containsAllTrue.get(i),this.list.get(i));
	}
	
	@Test
	public void testToArray() {
		MyLinkedList<Integer> arr = new MyLinkedList<Integer>();
		for(int i=0; i<5; i++){
			arr.add(i);
		}

		Object o[] = arr.toArray();
		for(int i=0; i<5; i++){
			Assert.assertEquals(i, o[i]);
		}
	}
	
	@Test 
	public void testEquals(){
		MyArrayList<Integer> arr = new MyArrayList<Integer>();

		for(int i=0; i<10; i++){
			arr.add(i);
		}
		
		assertEquals(arr, this.list);
		arr.remove(new Integer(1));
		assertEquals(false,arr.equals(this.list));
	}
}