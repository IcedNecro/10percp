package com.checkpoint.test;

import static org.junit.Assert.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.internal.runners.TestClass;

import com.checkpoint.mobilenetwork.core.MobileNetwork;
import com.checkpoint.mobilenetwork.core.MobileTower;
import com.checkpoint.mobilenetwork.core.PhoneCall;
import com.checkpoint.mobilenetwork.core.SMS;
import com.checkpoint.mobilenetwork.core.Subscriber;
import com.checkpoint.mobilenetwork.core.TransferMoneyNotification;
import com.checkpoint.mobilenetwork.core.packages.LowCostPackage;
import com.checkpoint.mobilenetwork.core.packages.Package;

public class TestPackageChange {

	private MobileNetwork network;
	private ArrayList<Subscriber> subscribers;
	
	@Before 
	public void init(){
		this.subscribers= new ArrayList<Subscriber>();
		this.network = new MobileNetwork("Kyivstar");
		FirstPack p1 = new FirstPack();
		SecondPack p2 = new SecondPack();
				
		this.network.addNewPackage(0, p1);
		
		this.network.setBasicPackageID(0);
		
		
		for(int i=0; i<5;i++) {
			Subscriber s = new Subscriber("097000000"+i);
			s.setOperator(network);
			if (i%2==0) {
				s.setPack(p2.clone());
			}
			
			this.subscribers.add(s);
		}
		
	}
	
	@Test
	public void test() throws InterruptedException {
		Thread.sleep(2000);
		this.network.addNewPackage(0, new LowCostPackage());
		Thread.sleep(1000);
		for(int i=0; i<5; i++){
			Subscriber s = subscribers.get(i);
			if(i%2==0)
				assertEquals(SecondPack.class,s.getPack().getPrototypeClass());
			else 
				assertEquals(LowCostPackage.class, s.getPack().getPrototypeClass());
		}		
	}
	
	@Test 
	public void testChangePackageRequest() throws InterruptedException{
		Thread.sleep(2000);
		Subscriber subs = this.subscribers.get(1);
		this.network.addNewPackage(2, new LowCostPackage());
		this.network.addNewPackage(1, new SecondPack());
		assertEquals(FirstPack.class, subs.getPack().getPrototypeClass());
		
		Thread.sleep(1000);
		subs.changePackage(1, Arrays.asList(new MobileTower(0,0,this.network,10)));
		Thread.sleep(1000);;
		assertEquals(SecondPack.class, subs.getPack().getPrototypeClass());
		
	}

	
	public class FirstPack extends Package {

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

	public class SecondPack extends Package {

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
