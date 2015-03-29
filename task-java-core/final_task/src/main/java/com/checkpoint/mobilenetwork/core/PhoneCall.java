package com.checkpoint.mobilenetwork.core;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.checkpoint.mobilenetwork.core.packages.*;
import com.checkpoint.mobilenetwork.core.packages.Package;

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

	public boolean performCall() throws InterruptedException {
		
		if(this.producer.getPack().performCall(this)){
			this.t = new Thread(new CallRunnable());
			this.t.start();
			
			producer.setPhoneCall(this);
			consumer.setPhoneCall(this);
			return true;
		} else return false;
		
	}
	
	public synchronized Thread getCallThread() {
		return this.t;
	}

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
	
	private class CallRunnable implements Runnable {

		@Override
		public void run() {
			
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
