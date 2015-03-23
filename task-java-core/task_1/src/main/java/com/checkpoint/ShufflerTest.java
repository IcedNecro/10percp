package com.checkpoint;

import static com.checkpoint.Shuffler.getShuffledElementsCount;
import static com.checkpoint.Shuffler.shuffle;
import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

public class ShufflerTest {

	@Test
	public void testOneDimentional() {

		Double arr[] = new Double[100];
		for(int i=0; i<arr.length; i++)
			arr[i] = new Double(Math.random());
		
		Double buf[] = arr.clone();
		arr = (Double[])shuffle(arr);
		outputArray(buf);
		outputArray(arr);
		System.out.println((float)getShuffledElementsCount(arr, buf)/arr.length);
		Assert.assertEquals(((float)getShuffledElementsCount(arr, buf)/arr.length)>0.25, true);
	}
	
	@Test
	public void testDoubleDimentional() {

		Double arr[][] = new Double[10][10];
		for(int i=0; i<arr.length; i++)
			for(int j=0; j<arr[0].length; j++)
				arr[i][j] = new Double(Math.random());
		
		
		Double buf[][] = arr.clone();
		for (int i = 0; i < arr.length; i++)
			buf[i] = arr[i].clone();
		
		arr = (Double[][])shuffle(arr);
		
		for(int i=0; i<arr.length; i++)
		{	
			Assert.assertEquals(((float)getShuffledElementsCount(arr[i], buf[i])/arr.length)>0.25, true);
		}
	}
	

	public static void outDoubleDimension(Object[][] arr) {
		System.out.println("array");
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[0].length; j++) {
				System.out.print(arr[i][j] + " ");
			}
			System.out.println();
		}
	}

	
	public static void outputArray(Object[] o){
		for(int i=0; i<o.length; i++)
			System.out.print(o[i]+" ");
		System.out.println();
	}

}
