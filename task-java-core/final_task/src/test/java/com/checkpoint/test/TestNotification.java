package com.checkpoint.test;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.*;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.checkpoint.mobilenetwork.core.MobileNetwork;
import com.checkpoint.mobilenetwork.core.MobileTower;
import com.checkpoint.mobilenetwork.core.Notification;
import com.checkpoint.mobilenetwork.core.TransferMoneyNotification;
import com.checkpoint.mobilenetwork.core.SMS;
import com.checkpoint.mobilenetwork.core.Subscriber;
import com.checkpoint.mobilenetwork.core.packages.LowCostPackage;
import com.checkpoint.mobilenetwork.core.packages.Package.MobileNetworkNotSupported;
import com.checkpoint.mobilenetwork.core.packages.Package;
import java.util.List;

import static org.mockito.Mockito.*;

public class TestNotification {
	
	@Test
	public void test() throws InterruptedException {
		System.out.println("Test 1: mock test");
		
		MobileTower tower = mock(MobileTower.class);
		final Subscriber real = new Subscriber("09700000001");
		final Subscriber subs_2 = new Subscriber("09700000002");

		final Subscriber subs_1 = spy(real);
		doAnswer(new Answer<Object>() {

			@Override
			public Object answer(InvocationOnMock invocation)
					throws Throwable {
				subs_1.receiveNotification(new SMS(subs_1, subs_2, "LOL"));
				subs_2.receiveNotification(new SMS(subs_2, subs_1, "LOL2"));
				return null;
			}
			
		}).when(subs_1).sendSMS(any(Subscriber.class), anyString(), any(List.class));
		when(tower.isInRange(any(Subscriber.class))).thenReturn(true);
		
		subs_1.sendSMS(subs_2, "testMessage", Arrays.asList(tower));
		Thread.sleep(1000);
	}
	
	@Test
	public void testRealNotification() throws InterruptedException {
		System.out.println("Test 2: successfull sms");
		MobileNetwork rnetwork = new MobileNetwork("Kyivstar");
		MobileTower rtower = new MobileTower(0,0,rnetwork,10);
		MobileTower tower = spy(rtower);
		Package lcp = new LowCostPackage();
		rnetwork.addNewPackage(1, lcp);
		rnetwork.setBasicPackageID(1);
		
		final Subscriber subs_1 = new Subscriber("09700000001");
		final Subscriber subs_2 = new Subscriber("09700000002");

		subs_1.setOperator(rnetwork);
		subs_2.setOperator(rnetwork);
		
		subs_1.chargeMoneyOnCount(10f);
		
		subs_1.sendSMS(subs_2, "Test message", Arrays.asList(tower));
		Thread.sleep(1000);
	}
	
	@Test 
	public void testSMSOutOfRange() throws InterruptedException {
		System.out.println("Test 3: out of range");
		MobileNetwork rnetwork = new MobileNetwork("Kyivstar");
		MobileTower rtower = new MobileTower(0,0,rnetwork,10);
		MobileTower tower = spy(rtower);
		Package pack = mock(Package.class);
		
		rnetwork.addNewPackage(1, pack);
		rnetwork.setBasicPackageID(1);
		
		Subscriber subs_1 = new Subscriber("09700000001");
		Subscriber subs_2 = new Subscriber("09700000002");

		
		subs_1.setOperator(rnetwork);
		subs_2.setOperator(rnetwork);
		
		subs_2.setX(1000);
		subs_2.setY(1000);
		when(pack.performSMSNotification(any(SMS.class))).thenReturn(true);
		
		when(tower.isInRange(subs_1)).thenReturn(true);
		subs_1.sendSMS(subs_2, "Test message", Arrays.asList(tower));		
		Thread.sleep(1000);

	}
	
	@Test 
	public void testSMSNotDelivered() throws InterruptedException {
		System.out.println("Test 3: Test send sms without money");
		MobileNetwork rnetwork = new MobileNetwork("Kyivstar");
		MobileTower rtower = new MobileTower(0,0,rnetwork,10);
		MobileTower tower = spy(rtower);
		LowCostPackage lcp = new LowCostPackage();
		rnetwork.addNewPackage(1, lcp);
		rnetwork.setBasicPackageID(1);
		
		Subscriber subs_1 = new Subscriber("09700000001");
		Subscriber subs_2 = new Subscriber("09700000002");
		
		subs_2.setX(100);
		subs_2.setY(100);
		
		subs_1.setOperator(rnetwork);
		subs_2.setOperator(rnetwork);
	
		when(tower.isInRange(subs_1)).thenReturn(true);
		
		subs_1.sendSMS(subs_2, "Test message", Arrays.asList(tower));		
		Thread.sleep(1000);

	}

