package com.checkpoint.mobilenetwork.core.packages;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.checkpoint.mobilenetwork.core.MobileNetwork;
import com.checkpoint.mobilenetwork.core.MobileTower;
import com.checkpoint.mobilenetwork.core.Notification;
import com.checkpoint.mobilenetwork.core.NotificationRequest;
import com.checkpoint.mobilenetwork.core.NotificationRequest.NotificationStatus;
import com.checkpoint.mobilenetwork.core.SMS;
import com.checkpoint.mobilenetwork.core.Subscriber;

import static org.mockito.Mockito.*;

public class TestNotification {

	@Test
	public void test() throws InterruptedException {
		MobileTower tower = mock(MobileTower.class);
		final Subscriber subs_1 = new Subscriber("09700000001",null);
		final Subscriber subs_2 = new Subscriber("09700000002",null);
		
		when(tower.sendNotification(any(Notification.class))).then(new Answer<NotificationRequest>() {

			@Override
			public NotificationRequest answer(InvocationOnMock invocation)
					throws Throwable {
				
				NotificationRequest response = new NotificationRequest();
				response.setStatus(NotificationStatus.OK);
				subs_1.receiveNotification(new SMS(subs_1, subs_2, "LOL"));
				subs_2.receiveNotification(new SMS(subs_2, subs_1, "LOL2"));
				
				return response;
			}
			
		});
		when(tower.isInRange(any(Subscriber.class))).thenReturn(true);
		
		subs_1.sendSMS(subs_2, "testMessage", Arrays.asList(tower));
		Thread.sleep(1000);
	}
	
	@Test
	public void testRealNotification() {
		
		MobileNetwork rnetwork = new MobileNetwork();//mock(MobileNetwork.class);
		MobileTower rtower = new MobileTower(0,0,rnetwork,10);
		MobileTower tower = spy(rtower);
		
		//doCallRealMethod().when(network).addTower(tower);
		//network.addTower(tower);
		//when(network.sendNotification(any(SMS.class))).thenCallRealMethod();
		
		final Subscriber subs_1 = new Subscriber("09700000001",null);
		final Subscriber subs_2 = new Subscriber("09700000002",null);
		
		when(tower.isInRange(any(Subscriber.class))).thenReturn(true);
		//when(tower.sendNotification(any(SMS.class))).thenCallRealMethod();
		
		subs_1.sendSMS(subs_2, "Test message", Arrays.asList(tower));
	}
}
