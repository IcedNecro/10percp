package com.checkpoint.mobilenetwork.core;

import java.util.List;
import java.util.Stack;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.checkpoint.mobilenetwork.core.packages.Package;

/**
 * Subscriber subscribes for network. It can make or receive calls and sms
 * 
 * @author roman
 *
 */
public class Subscriber {
	private int x=0,y=0;

	private float moneyOnCount;
	private int minutes;
	private Logger logger;
	private Package pack;
	private String phoneNumber;
	private PhoneCall phoneCall;
	
	private MobileNetwork operator;
	private IncomingCall incoming; 
	private NotificationListener notificationListener;

	public Subscriber(String phoneNumber, int x, int y) {
		this(phoneNumber);
		this.x=x;
		this.y=y;
	}

	public Subscriber(String phoneNumber, Package pack) {
		this(phoneNumber);
		this.operator = pack.getNetwork();
	}
		
	
	public Subscriber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
		this.logger = Logger.getLogger("Subscriber "+this.phoneNumber);
		
		this.incoming = new IncomingCall();
		this.notificationListener = new NotificationListener();
		this.incoming.start();
		this.notificationListener.start();
	}
	
	/**
	 * Sends request to change package of you mobile network. Each package has id at Mobile Network
	 * Subscriber sends an id of package that it want to get
	 * @param id - id of package 
	 * @param mobiTowers
	 */
	public void changePackage(Integer id, List<MobileTower> mobiTowers) {

		for(MobileTower tower : mobiTowers) {
			if(tower.isInRange(this) && tower.sharedNetworks().contains(this.getOperator())) {
				tower.sendRequestForPackageChange(this,id);
				return;
			}			
		}
		logger.log(Level.INFO, this.phoneNumber+" - You are offline, or the nearest mobile towers don't supports your operator");

	}
	
	/**
	 * Sends call request to another subscriber to the nearest tower
	 * @param subscriber
	 * @param mobiTowers - list of towers
	 */
	public synchronized void performCallTo(Subscriber subscriber, Long duration, List<MobileTower> mobiTowers) {
		// waits while current call will finish
		if(this.phoneCall!=null)
			try {
				this.phoneCall.getCallThread().join();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		//finds nearest tower
		for(MobileTower tower : mobiTowers) {
			if(tower.isInRange(this) && tower.sharedNetworks().contains(subscriber.getOperator())) {
				try {
					tower.sendRequest(new CallRequest(this, subscriber, duration, this.pack));
					return;
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
			}
		}
		logger.log(Level.INFO, this.phoneNumber+" - You are offline, or the nearest mobile towers don't supports your consumer operator");
	}
	
	/**
	 * Sends the sms request to the nearest tower
	 * @param consumer - consumer you want to send sms to
	 * @param text - text of sms
	 * @param mobiTowers - list of towers
	 */
	public void sendSMS(Subscriber consumer, String text,List<MobileTower> mobiTowers) {
		for(MobileTower tower : mobiTowers) {
			if(tower.isInRange(this) && tower.sharedNetworks().contains(consumer.getOperator())) {
				tower.sendNotification(new SMS(this, consumer, text));
				return;
			}
		}
	}
	
	/**
	 * Perform money transfer to another subscriber
	 * @param consumer - consumer you need to transfer money to
	 * @param amount - amount of money to transfer
	 * @param mobiTowers - list of towers
	 */
	public void transferMoneyTo(Subscriber consumer, float amount, List<MobileTower> mobiTowers) {
		//finds nearest tower
		for(MobileTower tower : mobiTowers) {
			if(tower.isInRange(this)) {
				tower.sendNotification(new TransferMoneyNotification(this, consumer, amount));
				return;
			}
		}
		logger.log(Level.INFO, this + " - Not in network");

	}
	
	public void receiveNotification(Notification notification) {
		this.notificationListener.addNotification(notification);
	}
	
	public PhoneCall getPhoneCall() {
		return this.phoneCall;
	}

	public synchronized void setPhoneCall(PhoneCall another) {

		this.phoneCall = another;
	}

	
	public float getMoneyOnCount() {
		return moneyOnCount;
	}

	public void setPack(Package pack) {
		this.pack = pack;
	}
	
	public Package getPack() {
		return this.pack;
	}

	/**
	 * charges money on count of subscriber according to package
	 * @param moneyOnCount
	 */
	public void chargeMoneyOnCount(float moneyOnCount) {
		this.moneyOnCount += moneyOnCount;
		this.pack.charged(this,moneyOnCount);
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
	
	public MobileNetwork getOperator() {
		return operator;
	}


	public void setOperator(MobileNetwork operator) {
		this.operator = operator;
		this.operator.addSubscriber(this);
	}


	@Override
	public boolean equals(Object o){
		if(o instanceof Subscriber)
			if(((Subscriber)o).phoneNumber.equals(this.phoneNumber))
				return true;
		return false; 
	}
	
	@Override 
	public String toString() {
		return this.phoneNumber;
	}
	
	
	/**
	 * Thread that listens for incoming and outgoing calls
	 * 
	 * @author roman
	 */
	private class IncomingCall extends Thread {
		
		@Override
		public void run() {
			logger.log(Level.INFO, "Subscriber "+phoneNumber+" is ready to recieve incoming calls");
			while(true) {
				synchronized (Subscriber.this) {
					// if there is a call 
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
				}
			}
		}
	}
	
	/**
	 * Listens for notifications that comes from network
	 * 
	 * @author roman
	 */
	private class NotificationListener extends Thread {
		private Stack<Notification> notifications = new Stack<Notification>();
		
		/**
		 * Adds notification to notifications stack
		 * @param notification
		 */
		public void addNotification(Notification notification){
			this.notifications.add(notification);
		}
		
		@Override
		public void run() {
			while(true) {
				if(notifications.size()!=0) {
					//if stack isn't empty
					notifications.pop().perform();
				}
			}			
		}
	}
}
