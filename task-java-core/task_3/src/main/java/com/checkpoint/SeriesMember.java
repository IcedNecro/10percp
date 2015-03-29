package com.checkpoint;

/**
 * Represents Series as a formula of i-th member. Is used for computation in
 * parallel mode, and also can provide any series
 * 
 * @author roman
 *
 */
public abstract class SeriesMember {
	public SeriesMember() {}

	/**
	 * @param n number of member
	 * @return value of member
	 */
	public abstract Double computeMember(int n);
}