	@Test 
	public void testSMSToAnotherNetwork() throws InterruptedException {
		System.out.println("Test 8: Test send sms to another Network");
		MobileNetwork rnetwork = new MobileNetwork("Kyivstar");
		MobileNetwork rnetwork_2 = new MobileNetwork("Life");
		
		
		MobileTower rtower = new MobileTower(0,0,rnetwork,10);
		MobileTower tower = spy(rtower);
		Package pack = new LowCostPackage();
		rnetwork.addNewPackage(1, pack);
		rnetwork.setBasicPackageID(1);

		rnetwork_2.addNewPackage(1, pack);
		rnetwork_2.setBasicPackageID(1);
		
		tower.addNetwork(rnetwork_2);
		
		Subscriber subs_1 = new Subscriber("09700000001");
		Subscriber subs_2 = new Subscriber("09700000002");

		Subscriber spy = spy(subs_1);
		
		subs_2.setX(100);
		subs_2.setY(100);
		
		subs_1.setOperator(rnetwork);
		subs_2.setOperator(rnetwork_2);
			
		subs_1.chargeMoneyOnCount(10f);
		subs_2.chargeMoneyOnCount(10f);
		
		subs_1.sendSMS(subs_2, "Test message", Arrays.asList(tower));		
		Thread.sleep(1000);
		assertEquals(9.8f, subs_1.getMoneyOnCount(), 0.001f);
	}
	
	@Test
	public void testOnRealConditions() throws InterruptedException{
		System.out.println("Test 4: Real conditions");
		MobileNetwork rnetwork = new MobileNetwork("Kyivstar");
		MobileTower rtower = new MobileTower(0,0,rnetwork,10);
		LowCostPackage pack = new LowCostPackage();
		rnetwork.addNewPackage(1, pack);
		rnetwork.setBasicPackageID(1);
		
		Subscriber subs_1 = new Subscriber("09700000001");
		Subscriber subs_2 = new Subscriber("09700000002");
		
		subs_1.setOperator(rnetwork);
		subs_2.setOperator(rnetwork);
		
		subs_1.setX(10);
		subs_1.setY(10);

		subs_2.setX(100);
		subs_2.setY(100);
		
		subs_1.chargeMoneyOnCount(10f);
		
		subs_1.sendSMS(subs_2, "Test Message", Arrays.asList(rtower));
		assertEquals(9.9f,subs_1.getMoneyOnCount(),0.01f);
		Thread.sleep(1000);
	}

	@Test
	public void testMoneyTransfer() throws InterruptedException, MobileNetworkNotSupported{
		System.out.println("Test 5: Money transfer");
		MobileNetwork rnetwork = new MobileNetwork("Kyivstar");
		MobileTower rtower = new MobileTower(0,0,rnetwork,10);
		MobileTower tower = spy(rtower);
		Package lcp = new LowCostPackage();
	
		rnetwork.addNewPackage(1, lcp);
		rnetwork.setBasicPackageID(1);
		
		Subscriber subs_1 = new Subscriber("09700000001");
		Subscriber subs_2 = new Subscriber("09700000002");

		subs_1.setOperator(rnetwork);
		subs_2.setOperator(rnetwork);
		
		subs_2.setX(100);
		subs_2.setY(100);
	
		subs_1.chargeMoneyOnCount(10f);
		
		when(tower.isInRange(subs_1)).thenReturn(true);
		
		subs_1.transferMoneyTo(subs_2, 10.f, Arrays.asList(tower));		
		Thread.sleep(1000);
	}

	@Test
	public void realMoneyTransfer() throws InterruptedException{
		System.out.println("Test 7: real money transfer");
		MobileNetwork rnetwork = new MobileNetwork("Kyivstar");
		MobileTower rtower = new MobileTower(0,0,rnetwork,10);
		MobileTower tower = spy(rtower);
		Package pack = new LowCostPackage();
		rnetwork.addNewPackage(1, pack);
		rnetwork.setBasicPackageID(1);
		
		Subscriber subs_1 = new Subscriber("09700000001");
		Subscriber subs_2 = new Subscriber("09700000002");

		subs_1.setOperator(rnetwork);
		subs_2.setOperator(rnetwork);
		
		subs_2.setX(100);
		subs_2.setY(100);
	
		subs_1.chargeMoneyOnCount(20f);
		subs_2.chargeMoneyOnCount(20f);
		
		when(tower.isInRange(subs_1)).thenReturn(true);
		
		subs_1.transferMoneyTo(subs_2, 10.f, Arrays.asList(tower));		
		Thread.sleep(1000);
		assertEquals(30f,subs_2.getMoneyOnCount(), 0.001f);
		assertEquals(9.8f,subs_1.getMoneyOnCount(), 0.001f);
	}
	
	@Test
	public void realMoneyTransferToIllegalNetwork() throws InterruptedException{
		System.out.println("Test 9: attemtp to transfer money to the ubscriber of another operator");
		MobileNetwork rnetwork = new MobileNetwork("Kyivstar");
		MobileNetwork rnetwork_2 = new MobileNetwork("Life");

		
		MobileTower rtower = new MobileTower(0,0,rnetwork,10);
		MobileTower tower = spy(rtower);
		Package pack = new LowCostPackage();
		
		rnetwork.addNewPackage(1, pack);
		rnetwork.setBasicPackageID(1);
		
		rnetwork_2.addNewPackage(1, pack);
		rnetwork_2.setBasicPackageID(1);
		
		Subscriber subs_1 = new Subscriber("09700000001");
		Subscriber subs_2 = new Subscriber("09700000002");

		subs_1.setOperator(rnetwork);
		subs_2.setOperator(rnetwork_2);
		
		subs_2.setX(100);
		subs_2.setY(100);
	
		subs_1.chargeMoneyOnCount(20f);
		subs_2.chargeMoneyOnCount(20f);
		
		when(tower.isInRange(subs_1)).thenReturn(true);
		
		subs_1.transferMoneyTo(subs_2, 10.f, Arrays.asList(tower));		
		Thread.sleep(1000);
	}
}
