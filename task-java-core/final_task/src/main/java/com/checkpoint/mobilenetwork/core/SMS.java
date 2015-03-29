package com.checkpoint.mobilenetwork.core;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.checkpoint.mobilenetwork.core.packages.*;

public class SMS implements Notification{
	private static Logger logger = Logger.getLogger("Notification");
	private Subscriber producer;
	private Subscriber consumer;
	private String text;
	
	public SMS(Subscriber producer, Subscriber consumer, String text) {
		this.producer = producer;
		this.consumer = consumer;
		this.text = text;
	}
	
	public Subscriber getProducer() {
		return producer;
	}

	public Subscriber getConsumer() {
		return consumer;
	}

	public void setConsumer(Subscriber consumer) {
		this.consumer = consumer;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public void perform() {
		logger.log(Level.INFO, this.producer+":"+this.text);
	}
	
}
