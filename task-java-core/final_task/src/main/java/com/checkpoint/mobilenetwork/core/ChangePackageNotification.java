package com.checkpoint.mobilenetwork.core;

import com.checkpoint.mobilenetwork.core.packages.Package;

/**
 * Notification that performs when network tries to change older package to a new one
 * @author roman
 *
 */
public class ChangePackageNotification extends SystemNotification{

	private Package newPackage;
	
	public ChangePackageNotification(MobileNetwork network,
			Subscriber subscriber, String text, Package newPackage) {
		super(network, subscriber, text);
		this.newPackage = newPackage;
	}
	
	@Override
	public void perform() {
		super.perform();
		this.consumer.setPack(this.newPackage);
	}

}
