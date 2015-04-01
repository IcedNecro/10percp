package com.checkpoint.mobilenetwork.core;

import java.util.logging.Level;

import com.checkpoint.mobilenetwork.core.packages.Package;
import com.checkpoint.mobilenetwork.core.Notification.Acceptable;
/**
 * A simple sms
 * @author roman
 *
 */
public class SMS extends Notification implements Acceptable {
	private Package pack;
	private String text;
	
	public SMS(Subscriber producer, Subscriber consumer, String text) {
		super(producer, consumer);
		this.text = text;
		this.pack = producer.getPack();
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	@Override
	public boolean isAccepted(){
		return this.pack.performSMSNotification(this);
	}
	
	@Override
	public void perform() {
		logger.log(Level.INFO,"for:"+this.consumer +" from: " + this.producer+":"+this.text);
	}
	
}
