package com.checkpoint.mobilenetwork.core;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Mobile Tower performs connection to the MobileNetwork. Before making call, subscriber scans the nearest 
 * Tower to send request for notification or call and binds connection to it. Mobile Tower has a limited amount of 
 * connections. When there are to much connections, calls are placed in queue, and executes after previous connections 
 * finished
 * 
 * @author roman
 *
 */
public class MobileTower {
	private Logger logger = Logger.getLogger("MobileTower");
	
	private final static int MAX_RANGE = 1000;
	
	private int x, y;
	private ArrayList<MobileNetwork> belongsTo = new ArrayList<MobileNetwork>();
	private ExecutorService service;
	
	public MobileTower(int x, int y, MobileNetwork belongsTo, int restriction) {
		this.x = x;
		this.y = y;
		this.belongsTo.add(belongsTo);
		belongsTo.addTower(this);
		this.service = Executors.newFixedThreadPool(restriction);
	}
	
	/**
	 * Checks if subscriber is in range of this tower
	 * @param subscriber
	 * @return true if subscriber is 
	 */
	public boolean isInRange(Subscriber subscriber) {
		return Math.sqrt(Math.pow(this.x-subscriber.getX(),2)+Math.pow(this.y-subscriber.getY(),2))<MAX_RANGE ? true: false;
	}
	
	/**
	 * Send notification request to Network
	 * @param notification
	 */
	public void sendNotification(Notification notification){
		//Checks if the consumer's operator is enabled for this tower
		for(MobileNetwork network : this.belongsTo){
			if(network.equals(notification.consumer.getOperator())) {
				network.sendNotification(notification);
				return;
			}
		}
		notification.getProducer().receiveNotification(new SystemNotification(notification.getProducer().getOperator(),
				notification.getProducer(), "This network " + notification.consumer.getOperator()+" isn't available"));
	}
	
	/**
	 * binds the connection to tower when consumer lies in its range
	 * @param future
	 */
	public void submitConnection(Future<PhoneCall> future) {
		this.service.submit(new OperateThread(future));
	}
	
	
	/**
	 * Sends request for call to the Network
	 * @param request
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public void sendRequest(CallRequest request) throws InterruptedException, ExecutionException {

		for(MobileNetwork network : this.belongsTo) {
			System.out.println(network+" "+request.getConsumer().getOperator());
				
			if(network.equals(request.getConsumer().getOperator())){
				
				service.submit(new OperateThread(network.getResponse(request)));
				return;
			}
		}
		request.getProducer().receiveNotification(new SystemNotification(request.getProducer().getOperator(),
				request.getProducer(), "This network " + request.getConsumer().getOperator()+" isn't available"));
	}
	
	/**
	 * adds this tower to the network
	 * @param network
	 */
	public void addNetwork(MobileNetwork network){
		this.belongsTo.add(network);
		network.addTower(this);
	}
	
	public ArrayList<MobileNetwork> sharedNetworks() {
		return this.belongsTo;
	}
	
	@Override
	public String toString() {
		return "Tower:("+this.x+", "+this.y+")";
	}
	
	/**
	 * Thread that executes in thread pool of Tower connections
	 * @author roman
	 *
	 */
	private class OperateThread implements Runnable {
		private Future<PhoneCall> call;
		
		OperateThread(Future<PhoneCall> call) {
			this.call = call;
		}
		
		@Override
		public void run() {
			if(call != null) {				
				
				try {
					PhoneCall phCall = call.get();
					if(phCall!=null){
						
						logger.log(Level.INFO,"Mobile Tower x="+x+" y="+y+" - Connection for Mobile Tower is binded");
					
						phCall.getCallThread().join();
						logger.log(Level.INFO,"Mobile Tower x="+x+" y="+y+" - Connection for Mobile Tower is unbinded");
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
