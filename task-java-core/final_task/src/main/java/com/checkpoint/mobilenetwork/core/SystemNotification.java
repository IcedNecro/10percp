package com.checkpoint.mobilenetwork.core;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SystemNotification implements Notification{
	private MobileNetwork network;
	private Subscriber consumer;
	private String text;
	private static Logger logger = Logger.getLogger("System Notification");
	
	public SystemNotification(MobileNetwork network, Subscriber subscriber, String text) {
		this.text = text;
		this.consumer = subscriber;
		this.network = network;
	}
	
	@Override
	public void perform() {
		logger.log(Level.INFO, network+": System Notification for:" + this.consumer+" - "+this.text);
	}
	
}
