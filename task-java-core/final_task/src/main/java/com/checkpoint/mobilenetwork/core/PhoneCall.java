package com.checkpoint.mobilenetwork.core;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.checkpoint.mobilenetwork.core.packages.*;
import com.checkpoint.mobilenetwork.core.packages.Package;
import com.checkpoint.mobilenetwork.core.packages.Package.NotEnoughMoney;

/**
 * Represents phone call
 * 
 * @author roman
 *
 */
public class PhoneCall {
	private Logger logger = Logger.getLogger("Phone call");

	private Subscriber producer;
	private Subscriber consumer;
	
	private long duration;
	private Thread t;

	public PhoneCall(Subscriber producer, Subscriber consumer, long duration) {
		this.producer = producer;
		this.consumer = consumer;
		this.duration = duration;
	}

	/**
	 * Checks if call is allowed
	 * @return is allowed or not?
	 * @throws InterruptedException
	 * @throws NotEnoughMoney - when producer have no money
	 */
	public boolean performCall() throws InterruptedException, NotEnoughMoney {
		
		// package checks if call is enabled
		if(this.producer.getPack().performCall(this)){
			this.t = new Thread(new CallRunnable());
			//If consumer is still busy, wait before previous call will finish
			if (consumer.getPhoneCall()!=null){
				logger.log(Level.INFO, producer+" - consumer is busy:"+consumer);
				consumer.getPhoneCall().getCallThread().join();
			}

			this.t.start();			
			return true;
		} else {
			return false;
		}
		
	}
	
	/**
	 * @return thread, where call is executing 
	 */
	public synchronized Thread getCallThread() {
		return this.t;
	}

	/**
	 * 
	 * @return call duration
	 */
	public long getDuration() {
		return this.duration;
	}
	
	public Subscriber getProducer() {
		return producer;
	}

	public void setProducer(Subscriber producer) {
		this.producer = producer;
	}

	public Subscriber getConsumer() {
		return consumer;
	}

	public void setConsumer(Subscriber consumer) {
		this.consumer = consumer;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	@Override
	public String toString() {
		return "producer:"+this.producer+" consumer:"+this.consumer+ " duration:"+this.duration;
	}
	/**
	 * Call executes in this runnable
	 * @author roman
	 *
	 */
	private class CallRunnable implements Runnable {

		@Override
		public void run() {
			
			PhoneCall.this.consumer.setPhoneCall(PhoneCall.this);
			PhoneCall.this.producer.setPhoneCall(PhoneCall.this);
			
			logger.log(Level.INFO, "performing call between " + producer + " and " + consumer);
			try {
				Thread.sleep(duration);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			logger.log(Level.INFO, "call between " + producer + " and " + consumer+" finnished");
		}
	}
}
