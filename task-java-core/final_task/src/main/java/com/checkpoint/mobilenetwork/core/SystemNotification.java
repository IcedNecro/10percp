package com.checkpoint.mobilenetwork.core;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents any system notification that comes from Mobile Network
 * @author roman
 *
 */
public class SystemNotification extends Notification{
	private MobileNetwork network;
	private String text;
	private static Logger logger = Logger.getLogger("System Notification");
	
	public SystemNotification(MobileNetwork network, Subscriber subscriber, String text) {
		super(null, subscriber);
		
		this.text = text;
		this.network = network;
	}
	
	@Override
	public void perform() {
		logger.log(Level.INFO, network+": System Notification for:" + this.consumer+" - "+this.text);
	}
	
}
