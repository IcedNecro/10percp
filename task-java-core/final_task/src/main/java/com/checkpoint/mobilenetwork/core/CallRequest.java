package com.checkpoint.mobilenetwork.core;

import com.checkpoint.mobilenetwork.core.packages.Package;


public class CallRequest {
	private Subscriber producer;
	private Subscriber consumer;
	private Package pack;
	
	public CallRequest(Subscriber producer, Subscriber consumer, Package pack) {
		this.consumer = consumer;
		this.producer = producer;
		this.pack = pack;
	}
	
	public Subscriber getProducer() {
		return producer;
	}

	public Subscriber getConsumer() {
		return consumer;
	}

	public Package getPack() {
		return pack;
	}

	public enum ResponseStatus {
		CONSUMER_IS_BUSY,
		CONSUMER_OUT_OF_RANGE,
		NOT_ENOUGH_MONEY,
		NETWORK_IS_OVERLOADED,
		OK
	}
}
