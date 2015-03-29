package com.checkpoint.mobilenetwork.core.packages;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.checkpoint.mobilenetwork.core.CallRequest;
import com.checkpoint.mobilenetwork.core.CallResponse;
import com.checkpoint.mobilenetwork.core.MobileNetwork;
import com.checkpoint.mobilenetwork.core.MobileTower;
import com.checkpoint.mobilenetwork.core.PhoneCall;
import com.checkpoint.mobilenetwork.core.Subscriber;
import com.checkpoint.mobilenetwork.core.CallRequest.ResponseStatus;

public class TestPackage {

	Package testPack;
	
	@Before
	public void before() {
		testPack = new Package() {

			@Override
			protected boolean perform(PhoneCall call) {
				// TODO Auto-generated method stub
				return true;
			}

			@Override
			protected void chargedWithMoney(Subscriber subscriber, float ammount) {
				// TODO Auto-generated method stub
				
			}
			
		};
	}
	
	@Test 
	public void testSubscriber() throws InterruptedException, ExecutionException {
		final Subscriber subs_1 = new Subscriber("0975840059", testPack);
		final Subscriber subs_2 = new Subscriber("0975840060", testPack);
		
		MobileTower tower = mock(MobileTower.class);
		
		doAnswer(new Answer<Void>() {

			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				PhoneCall call = new PhoneCall(subs_1, subs_2, 2000);
				System.out.println(subs_1);
				call.performCall();
			//	return new CallResponse(call, ResponseStatus.OK);
				return null;
			}
		
		}).when(tower).sendRequest((CallRequest) anyObject());
		when(tower.isInRange(subs_1)).thenReturn(true);
		subs_1.performCallTo(subs_2, Arrays.asList(tower));
		Thread.sleep(10000);
	}
	
	@Test 
	public void testTower() throws InterruptedException, ExecutionException {
		final Subscriber subs_1 = new Subscriber("0975840059", testPack);
		final Subscriber subs_2 = new Subscriber("0975840060", testPack);
		
		
		MobileNetwork network = mock(MobileNetwork.class);
		MobileTower rtower = new MobileTower(0, 0, network, 10);
		MobileTower tower = spy(rtower);
		when(network.getResponse((CallRequest) anyObject())).thenAnswer( new Answer<CallResponse>() {

			@Override
			public CallResponse answer(InvocationOnMock invocation) throws Throwable {
				PhoneCall call = new PhoneCall(subs_1, subs_2, 3000);
				call.performCall();
				return new CallResponse(call, CallRequest.ResponseStatus.OK);
		
			}
		
		});
		when(tower.isInRange(subs_1)).thenReturn(true);
		doCallRealMethod().when(tower).sendRequest((CallRequest)anyObject());
		
		subs_1.performCallTo(subs_2, Arrays.asList(rtower));
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test 
	public void testNetworkSingleCall() throws InterruptedException, ExecutionException {
		final Subscriber subs_1 = new Subscriber("0975840059", testPack);
		final Subscriber subs_2 = new Subscriber("0975840060", testPack);
		
		
		MobileNetwork rnetwork = new MobileNetwork();
		MobileTower rtower = new MobileTower(0, 0, rnetwork, 10);
		MobileTower tower = spy(rtower);
		when(tower.isInRange(subs_1)).thenReturn(true);
		
		subs_1.performCallTo(subs_2, Arrays.asList(rtower));
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Test 
	public void testTowerRestrictions() throws InterruptedException, ExecutionException {
		ArrayList<Subscriber> subscribers = new ArrayList<Subscriber>();
		System.out.println("Restriction");
		MobileNetwork rnetwork = new MobileNetwork();
		
		//MobileNetwork network = spy(rnetwork);
		MobileTower rtower = new MobileTower(0, 0, rnetwork, 10);
		MobileTower tower = spy(rtower);
		when(tower.isInRange(any(Subscriber.class))).thenReturn(true);
		
		
		for(int i = 0; i<10; i++){
			subscribers.add(new Subscriber("097000000"+i,testPack));
		}
		
		assertEquals(true, tower.isInRange(subscribers.get(0)));
		
		for(int i = 0; i<subscribers.size()-1; i+=2) {
			subscribers.get(i).performCallTo(subscribers.get(i+1),Arrays.asList(tower) );
		}
		
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*
	@Test 
	public void testBusy() throws InterruptedException, ExecutionException {
		ArrayList<Subscriber> subscribers = new ArrayList<Subscriber>();
		System.out.println("Restriction");
		MobileNetwork rnetwork = new MobileNetwork();
		
		//MobileNetwork network = spy(rnetwork);
		MobileTower rtower = new MobileTower(0, 0, rnetwork, 3);
		MobileTower tower = spy(rtower);
		when(tower.isInRange(any(Subscriber.class))).thenReturn(true);
		
		
		for(int i = 0; i<3; i++){
			subscribers.add(new Subscriber("097000000"+i,null));
		}
		
		subscribers.get(0).performCallTo(subscribers.get(1),Arrays.asList(tower));
		
		subscribers.get(2).performCallTo(subscribers.get(1),Arrays.asList(tower));
		
		try {
			Thread.sleep(12000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
}
