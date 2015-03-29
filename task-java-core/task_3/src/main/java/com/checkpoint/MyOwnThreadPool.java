package com.checkpoint;

import java.util.ArrayList;

/**
 * Computes sum of series using 'handmade' thread pool
 * @author roman
 *
 */
public class MyOwnThreadPool extends SeriesComputer {

	public MyOwnThreadPool(String filename, Class<? extends SeriesMember> memberClass){
		super(filename, memberClass);
	}
		
	public MyOwnThreadPool(int n,int nOfThreads,Class<? extends SeriesMember> classOfSeries) {
		super(n, nOfThreads, classOfSeries);
	}

	@Override
	public Double getResult() {
		
		int mod = this.n%this.nOfThreads;
		// topBorder % nOfThreads == 0
		int topBorder = this.n+(this.nOfThreads-mod);
		
		int membersPerThread = nOfThreads<=topBorder ? topBorder/this.nOfThreads: 1;
		int i = 0;
		
		ArrayList<ComputingThread> threads = new ArrayList<ComputingThread>();
		try {
			// each thread computes sum of series from (i-membersPerThread)-th member inclusive and
			// to i-th member exclusive. Each thread adds to list that will be executed in summary thread
			for(i = membersPerThread+1; i<=topBorder+1; i+=membersPerThread){
				ComputingThread t = new ComputingThread(i-membersPerThread, i, this.memberClass.newInstance());
				threads.add(t);
				t.start();
			}
			
			// Starting thread that computes total result
			ResultThread rThread = new ResultThread(threads);
			rThread.start();
			// wait before summary thread finish its computations
			rThread.join();
			return rThread.getResult();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	private class ComputingThread extends Thread {
		private int first, last;
		private Double result = 0.0d;
		private SeriesMember member;
		
		public ComputingThread(int firstMember, int lastMember, SeriesMember member) throws InstantiationException, IllegalAccessException {
			this.first = firstMember;
			this.last = lastMember;
			this.member = member;
		}
		
		@Override 
		public void run() {
			// computing sum of members from i=first while i<last exclusive and i is less than n 
			for(int i = first; i<last && i<=MyOwnThreadPool.this.n; i++) {
				result += this.member.computeMember(i);
			}
			
		}
		
		public Double getResult() {
			return this.result;
		}
	}
	
	private class ResultThread extends Thread {
		private ArrayList<ComputingThread> threads;
		private Double result=0.0d;
		public ResultThread(ArrayList<ComputingThread> threads) {
			this.threads = threads;
		}
		
		@Override 
		public void run() {
			try {
				// joins all threads before they finish their calculations
				for(Thread t : this.threads)
					t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for(ComputingThread t : this.threads)
				this.result += t.getResult();
			
		}
		/**
		 * returns result
		 */
		public Double getResult() {
			return this.result;
		}
	}
}
