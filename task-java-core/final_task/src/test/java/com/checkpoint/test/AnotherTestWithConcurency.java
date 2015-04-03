package com.checkpoint.test;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import com.checkpoint.mobilenetwork.core.MobileNetwork;
import com.checkpoint.mobilenetwork.core.MobileTower;
import com.checkpoint.mobilenetwork.core.PhoneCall;
import com.checkpoint.mobilenetwork.core.SMS;
import com.checkpoint.mobilenetwork.core.Subscriber;
import com.checkpoint.mobilenetwork.core.packages.LowCostPackage;
import com.checkpoint.mobilenetwork.core.packages.Package;
import com.checkpoint.mobilenetwork.core.packages.Package.NotEnoughMoney;

public class AnotherTestWithConcurency {

	private MobileNetwork rnetwork;
	private MobileTower rtower;
	@Before 
	public void init() throws NotEnoughMoney {
		Package p = new LowCostPackage();
		rnetwork = new MobileNetwork("Kyivstar");

		rnetwork.addNewPackage(1, p);
		rnetwork.setBasicPackageID(1);
		rtower = new MobileTower(0, 0, rnetwork, 20);
		
	}
	
	@Test
	public void test() throws InterruptedException, NotEnoughMoney {
		System.out.println("Test 1: Concurrent");
		final ArrayList<Subscriber> subscribers = new ArrayList<Subscriber>();
		
		MobileTower rtower = new MobileTower(0, 0, rnetwork, 20);
		final MobileTower tower = spy(rtower);
		
		for(int i = 0; i<4; i++){
			Subscriber s = new Subscriber("097000000"+i);
			s.setOperator(rnetwork);
			s.chargeMoneyOnCount(10f);
			subscribers.add(s);
		}
		Thread t1 = new Thread(new Runnable(){

			@Override
			public void run() {
				subscribers.get(0).performCallTo(subscribers.get(1),(long)4000, Arrays.asList(tower));
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		});	
		
		Thread t2 = new Thread(new Runnable(){

			@Override
			public void run() {
				subscribers.get(2).performCallTo(subscribers.get(3), (long)4000,Arrays.asList(tower));
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}
			}
			
		});
		
		t1.start();
		t2.start();
		t1.join();
		t2.join();
		assertEquals(8.95f,subscribers.get(0).getMoneyOnCount(),0.001);
	}

	
	@Test
	public void testSingleThread() throws InterruptedException, NotEnoughMoney{
		final ArrayList<Subscriber> subscribers = new ArrayList<Subscriber>();
		System.out.println("Test 2: Single thread");
		final MobileTower tower = spy(rtower);
		
		for(int i = 0; i<4; i++){
			Subscriber s = new Subscriber("097000000"+i);
			s.setOperator(rnetwork);
			s.chargeMoneyOnCount(10f);
			subscribers.add(s);
		}
		subscribers.get(0).performCallTo(subscribers.get(1),(long)3000, Arrays.asList(tower));
		subscribers.get(3).performCallTo(subscribers.get(2), (long)4000,Arrays.asList(tower));
		Thread.sleep(10000);
		//does subscribers spent money for calls?
		assertEquals(9.2f,subscribers.get(0).getMoneyOnCount(),0.001);
		assertEquals(8.95f,subscribers.get(0).getMoneyOnCount(),0.001);
	}
	
	@Test
	public void testBusySingleThread1() throws InterruptedException, NotEnoughMoney{
		final ArrayList<Subscriber> subscribers = new ArrayList<Subscriber>();
		System.out.println("Test 3: Single Thread Busy");
		final MobileTower tower = spy(rtower);
		
		for(int i = 0; i<4; i++){
			Subscriber s = new Subscriber("097000000"+i);
			s.setOperator(rnetwork);
			s.chargeMoneyOnCount(10f);
			subscribers.add(s);
		}
		subscribers.get(0).performCallTo(subscribers.get(1),(long)4000, Arrays.asList(tower));
		subscribers.get(2).performCallTo(subscribers.get(1),(long)4000, Arrays.asList(tower));
		Thread.sleep(10000);
	}
	
	@Test
	public void testBusyConcurent() throws InterruptedException, NotEnoughMoney{
		final ArrayList<Subscriber> subscribers = new ArrayList<Subscriber>();
		
		ArrayList<Thread> threads = new ArrayList<Thread>();
		System.out.println("Test 4: Concurrent Busy");
		
		final MobileTower tower = spy(rtower);
		
		for(int i = 0; i<4; i++){
			Subscriber s = new Subscriber("097000000"+i);
			s.setOperator(rnetwork);
			s.chargeMoneyOnCount(10f);
			subscribers.add(s);
		}
		Thread t1 = new Thread(new Runnable(){

			@Override
			public void run() {
				subscribers.get(0).performCallTo(subscribers.get(2), (long)4000, Arrays.asList(tower));
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		});	
		
		Thread t2 = new Thread(new Runnable(){

			@Override
			public void run() {
				try {
					Thread.sleep(2000);
					subscribers.get(1).performCallTo(subscribers.get(2), (long)4000, Arrays.asList(tower));
				
					Thread.sleep(6000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		});
		
		t1.start();
		t2.start();
		t1.join();
		t2.join();
	}
	
}
