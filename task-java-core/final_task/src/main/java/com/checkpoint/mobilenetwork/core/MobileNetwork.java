package com.checkpoint.mobilenetwork.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.CopyOnWriteArrayList;

import com.checkpoint.mobilenetwork.core.NotificationRequest.NotificationStatus;

public class MobileNetwork {
	private CopyOnWriteArrayList<MobileTower> mobileTowers = new CopyOnWriteArrayList<MobileTower>();
	private HashMap<Integer,Package> packagePrototypes = new HashMap<Integer, Package>();
	
	public void addNewPackage(Integer id,Package pack) {
		Package oldePackage = this.packagePrototypes.put(id, pack);
		
		if(oldePackage!=null) {
			
		}
	}
	
	public void addTower(MobileTower tower) {
		this.mobileTowers.add(tower);
	}
	
	public CallResponse getResponse(CallRequest request) throws InterruptedException {
		Subscriber subs = request.getConsumer();
		CallResponse response = null;
		for(MobileTower tower: mobileTowers) {
			if(tower.isInRange(subs)){
				System.out.println(subs.isFree());
				if (!subs.isFree()){
					response = new CallResponse(null, CallRequest.ResponseStatus.CONSUMER_IS_BUSY);
					return response;
				}
				else  {
					PhoneCall phCall = new PhoneCall(request.getProducer(), request.getConsumer(), 4000);			
					if(phCall.performCall()) {
						System.out.println("lol3");
						
						response = new CallResponse(phCall, CallRequest.ResponseStatus.OK);
					} else 
						response = new CallResponse(null, CallRequest.ResponseStatus.NOT_ENOUGH_MONEY);
					return response;
				}
			}
		}
		return new CallResponse(null, CallRequest.ResponseStatus.CONSUMER_OUT_OF_RANGE);
	}
	
	public NotificationRequest sendNotification(Notification notification) {
		Subscriber subs = ((SMS)notification).getConsumer();
		NotificationRequest response = new NotificationRequest();
		response.setStatus(NotificationStatus.NOTIFICATION_NOT_DELIVERED);
		for(MobileTower tower: mobileTowers) {
			if(tower.isInRange(subs)){
				response.setStatus(NotificationStatus.OK);
				((SMS)notification).getProducer().receiveNotification(new SMS(null, ((SMS)notification).getProducer(), "System message recieved"));
				subs.receiveNotification(notification);
				
				return response;
			}
		}
		return response;
	}
}
