package com.checkpoint;

import java.util.Random;

public class Shuffler {

	private static float MAX_SWAP = 0.25f;
	
	public static Object[] shuffle(Object arr[]) {
		try {
			Object[][] buffer = (Object[][]) arr;
			return (Object[]) doubleDimensional(buffer);
		}catch(Exception e) {
			return oneDimensional(arr);
		}
	}
	
	private static Object[][] doubleDimensional(Object[][] arr) {
		Object[][] buffer = arr.clone();
		for(int i = 0; i<arr.length; i++) {
			buffer[i] = arr[i].clone();
		}
		
		int countToSwap = (int) (arr.length*arr[0].length*MAX_SWAP);
		
		Random random = new Random();
		
		while(countToSwap>=checkForIdentity(arr, buffer)) {
			int swapFromX = random.nextInt(arr.length);
			int swapFromY = random.nextInt(arr.length);
			
			int swapToX = random.nextInt(arr.length);
			int swapToY = random.nextInt(arr.length);
			
			Object buf = arr[swapFromX][swapFromY];
			arr[swapFromX][swapFromY] = arr[swapToX][swapToY];
			arr[swapToX][swapToY] = buf;
		}
		
		return arr;
	}
	
	private static Object[] oneDimensional(Object[] arr) {
		int countToSwap = (int) (arr.length*MAX_SWAP);
		Object[] buffer = arr.clone();
		Random random = new Random();
		
		while(countToSwap>=checkForIdentity(arr, buffer)) {
			int swapFrom = random.nextInt(arr.length);
			int swapTo = random.nextInt(arr.length);
			
			Object buf = arr[swapFrom];
			arr[swapFrom] = arr[swapTo];
			arr[swapTo] = buf;
		}
			
		return arr;
	}
	
	public static Integer checkForIdentity(Object[] current, Object[] initial) {
		int numberOfNotEqual = 0;
		try
		{
			Object[][] buffer = (Object[][]) current;
			Object[][] initialBuffer = (Object[][]) initial;
			
			for(int i=0; i<buffer.length; i++)
				for(int j=0; j<buffer[i].length; j++)
					if(buffer[i][j]!=initialBuffer[i][j])
						numberOfNotEqual++;
		} catch(Exception e){
			for(int i=0; i<current.length; i++)
				if(current[i]!=initial[i])
					numberOfNotEqual++;
		}
		return numberOfNotEqual;
	}
}