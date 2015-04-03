package com.checkpoint;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Abstract class that apointed for computing sum of series
 * Its constructor receives Class<?> of SeriesMember, that you need to compute 
 * @author roman
 *
 */
public abstract class SeriesComputer {
	protected Integer n, nOfThreads;
	protected Class<? extends SeriesMember> memberClass;
	
	/**
	 * Constructor that reads input parameters from file
	 * 
	 * @param filename name of Input file
	 * @param memberClass class of series you need to compute
	 */
	public SeriesComputer(String filename, Class<? extends SeriesMember> memberClass) {
		FileReader fis;
		try {
			fis = new FileReader(new File(filename));
			BufferedReader br = new BufferedReader(fis);
			String[] line = br.readLine().split(" ");
			this.n = Integer.parseInt(line[0]);
			this.nOfThreads = Integer.parseInt(line[1]);
			this.memberClass = memberClass;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Takes 
	 * @param n - number of member to which you want to compute the sum of series
	 * @param countOfThreads - number of threads that performs computation
	 * @param memberClass - class of series
	 */
	protected SeriesComputer(Integer n, Integer countOfThreads, Class<? extends SeriesMember> memberClass) {
		this.n = n;
		this.memberClass = memberClass;
		this.nOfThreads = countOfThreads;
	}
	
	/**
	 * 
	 * @return computation result
	 */
	public abstract Double getResult();
}
