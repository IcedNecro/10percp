package com.checkpoint.mobilenetwork.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.checkpoint.mobilenetwork.core.Notification.Acceptable;
import com.checkpoint.mobilenetwork.core.packages.Package.MobileNetworkNotSupported;
import com.checkpoint.mobilenetwork.core.packages.Package.NotEnoughMoney;
import com.checkpoint.mobilenetwork.core.packages.Package;


import java.util.concurrent.Future;


/**
 * This class represents MobileNetwork
 * MobileNetwork contains a List of available towers, all of subscribers
 * Also, it performs all operations - without it you won't be able to perform any operation
 * 
 * Each Mobile Network contains a set of Package prototypes, that are available for its subscribers
 * 
 * @author roman
 *
 */
public class MobileNetwork {
	private CopyOnWriteArrayList<MobileTower> mobileTowers = new CopyOnWriteArrayList<MobileTower>();
	private HashMap<Integer,Package> packagePrototypes = new HashMap<Integer, Package>();
	private List<Subscriber> subcribers = new ArrayList<Subscriber>();
	private Integer basicPackageID;
	private String name;
	private ExecutorService service = Executors.newFixedThreadPool(1000);
	
	
	public MobileNetwork(String name){
		this.name = name;
	}
	
	/**
	 * Sets an default id of package that is given to all subscribers that subscribed current network recently
	 * @param i - id of package
	 */
	public void setBasicPackageID(Integer i) {
		this.basicPackageID = i;
	}
	
	/**
	 * Adds new package to Package prototypes set. If package with such id already exists, 
	 * the Network automatically sends notification that the previous Package is deprecated
	 * and changes it for each subscriber to a new one
	 * 
	 * @param id - id of new Package
	 * @param pack - Package prototype
	 */
	public void addNewPackage(Integer id,Package pack) {
		Package oldePackage = this.packagePrototypes.put(id, pack);
		// If package with such id exists
		if(oldePackage!=null) {
			// Getting class of prototype
			final Class<? extends Package> toCompare = oldePackage.getClass();
			for(Subscriber s: this.subcribers) {
				Class<? extends Package> packClass = s.getPack().getPrototypeClass();
				
				// is it a clone of prototype?
				if(toCompare.equals(packClass)){
					// sends notification that package is changed
					s.receiveNotification(new ChangePackageNotification(this,s,s+" - Your package is deprecated. It was authomaticaly"
							+ " changed to "+pack,pack.clone()));
				}
			}				
		}
	}
	
	/**
	 * Sends package change notification to the subscribers
	 * @param subs - subscriber
	 * @param id - id of package
	 */
	public void performPackageChange(Subscriber subs, Integer id){
		subs.receiveNotification(new ChangePackageNotification(this,subs,subs+" - You have changed your package to "
				+this.packagePrototypes.get(id),this.packagePrototypes.get(id).clone()));
	}
	
	/**
	 * adds subscriber to the network and gives him default package
	 * @param s
	 */
	public void addSubscriber(Subscriber s) {
		this.subcribers.add(s);
		s.setPack(packagePrototypes.get(this.basicPackageID).clone());
	}
	
	/**
	 * adds tower to the network
	 * @param tower
	 */
	public void addTower(MobileTower tower) {
		this.mobileTowers.add(tower);
	}
	
	/**
	 * Returns the future call that would be executed at towers. This method 
	 * connects the call producer with consumer and starts the call
	 * 
	 * @param r - request for call
	 * @return 
	 * @throws InterruptedException
	 */
	public Future<PhoneCall> getResponse(CallRequest r) throws InterruptedException {
		final Subscriber subs = r.getConsumer();
		final CallRequest request =r;
		
		// is consumer in range of towers
		for(MobileTower tower: mobileTowers) {
			if(tower.isInRange(subs)){
				Future<PhoneCall> future = service.submit(new Callable<PhoneCall>() {
					@Override
					public PhoneCall call() throws InterruptedException {						
						//performing call
						PhoneCall phCall = new PhoneCall(request.getProducer(), request.getConsumer(), request.getDuration());			
						try {
							if(phCall.performCall()) 
								return phCall;
						} catch (NotEnoughMoney e) {
							SystemNotification notification = new SystemNotification(MobileNetwork.this, phCall.getProducer(),"You have not enough money to make call");
							phCall.getProducer().receiveNotification(notification);
						}
						return null;
					}
				});
				tower.submitConnection(future);
				return future;
			}
		}
	
		SystemNotification notification = new SystemNotification(MobileNetwork.this, r.getProducer(),"Consumer is out of range");
		r.getProducer().receiveNotification(notification);
		return null;
	}
	
	/**
	 * Finds the consumer if it is in range of towers and sends notification to it
	 * 
	 * @param notification notification that would be send
	 */
	public void sendNotification(Notification notification) {
		Subscriber subs = notification.getConsumer();
		boolean isInRange = false;
		// looks for consumer
		for(MobileTower tower: mobileTowers) {
			isInRange=tower.isInRange(subs);
			try {
				if(isInRange){
					//which type of notification?
					if(notification instanceof SMS) {
						if(((Acceptable)notification).isAccepted()) {
							notification.getProducer().receiveNotification(new SystemNotification(this, notification.getProducer(), "SMS successfully send"));
							subs.receiveNotification(notification);
						} else
							notification.getProducer().receiveNotification(new SystemNotification(this, notification.getProducer(), "No enough money to send sms"));
					} else if(notification instanceof TransferMoneyNotification){
						if(((Acceptable)notification).isAccepted()) {
							notification.getProducer().receiveNotification(new SystemNotification(this, notification.getProducer(), "Money transfer to "+notification.getConsumer()+" is sucessfull"));
							subs.receiveNotification(notification);
						} else
							notification.getProducer().receiveNotification(new SystemNotification(this, notification.getProducer(), "Not enough money to transfer money for another subscriber"));					
					}
					return;
				}
			} catch(MobileNetworkNotSupported e) {
				notification.getProducer().receiveNotification(new SystemNotification(this, notification.getProducer()," this operation isn't supported by consumer's mobile operator "+notification.getConsumer().getOperator() ));
			}
		}
		if(!isInRange) 
			notification.getProducer().receiveNotification(new SystemNotification(this, notification.getProducer(), "Consumer is out of range"));

	}
	
	@Override 
	public boolean equals(Object o){

		if(o instanceof MobileNetwork)
			return ((MobileNetwork)o).name.equals(this.name);
		return false;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
}
