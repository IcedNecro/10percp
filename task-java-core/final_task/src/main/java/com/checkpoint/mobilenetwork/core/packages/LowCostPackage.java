package com.checkpoint.mobilenetwork.core.packages;

import com.checkpoint.mobilenetwork.core.PhoneCall;
import com.checkpoint.mobilenetwork.core.Subscriber;

public class LowCostPackage extends Package {

	private static final float COST_PER_MINUTE = 0.25f;
	private static final float COST_FOR_CONNECTION = 0.05f;
	
	@Override
	protected boolean perform(PhoneCall call) {
		if(call.getProducer().getMoneyOnCount()<COST_FOR_CONNECTION) {
			return false;
		} else {
			Long time = call.getDuration();
			float reduceMoney = (time / 1000)*COST_PER_MINUTE+COST_FOR_CONNECTION;
			call.getProducer().chargeMoneyOnCount(call.getProducer().getMoneyOnCount()-reduceMoney);
			return true;
		}
	}

	@Override
	protected void chargedWithMoney(Subscriber subscriber, float amount) {}
	
}
