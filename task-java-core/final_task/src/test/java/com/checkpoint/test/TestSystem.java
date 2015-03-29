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
		this.network = new MobileNetwork();
		this.tower = new MobileTower(0,0,this.network,10);
	}
	
	@Test
	public void test() throws InterruptedException {
		Subscriber subs_1 = new Subscriber("0970000001", this.pack);
		Subscriber subs_2 = new Subscriber("0970000002", this.pack);
		
		subs_1.chargeMoneyOnCount(20f);
		subs_2.chargeMoneyOnCount(10f);
		
		subs_1.performCallTo(subs_2, Arrays.asList(tower));
		
		Thread.sleep(3000);
		
		System.out.println(subs_1.getMoneyOnCount());
		System.out.println(subs_2.getMoneyOnCount());
	}
	
	@Test
	public void testZeroOnCount() throws InterruptedException {
		Subscriber subs_1 = new Subscriber("0970000001", this.pack);
		Subscriber subs_2 = new Subscriber("0970000002", this.pack);
		
		subs_1.chargeMoneyOnCount(0.03f);
		subs_2.chargeMoneyOnCount(10f);
		
		subs_1.performCallTo(subs_2, Arrays.asList(tower));
		
		Thread.sleep(3000);
		
		System.out.println(subs_1.getMoneyOnCount());
		System.out.println(subs_2.getMoneyOnCount());
	}
	
	@Test
	public void testBusy() throws InterruptedException {
		Subscriber subs_1 = new Subscriber("0970000001", this.pack);
		Subscriber subs_2 = new Subscriber("0970000002", this.pack);
		Subscriber subs_3 = new Subscriber("0970000003", this.pack);
		List<MobileTower> towers = Arrays.asList(tower);
		subs_1.chargeMoneyOnCount(20f);
		subs_2.chargeMoneyOnCount(10f);
		subs_3.chargeMoneyOnCount(10f);
		
		subs_1.performCallTo(subs_2, towers);
		subs_3.performCallTo(subs_1, towers);
	//	subs_3.performCallTo(subs_2, Arrays.asList(tower));
		
		Thread.sleep(10000);
		
		System.out.println(subs_1.getMoneyOnCount());
		System.out.println(subs_2.getMoneyOnCount());
	}
}
