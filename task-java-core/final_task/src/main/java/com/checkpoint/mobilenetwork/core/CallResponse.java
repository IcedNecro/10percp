package com.checkpoint.mobilenetwork.core;

public class CallResponse {
	private PhoneCall call;
	private CallRequest.ResponseStatus status;

	public CallResponse(PhoneCall call, CallRequest.ResponseStatus status) {
		this.call = call;
		this.status = status;
	}
	
	public PhoneCall getCall() {
		return call;
	}

	public void setCall(PhoneCall call) {
		this.call = call;
	}

	public CallRequest.ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(CallRequest.ResponseStatus status) {
		this.status = status;
	}

}
