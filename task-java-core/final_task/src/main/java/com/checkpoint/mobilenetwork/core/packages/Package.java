package com.checkpoint.mobilenetwork.core.packages;

import com.checkpoint.mobilenetwork.core.PhoneCall;
import com.checkpoint.mobilenetwork.core.Subscriber;

public abstract class Package {
	private Package pack;
	private String description;
	private boolean isDeprecated;
	private Subscriber subscriber;
	
	protected Package() {}
	
	private Package(Package pack) {
		this.description = pack.getDescription();
		this.pack = pack;
	}
	
	private Package(Package pack, Subscriber subscriber) {
		this.subscriber = subscriber;
		this.description = pack.getDescription();
		this.pack = pack;
	}
	
	public Package clone(Subscriber subscriber) {
		final Package pack = this;
		return new Package(this,subscriber) {
			@Override
			protected boolean perform(PhoneCall call) {
				return getPack().perform(call); 
			}
			
			@Override
			protected void chargedWithMoney(Subscriber subscriber,float ammount) {
				getPack().chargedWithMoney(subscriber, ammount);
			}};
	}
	
	public Package clone() {
		final Package pack = this;
		return new Package(this,subscriber) {
			@Override
			protected boolean perform(PhoneCall call) {
				return getPack().perform(call); 
			}
			
			@Override
			protected void chargedWithMoney(Subscriber subscriber,float ammount) {
				getPack().chargedWithMoney(subscriber, ammount);
			}};
	}
	
	public Package getPack() {
		return pack;
	}

	protected abstract boolean perform(PhoneCall call);
	protected abstract void chargedWithMoney(Subscriber subscriber, float ammount);
	
	public void charged(float ammount) {
		this.chargedWithMoney(this.subscriber, ammount);
	}
	
	public boolean performCall(PhoneCall call) {
		
		return this.perform(call);
	}
	
	public void setPack(Package p) {
		this.pack = p;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isDeprecated() {
		return isDeprecated;
	}

	public void setDeprecated(boolean isDeprecated) {
		this.isDeprecated = isDeprecated;
	}

}
