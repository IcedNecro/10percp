package com.checkpoint.mobilenetwork.core;

import java.util.logging.Level;

import com.checkpoint.mobilenetwork.core.Notification.Acceptable;
import com.checkpoint.mobilenetwork.core.packages.Package.MobileNetworkNotSupported;

/**
 * Notification for money transfering
 * @author roman
 *
 */
public class TransferMoneyNotification extends Notification implements Acceptable{
	private Float amount;
	
	public TransferMoneyNotification(Subscriber producer, Subscriber consumer, Float amount) {
		super(producer, consumer);
		this.amount = amount;
	}
	
	public Float getAmount(){
		return amount;
	}

	public void setAmount(Float amount) {
		this.amount = amount;
	}

	@Override 
	public boolean isAccepted() throws MobileNetworkNotSupported {
		return this.producer.getPack().performMoneyTransferOperation(this);
	};
	
	@Override
	public void perform() {
		// charges count of consumer 
		this.consumer.chargeMoneyOnCount(amount);
		logger.log(Level.INFO, this.consumer+" - Subscriber's count was charged for "+amount+" by subscriber " +this.producer+
				". Current money amount on count:"+this.consumer.getMoneyOnCount());
	}

}
