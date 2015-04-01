package com.checkpoint.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.checkpoint.mobilenetwork.core.MobileNetwork;
import com.checkpoint.mobilenetwork.core.MobileTower;
import com.checkpoint.mobilenetwork.core.Subscriber;
import com.checkpoint.mobilenetwork.core.packages.LowCostPackage;
import com.checkpoint.mobilenetwork.core.packages.Package;

public class TestSystem {
	Package pack;
	MobileNetwork network;
	MobileTower tower;
	
	@Before 
	public void init() {
		this.pack = new LowCostPackage();
		this.network = new MobileNetwork("Kyivstar");
		this.tower = new MobileTower(0,0,this.network,10);
		network.addNewPackage(1,new LowCostPackage());
		network.setBasicPackageID(1);
	}
	
	@Test
	public void test() throws InterruptedException {
		System.out.println("Test 1: Successfull call");
		Subscriber subs_1 = new Subscriber("0970000001");
		Subscriber subs_2 = new Subscriber("0970000002");
		subs_1.setOperator(network);
		subs_2.setOperator(network);
		
		subs_1.chargeMoneyOnCount(20f);
		subs_2.chargeMoneyOnCount(10f);
		
		subs_1.performCallTo(subs_2, (long)4000,Arrays.asList(tower));
		
		Thread.sleep(5000);
		
		assertEquals(18.95f,subs_1.getMoneyOnCount(),0.0001);
	}
	
	
	@Test
	public void testBusy() throws InterruptedException {
		System.out.println("Test 2: test for busy");
		Subscriber subs_1 = new Subscriber("0970000001");
		Subscriber subs_2 = new Subscriber("0970000002");
		Subscriber subs_3 = new Subscriber("0970000003");
		List<MobileTower> towers = Arrays.asList(tower);
		
		subs_1.setOperator(network);
		subs_2.setOperator(network);
		subs_3.setOperator(network);
		
		subs_1.chargeMoneyOnCount(20f);
		subs_2.chargeMoneyOnCount(10f);
		subs_3.chargeMoneyOnCount(10f);
		
		subs_1.performCallTo(subs_2, (long) 4000, towers);
		Thread.sleep(1000);
		subs_3.performCallTo(subs_1, (long) 4000, towers);
	
		Thread.sleep(10000);
		
		assertEquals(18.95f,subs_1.getMoneyOnCount(),0.0001);
	}
	
	@Test
	public void testZeroOnCount() throws InterruptedException {
		System.out.println("Test 3: Test call with low funds");
		Subscriber subs_1 = new Subscriber("0970000001");
		Subscriber subs_2 = new Subscriber("0970000002");
		
		subs_1.setOperator(network);
		subs_2.setOperator(network);
		
		Thread.sleep(1000);
		subs_1.chargeMoneyOnCount(0.03f);
		subs_2.chargeMoneyOnCount(10f);
		
		subs_1.performCallTo(subs_2, (long) 4000, Arrays.asList(tower));
		
		Thread.sleep(5000);
		
		assertEquals(0.03f,subs_1.getMoneyOnCount(),0.001);
	}
	
	@Test
	public void testMultiNetwork() throws InterruptedException {
		System.out.println("Test 4: Test call between different networks");
		Subscriber subs_1 = new Subscriber("0970000001");
		Subscriber subs_2 = new Subscriber("0980000002");
		
		MobileNetwork network_2 = new MobileNetwork("Life");
		network_2.addNewPackage(1, new LowCostPackage());
		network_2.setBasicPackageID(1);
		
		this.tower.addNetwork(network_2);
		subs_1.setOperator(network);
		subs_2.setOperator(network_2);
		
		Thread.sleep(1000);
		subs_1.chargeMoneyOnCount(10f);
		subs_2.chargeMoneyOnCount(10f);
		
		subs_1.performCallTo(subs_2, (long) 4000, Arrays.asList(tower));
		
		Thread.sleep(5000);
		
		assertEquals(8.7f,subs_1.getMoneyOnCount(),0.001);
	}
	
}
