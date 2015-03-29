package com.checkpoint.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.checkpoint.SeriesComputer;
import com.checkpoint.SpecialSeries;

public class TestExecutor {

	@Test
	public void test() {
		SeriesComputer computer = new SeriesComputer(100, 20, SpecialSeries.class);
		
		System.out.println(computer.getResult());
	}

}
