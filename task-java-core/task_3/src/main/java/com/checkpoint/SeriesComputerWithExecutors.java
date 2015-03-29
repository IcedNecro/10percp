package com.checkpoint;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * This series computer uses usual thread pool for computing sum of series
 * Each member computes into callable, and it's value adds to the general sum,
 * that returns by getResult() method
 * 
 * @author roman
 *
 */
public class SeriesComputerWithExecutors extends SeriesComputer {
	
	private ExecutorService service;
	
	public SeriesComputerWithExecutors(String filename, Class<? extends SeriesMember> memberClass){
		super(filename, memberClass);
		this.service = Executors.newFixedThreadPool(this.nOfThreads);

	}
	
	public SeriesComputerWithExecutors(int n, int nThreads, Class<? extends SeriesMember> memberClass) {
		super(n, nThreads, memberClass);

		this.service = Executors.newFixedThreadPool(this.nOfThreads);
	}
	
	@Override
	public Double getResult() {
		
		Double result = 0.0d;
		try {
			//Initializing an array of Future results
			final ArrayList<Future<Double>> results = new ArrayList<Future<Double>>();
			
			/*
			 * Loop, that perform an initialization of computation
			 * Each member computes into separate Callable, and future result persists
			 * in result list that will be executed later in the thread that computes the general sum
			 */
			for(int i=1; i<=this.n; i++)
				results.add(this.service.submit(new CountMember(i,this.memberClass.newInstance())));
			
			// initialization of summary thread
			Future<Double> totalResult = this.service.submit(new Callable<Double>() {
	
				@Override
				public Double call() throws Exception {
					Double result = 0.0d;
					// executing each member result from Future
					for(Future<Double> future : results)
						result += future.get();
					return result;
				}
			});
			result = totalResult.get();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	
		return result;
	}
	
	private class CountMember implements Callable<Double> {
		private Integer n;
		private SeriesMember member;
		
		public CountMember(Integer n, SeriesMember member){
			this.n = n;
			this.member = member;
		}

		@Override
		public Double call() throws Exception {
			//computes member's value
			Double result = this.member.computeMember(this.n);
			return result;
		}
	}
}
