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

	public class TestPackage extends Package {

		protected String name = "Test package";
		
		@Override
		protected boolean perform(PhoneCall call) throws NotEnoughMoney {
			System.out.println("perform");
			return false;
		}

		@Override
		protected void chargedWithMoney(Subscriber subscriber, float ammount) {
			// TODO Auto-generated method stub
			
		}

		@Override
		protected boolean performSMS(SMS sms) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		protected boolean performMoneyTransfer(
				TransferMoneyNotification notification)
				throws MobileNetworkNotSupported {
			// TODO Auto-generated method stub
			return false;
		}
		
	}
	@Test
	public void test() throws NotEnoughMoney {
		LowCostPackage p = new LowCostPackage();
		TestPackage pp = new TestPackage();
		Package pa = p.clone();
		Package pb = pp.clone();
		
		System.out.println(pb.getPrototypeClass());
		System.out.println(pa.getPrototypeClass());
		Class<?> p2 = pa.getClass();
		Class<?> p1 = p.getClass();
		Class<?> p3 = pb.getClass();
		System.out.println(p3+"  "+p2);
		System.out.println(p2.equals(p1));
		System.out.println(p3.equals(p2));
	}

}
