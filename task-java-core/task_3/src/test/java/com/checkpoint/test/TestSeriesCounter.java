package com.checkpoint.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.checkpoint.MyOwnThreadPool;
import com.checkpoint.SeriesComputer;
import com.checkpoint.SeriesComputerWithExecutors;
import com.checkpoint.SpecialSeries;

/**
 * Task 3 - Java Core
 * 
 * This unit test checks if my classes for computing series sum work correct with 
 * different input params
 * 
 * @author roman
 *
 */
public class TestSeriesCounter{
	private final static Double TO_CHECK = 393214.0d;
	private ArrayList<SeriesComputerWithExecutors> executors = new ArrayList<SeriesComputerWithExecutors>();
	private ArrayList<MyOwnThreadPool> myOwn = new ArrayList<MyOwnThreadPool>();
	private SeriesComputer executor;
	private SeriesComputer myOwnPool;
	
	
	@Before
	public void init() {
		for(int i = 1; i<20; i++) {
			SeriesComputerWithExecutors computer = new SeriesComputerWithExecutors(17, i, SpecialSeries.class);
			this.executors.add(computer);
		}
		
		for(int i = 1; i<20; i++) {
			MyOwnThreadPool computer = new MyOwnThreadPool(17, i, SpecialSeries.class);
			this.myOwn.add(computer);
		}
		
		this.executor = new SeriesComputerWithExecutors("testfiles/test", SpecialSeries.class);
		this.myOwnPool = new MyOwnThreadPool("testfiles/test", SpecialSeries.class);

	}
	
	@Test
	public void testExecutors() {
		for(SeriesComputer computer: this.executors)
			assertEquals(TO_CHECK, computer.getResult());
	}
	
	@Test
	public void testMyOwn() {
		for(SeriesComputer computer: this.myOwn)
			assertEquals(TO_CHECK, computer.getResult());
	}
	
	@Test 
	public void testInputFromFile(){
		assertEquals(TO_CHECK, this.myOwnPool.getResult());
		assertEquals(TO_CHECK, this.executor.getResult());
	}
}
