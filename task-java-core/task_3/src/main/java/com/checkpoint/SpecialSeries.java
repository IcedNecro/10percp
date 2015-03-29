package com.checkpoint;

/**
 * An implementation of SeriesMember abstract class.
 * Member's formula: 2^(i-(-1)^i)
 * @author roman
 *
 */
public class SpecialSeries extends SeriesMember{
	
	@Override
	public Double computeMember(int n) {
		Double buffer = (double) (n%2==0?1:-1);
		return Math.pow(2, (n-buffer));
	}

}
