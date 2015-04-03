package com.checkpoint;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import static com.checkpoint.Shuffler.*;

public class ShufflerTest {

	static final Float MAX_SWAP = 0.25f;
	
	Integer oneDim[];
	Integer oneDimInitial[];
	Integer doubleDim[][];
	Integer doubleDimInitial[][];
	Random rand = new Random();
	
	@Before 
	public void init() {
		this.oneDim = new Integer[20];
		for(int i=0; i<this.oneDim.length; i++)
			this.oneDim[i] = rand.nextInt(1000);
		this.oneDimInitial = this.oneDim.clone();
		
		this.doubleDim = new Integer[10][10];
		for(int i=0; i<this.doubleDim.length; i++)
			for(int j=0; j<this.doubleDim[i].length; j++)
				
				this.doubleDim[i][j] = rand.nextInt(1000);
		
		this.doubleDimInitial = this.doubleDim.clone();
		for(int i=0; i<this.doubleDim.length;i++)
			this.doubleDimInitial[i] = this.doubleDim[i].clone();
	}
	
	@Test
	public void testOneDimShuffle() {
		System.out.println("Before");
		outputOneDimensional(oneDim);
		shuffle(this.oneDim);
		System.out.println("After");
		outputOneDimensional(oneDim);

		System.out.println("About "+((float)checkForIdentity(oneDim, oneDimInitial)/(this.oneDim.length))+" of array elements aren't equal to initialArray");
		assertEquals(true, MAX_SWAP<((float)checkForIdentity(oneDim, oneDimInitial)/(this.oneDim.length)));
	}
	
	@Test
	public void testDoubleDimShuffle() {
		System.out.println("Before");
		outputDoubleDimensional(doubleDim);
		shuffle(this.doubleDim);
		System.out.println("After");		
		outputDoubleDimensional(doubleDim);
		System.out.println("About "+((float)checkForIdentity(doubleDim, doubleDimInitial)/(this.doubleDim.length*this.doubleDim[0].length))+" of double dimensional array elements aren't equal to initialArray");
		assertEquals(true, MAX_SWAP<((float)checkForIdentity(doubleDim, doubleDimInitial)/(this.doubleDim.length*this.doubleDim[0].length)));
	}
	
	private static void outputDoubleDimensional(Object[][] arr) {
		for(int i = 0; i<arr.length; i++) {
			for(int j=0; j<arr[i].length; j++)
				System.out.print(arr[i][j]+"\t");
			System.out.println();
		}
		System.out.println();
	}
	private static void outputOneDimensional(Object[] arr) {
		for(int i = 0; i<arr.length; i++) 
			System.out.print(arr[i]+"\t");
		System.out.println();
	}
}
