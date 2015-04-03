package com.checkpoint.test;

import static org.junit.Assert.*;

import javax.crypto.interfaces.PBEKey;

import org.junit.Test;

import com.checkpoint.mobilenetwork.core.PhoneCall;
import com.checkpoint.mobilenetwork.core.SMS;
import com.checkpoint.mobilenetwork.core.Subscriber;
import com.checkpoint.mobilenetwork.core.TransferMoneyNotification;
import com.checkpoint.mobilenetwork.core.packages.LowCostPackage;
import com.checkpoint.mobilenetwork.core.packages.Package;
import com.checkpoint.mobilenetwork.core.packages.Package.NotEnoughMoney;

public class TestPackagePrototype {

	@Test
	public void test() throws NotEnoughMoney {
		LowCostPackage p = new LowCostPackage();
		Package pa = p.clone();
		

		assertEquals(null,p.getPrototypeClass());
		assertEquals(LowCostPackage.class, pa.getPrototypeClass());
	}

}
