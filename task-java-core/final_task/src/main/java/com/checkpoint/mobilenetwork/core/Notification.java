package com.checkpoint.mobilenetwork.core;

import java.util.logging.Logger;

import com.checkpoint.mobilenetwork.core.packages.Package.MobileNetworkNotSupported;

/**
 * Abstract class of notifications 
 * It could be different, but there are two main implementation - as SMS, and as System Message
 * 
 * It can perform different actions - it depends from perform() method implementation
 * Generally, this class is an implementation of Command pattern
 * 
 * @author roman
 *
 */
public abstract class Notification {
	protected static Logger logger = Logger.getLogger("Notification");
	
	protected Subscriber consumer;
	protected Subscriber producer;
	
	public Notification(Subscriber producer, Subscriber consumer){
		this.producer = producer;
		this.consumer = consumer;
	}
	
	public Subscriber getConsumer() {
		return consumer;
	}


	public void setConsumer(Subscriber consumer) {
		this.consumer = consumer;
	}


	public Subscriber getProducer() {
		return producer;
	}


	public void setProducer(Subscriber producer) {
		this.producer = producer;
	}
	
	/**
	 * Notifications, that implements this interface, can be checked if they can be executed or not
	 * @author roman
	 *
	 */
	public static interface Acceptable {
		/**
		 * 
		 * @return true if current notification is accepted
		 * @throws MobileNetworkNotSupported
		 */
		public boolean isAccepted() throws MobileNetworkNotSupported;
	}
	
	/**
	 * performs any action that you implement when subscriber receives this notification
	 * It allows to execute all notifications, not only SMS, in the single thread on subscriber side
	 */
	public abstract void perform();
}
