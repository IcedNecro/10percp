package com.checkpoint.mobilenetwork.core;

import java.util.List;
import java.util.Stack;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.checkpoint.mobilenetwork.core.packages.Package;

public class Subscriber {
	private int x=0,y=0;

	private float moneyOnCount;
	private int minutes;
	private Logger logger;
	private Package pack;
	private String phoneNumber;
	private boolean free = true;
	private PhoneCall phoneCall;
	
	private IncomingCall incoming; 
	private NotificationListener notificationListener;

	public Subscriber(String phoneNumber, Package pack, int x, int y) {
		this(phoneNumber,pack);
		this.x=0;
		this.y=0;
	}

	
	public Subscriber(String phoneNumber, Package pack) {
		this.phoneNumber = phoneNumber;
		this.pack = pack;
		this.logger = Logger.getLogger("Subscriber "+this.phoneNumber);
		
		this.incoming = new IncomingCall();
		this.notificationListener = new NotificationListener();
		this.incoming.start();
		this.notificationListener.start();
	}
	
	public synchronized boolean isFree() {
		return free;
	}

	public synchronized void setFree(boolean isFree) {
		this.free = isFree;
	}
	
	public void performCallTo(Subscriber subscriber, List<MobileTower> mobiTowers) {
		for(MobileTower tower : mobiTowers) {
			if(tower.isInRange(this)) {
				try {
					CallResponse response = tower.sendRequest(new CallRequest(this, subscriber, this.pack));
					this.incoming.setResponse(response);
					return;
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void sendSMS(Subscriber consumer, String text,List<MobileTower> mobiTowers) {
		for(MobileTower tower : mobiTowers) {
			if(tower.isInRange(this)) {
				tower.sendNotification(new SMS(this, consumer, text));
			}
		}
	}
	
	public void receiveNotification(Notification notification) {
		this.notificationListener.addNotification(notification);
	}
	
	public PhoneCall getPhoneCall() {
		return this.phoneCall;
	}

	public void setPhoneCall(PhoneCall another) {
		this.phoneCall = another;
	}

	
	public float getMoneyOnCount() {
		return moneyOnCount;
	}

	public Package getPack() {
		return this.pack;
	}
	
	public void chargeMoneyOnCount(float moneyOnCount) {
		this.moneyOnCount += moneyOnCount;
		this.pack.charged(moneyOnCount);
	}

	public int getMinutes() {
		return minutes;
	}

	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	
	@Override 
	public String toString() {
		return this.phoneNumber;
	}
	

	private class IncomingCall extends Thread {
		
		private CallResponse response;
		
		public void setResponse(CallResponse callResponse) {
			this.response = callResponse;
		}
		@Override
		public void run() {
			logger.log(Level.INFO, "Subscriber "+phoneNumber+" is ready to recieve incoming calls");
			while(true) {
				if(Subscriber.this.isFree()){
					synchronized(Subscriber.this){
						
						if(Subscriber.this.phoneCall!=null) {
				
								logger.log(Level.INFO, phoneNumber + " Call " + Subscriber.this.phoneCall + " started");
								try {
									Thread t = Subscriber.this.phoneCall.getCallThread();
									t.join();
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								logger.log(Level.INFO, phoneNumber + " Call " + Subscriber.this.phoneCall + " successfully finished");
								Subscriber.this.phoneCall = null;
							}
							if(response!=null) {
								logger.log(Level.INFO, this.response.getStatus().name());
								this.response = null;
							}
						
						}
				}
			}
		}
	}
	
	private class NotificationListener extends Thread {
		private Stack<Notification> notifications = new Stack<Notification>();
		
		public void addNotification(Notification notification){
			this.notifications.add(notification);
		}
		
		@Override
		public void run() {
			while(true) {
				if(notifications.size()!=0) {
					notifications.pop().perform();
				}
			}			
		}
	}
}
