package com.checkpoint.mobilenetwork.core;

import com.checkpoint.mobilenetwork.core.packages.Package;


public class CallRequest {
	private Subscriber producer;
	private Subscriber consumer;
	private Long duration;
	private Package pack;
	
	public CallRequest(Subscriber producer, Subscriber consumer,Long duration, Package pack) {
		this.consumer = consumer;
		this.producer = producer;
		this.duration = duration;
		this.pack = pack;
	}

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
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

}
