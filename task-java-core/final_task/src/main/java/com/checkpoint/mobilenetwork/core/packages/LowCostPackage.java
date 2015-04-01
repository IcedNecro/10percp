package com.checkpoint.mobilenetwork.core.packages;

import com.checkpoint.mobilenetwork.core.PhoneCall;
import com.checkpoint.mobilenetwork.core.SMS;
import com.checkpoint.mobilenetwork.core.Subscriber;
import com.checkpoint.mobilenetwork.core.TransferMoneyNotification;

public class LowCostPackage extends Package {

	protected String name = "Low cost package";
	
	private static final float COST_PER_MINUTE_IN_NETWORK = 0.25f;
	private static final float COST_FOR_CONNECTION_IN_NETWORK = 0.05f;
	private static final float COST_PER_MINUTE_OUT_NETWORK = 0.30f;
	private static final float COST_FOR_CONNECTION_OUT_NETWORK = 0.1f;
	
	private static final float COST_FOR_SMS_OUT_NETWORK = 0.2f;
	private static final float COST_FOR_SMS = 0.10f;
	
	private static final float TAX_FOR_MONEY_TRANSFER = 0.02f;
	
	@Override
	protected boolean perform(PhoneCall call) throws NotEnoughMoney {
		float connection = 0;
		float ratePerMinute = 0;
		if(call.getProducer().getOperator().equals(call.getConsumer().getOperator())) {
			connection = COST_FOR_CONNECTION_IN_NETWORK;
			ratePerMinute = COST_PER_MINUTE_IN_NETWORK;
		} else {
			connection = COST_FOR_CONNECTION_OUT_NETWORK;
			ratePerMinute = COST_PER_MINUTE_OUT_NETWORK;			
		}
		if(call.getProducer().getMoneyOnCount()<connection) {
			throw new NotEnoughMoney();
		} else {
			Long time = call.getDuration();
			float reduceMoney = (time / 1000)*ratePerMinute+connection;
			call.getProducer().chargeMoneyOnCount(-reduceMoney);
			return true;
		}
	
	}

	@Override
	protected void chargedWithMoney(Subscriber subscriber, float amount) {}
	
	@Override 
	protected boolean performSMS(SMS sms) {
		float rate = 0;
		if(sms.getProducer().getOperator().equals(sms.getConsumer().getOperator())) 
			rate = COST_FOR_SMS;
		else 
			rate = COST_FOR_SMS_OUT_NETWORK;
		if(sms.getProducer().getMoneyOnCount()>rate) {
			sms.getProducer().chargeMoneyOnCount(-rate);
			return true;
		}
		else 
			return false;
	}
	
	@Override
	protected boolean performMoneyTransfer(TransferMoneyNotification notification) throws MobileNetworkNotSupported {
		float moneyToTransfer = notification.getAmount()*TAX_FOR_MONEY_TRANSFER+notification.getAmount();
		if(notification.getProducer().getOperator().equals(notification.getConsumer().getOperator())){
			if(notification.getProducer().getMoneyOnCount()>moneyToTransfer){
				notification.getProducer().chargeMoneyOnCount(-moneyToTransfer);
				return true;
			} else 
				return false;
		} else throw new MobileNetworkNotSupported();
	}
	
	
}
