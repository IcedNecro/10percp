package com.checkpoint.mobilenetwork.core.packages;

import static org.junit.Assert.*;

import javax.crypto.interfaces.PBEKey;

import org.junit.Test;

public class TestPackagePrototype {

	@Test
	public void test() {
		Package p = new LowCostPackage();
		
		Package pa = p.clone();
		Package pb = p.clone();
		
		Class<?> p2 = pa.getClass();
		Class<?> p1 = p.getClass();
		Class<?> p3 = pb.getClass();
		
		System.out.println(p2.equals(p1));
		System.out.println(p3.equals(p2));
	}

}
