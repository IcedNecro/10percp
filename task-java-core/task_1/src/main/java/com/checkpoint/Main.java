package com.checkpoint;

import static com.checkpoint.Shuffler.getShuffledElementsCount;
import static com.checkpoint.Shuffler.*;

public class Main {
	public static void main(String args[]) {
		//System.out.println("Hello");

		Double arr[][] = new Double[10][10];
		for (int i = 0; i < arr.length; i++)
			for (int j = 0; j < arr[0].length; j++)
				arr[i][j] = new Double(Math.random());

/*		Double buf[][] = arr.clone();
		for (int i = 0; i < arr.length; i++)
			buf[i] = arr[i].clone();-*/
		anotherShuffle((Object[])arr);

		//outDoubleDimension(arr);
		//outDoubleDimension(buf);
		/*System.out.println((float) getShuffledElementsCount(arr, buf)
				/ arr.length);*/
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
}