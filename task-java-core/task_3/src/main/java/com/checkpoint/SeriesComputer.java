package com.checkpoint;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SeriesComputer {
	
	private ExecutorService service;
	private Integer n;
	private Class<? extends SeriesMember> memberClass;
	
	public SeriesComputer(int n, int nThreads, Class<? extends SeriesMember> memberClass) {
		this.service = Executors.newFixedThreadPool(nThreads);
		this.n = n;
		this.memberClass = memberClass;
	}
	
	public Double getResult() {
		
		Double result = 0.0d;
		try {
			
			final ArrayList<Future<Double>> results = new ArrayList<Future<Double>>();
			for(int i=1; i<=this.n; i++)
					results.add(this.service.submit(new CountMember(i,this.memberClass.newInstance())));
				
			Future<Double> totalResult = this.service.submit(new Callable<Double>() {
	
				@Override
				public Double call() throws Exception {
					Double result = 0.0d;
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
			Double result = this.member.computeMember(this.n);
			System.out.println(result);
			return result;
		}
	}
}
