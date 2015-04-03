package com.checkpoint.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.checkpoint.mobilenetwork.core.MobileNetwork;
import com.checkpoint.mobilenetwork.core.MobileTower;
import com.checkpoint.mobilenetwork.core.PhoneCall;
import com.checkpoint.mobilenetwork.core.SMS;
import com.checkpoint.mobilenetwork.core.Subscriber;
import com.checkpoint.mobilenetwork.core.TransferMoneyNotification;
import com.checkpoint.mobilenetwork.core.packages.Package;

public class TestMultiTowers {

	private MobileTower tower_1;
	private MobileTower tower_2;
	private MobileTower tower_3;

	private MobileNetwork network_2;
	private MobileNetwork network;
	private TestPackage pack;
	
	@Before
	public void init() {
		this.network = new MobileNetwork("kyivstar");
		this.network_2 = new MobileNetwork("Life");
		this.pack = new TestPackage();
		this.network.addNewPackage(1,this.pack);
		this.network.setBasicPackageID(1);
		
		this.network_2.addNewPackage(1, this.pack);
		this.network_2.setBasicPackageID(1);
		
		this.tower_1 = new MobileTower(-25, -25, this.network, 6);
		this.tower_2 = new MobileTower(500, 500, this.network, 7);
		this.tower_3 = new MobileTower(1000, 1200, this.network, 6);
		
	}
	
	@Test
	public void testInRange() throws InterruptedException {
		
		System.out.println("Test 1 - Both subscribers belongs to same network and are in range of towers");
		final Subscriber s_1 = new Subscriber("0970000001", -500, -500);
		final Subscriber s_2 = new Subscriber("0970000002", 1000, 1000);
		
		final List<MobileTower> towers = Arrays.asList(tower_1, tower_2);
		s_1.setOperator(this.network);
		s_2.setOperator(this.network);
		
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				s_1.performCallTo(s_2, (long)4000,towers);
				s_1.sendSMS(s_2, "Test Mesage", towers);
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		
		t.start();
		t.join();
		
	}
	
	@Test
	public void testOutOfRange() throws InterruptedException {
		System.out.println("Test 2 - one of subscribers is out of range");
		final Subscriber s_1 = new Subscriber("0970000001", -500, -500);
		final Subscriber s_2 = new Subscriber("0970000002", 1500, 1500);
	
		final List<MobileTower> towers = Arrays.asList(tower_1, tower_2);
		s_1.setOperator(this.network);
		s_2.setOperator(this.network);
		
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				s_1.performCallTo(s_2, (long)4000,towers);
				s_1.sendSMS(s_2, "Test Mesage", towers);
				
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		t.start();
		t.join();
		
	}
	
	@Test
	public void testSharedTower() throws InterruptedException {
		System.out.println("Test 3 - Testing shared towers: two subscribers belong to different operators");
		MobileNetwork network_2 = new MobileNetwork("Life");
		
		network_2.addNewPackage(1,new TestPackage());
		network_2.setBasicPackageID(1);
		
		final Subscriber s_1 = new Subscriber("0970000001", -500, -500);
		final Subscriber s_2 = new Subscriber("0970000002", 1000, 1000);
		
		final List<MobileTower> towers = Arrays.asList(tower_1, tower_2);
		tower_1.addNetwork(network_2);
		tower_2.addNetwork(network_2);
		
		s_1.setOperator(this.network);
		s_2.setOperator(network_2);
		
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				s_1.performCallTo(s_2, (long) 4000, towers);
				s_1.sendSMS(s_2, "Test Mesage", towers);
				
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				
			}
		});
		
		t.start();
		t.join();
		
	}
	
	

	@Test
	public void testUnSharedTower() throws InterruptedException {
		
		System.out.println("Test 4 - Testing unshared towers : Towers don't covers one of subscriber");
		MobileNetwork network_2 = new MobileNetwork("Life");
		
		network_2.addNewPackage(1,new TestPackage());
		network_2.setBasicPackageID(1);
		
		final Subscriber s_1 = new Subscriber("0970000001", -500, -500);
		final Subscriber s_2 = new Subscriber("0970000002", 1000, 1000);
		
		final List<MobileTower> towers = Arrays.asList(tower_1, tower_2);
		tower_2.addNetwork(network_2);

		
		s_1.setOperator(this.network);
		s_2.setOperator(network_2);
		
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				s_1.performCallTo(s_2, (long)4000, towers);
				s_1.sendSMS(s_2, "Test Mesage", towers);
				
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				
			}
		});
		
		t.start();
		t.join();		
	}
	
	@Test
	public void testBusyStatus() throws InterruptedException {
		
		System.out.println("Test 5: test on Busy");
		final Subscriber s_1 = new Subscriber("0970000001", -500, -500);
		final Subscriber s_2 = new Subscriber("0970000002", 1000, 1000);
		final Subscriber s_3 = new Subscriber("0970000003", 900, 900);
		
		s_1.setOperator(network);
		s_2.setOperator(network);
		s_3.setOperator(network);
		
		final List<MobileTower> towers = Arrays.asList(tower_1, tower_2);
		
		s_1.performCallTo(s_2,(long)4000, towers);
		Thread.sleep(1000);
		s_3.performCallTo(s_2,(long)4000, towers);
		Thread.sleep(5000);
		s_2.performCallTo(s_1,(long)4000, towers);
		Thread.sleep(1000);
		s_3.performCallTo(s_2, (long)4000,towers);
		Thread.sleep(9000);
	}

	@Test
	public void testFewCalls() throws InterruptedException {
		System.out.println("Test 6: test three towers and 4 consumers");
		ArrayList<Subscriber> subscribers = new ArrayList<Subscriber>();

		this.tower_3.addNetwork(network_2);
		this.tower_2.addNetwork(network_2);
		
		final Subscriber s_1 = new Subscriber("0970000001",0,0);
		final Subscriber s_2 = new Subscriber("0970000002",-500,-500);
		final Subscriber s_3 = new Subscriber("0970000003",1500,1500);
		final Subscriber s_4 = new Subscriber("0970000004",900,900);
		
		final List<MobileTower> towers = Arrays.asList(tower_1, tower_2, tower_3);
		
		s_1.setOperator(network);
		s_2.setOperator(network);
		s_3.setOperator(network_2);
		s_4.setOperator(network_2);

		Thread t1 =new Thread() {
			public void run() {
				try {				
					s_2.performCallTo(s_3, (long)3000, towers);

					Thread.sleep(4000);
					s_2.performCallTo(s_1, (long)4000, towers);
					Thread.sleep(4000);
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			
			};
		};
		
		Thread t2 = new Thread() {
			public void run() {
				try {	
					Thread.sleep(1000);
					s_1.performCallTo(s_3, (long)3000, towers);
				
					Thread.sleep(4000);
					s_1.performCallTo(s_4, (long)3000, towers);
					Thread.sleep(6000);
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				s_2.performCallTo(s_1, (long)4000, towers);
			};
		};
		t1.start();
		t2.start();
		t1.join();
		t2.join();
	}

	
	public class TestPackage extends Package {

		@Override
		protected boolean perform(PhoneCall call) throws NotEnoughMoney {
			return true;
		}

		@Override
		protected void chargedWithMoney(Subscriber subscriber, float ammount) {
			
		}

		@Override
		protected boolean performSMS(SMS sms) {
			return true;
		}

		@Override
		protected boolean performMoneyTransfer(
				TransferMoneyNotification notification)
				throws MobileNetworkNotSupported {
			return true;
		}		
	}
	
}
