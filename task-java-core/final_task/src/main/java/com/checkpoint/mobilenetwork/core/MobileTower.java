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

public class MobileTower {
	private Logger logger = Logger.getLogger("MobileTower");
	
	private final static int MAX_RANGE = 1000;
	
	private int x, y, restriction;
	private MobileNetwork belongsTo;
	private ExecutorService service;
	
	public MobileTower(int x, int y, MobileNetwork belongsTo, int restriction) {
		this.x = x;
		this.y = y;
		this.belongsTo = belongsTo;
		this.belongsTo.addTower(this);
		this.restriction = restriction;
		this.service = Executors.newFixedThreadPool(restriction);
	}
	
	public boolean isInRange(Subscriber subscriber) {
		return true;
	}
	
	public NotificationRequest sendNotification(Notification notification){
		return this.belongsTo.sendNotification(notification);
	}
	
	public CallResponse sendRequest(CallRequest request) throws InterruptedException, ExecutionException {
		CallResponse response = MobileTower.this.belongsTo.getResponse(request);
		service.submit(new OperateThread(response.getCall()));		
		return response;
	}
	
	private class OperateThread implements Runnable {
		private PhoneCall call;
		
		OperateThread(PhoneCall call) {
			this.call = call;
		}
		
		@Override
		public void run() {
			if(call != null) {				
				logger.log(Level.INFO,"Connection for Mobile Tower is binded");
			
				try {
					call.getCallThread().join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				logger.log(Level.INFO,"Connection for Mobile Tower is unbinded");
			}
		}
	}
}
