package com.checkpoint;

public class Shuffler {
	public static Object[] shuffle(Object arr[]) {
		int count = (int) (0.25*arr.length);
		Object[] initialArray = arr.clone();
		while (true) {
			int shufflePosition = (int) (arr.length*Math.random());
			int swapPosition = (int) (arr.length*Math.random());
			
			Object buf = arr[shufflePosition];
			arr[shufflePosition] = arr[swapPosition];
			arr[swapPosition] = buf;
			
			int shuffledElementsCount = getShuffledElementsCount(initialArray, arr);
			
			if(shuffledElementsCount>count) {
				try {
					Object[][] o = (Object[][])arr;
					o.clone();
					
					for(int i=0; i<((Object[][])arr).length; i++) {
						System.out.println("before");
						outputArray((Object[]) arr[i]);
						arr[i] = shuffle((Object[])arr[i]);
						System.out.println("after");
						outputArray((Object[]) arr[i]);
					}
					return arr;
				} catch(Exception e) {
					return arr;
				}
			}
		}
	}

	public static void anotherShuffle(Object[] objects) {
		int depth=1;
		Object[] arr = objects;
		do{
			try{
				arr = (Object[][]) arr;
			}catch(Exception e){
				break;
			}
			depth++;
		}while(true);
		System.out.println(depth + " depth");
	}
	
	public static void outputArray(Object[] o) {
		for (int i = 0; i < o.length; i++)
			System.out.print(o[i] + " ");
		System.out.println();
	}

	
	public static int getShuffledElementsCount(
			Object[] initialArray,
			Object[] currentArray) {
		int count = 0;
		
		for(int i=0; i<initialArray.length; i++)
			if(!initialArray[i].equals(currentArray[i]))
				count++;
		
		return count;
	}
}