package com.checkpoint.mobilenetwork.core.packages;

import com.checkpoint.mobilenetwork.core.MobileNetwork;
import com.checkpoint.mobilenetwork.core.PhoneCall;
import com.checkpoint.mobilenetwork.core.SMS;
import com.checkpoint.mobilenetwork.core.Subscriber;
import com.checkpoint.mobilenetwork.core.TransferMoneyNotification;
import com.sun.org.apache.xalan.internal.xsltc.runtime.Operators;

/**
 * This class is a hybrid of two design patterns - prototype and decorator
 * It represents a Package - Packages supports different operations that are allowed
 * and supported by mobile network. As a decorator, Package is declared as abstract 
 * class. It has a set of methods, that you need to implement when creating new package.
 * There are 4 abstract protected methods:
 * 
 *  - perform() - checks if a call is enabled
 *  - performSMS() - checks if a SMS sending is available
 *  - performMoneyTransfer() - executes when you are trying to transfer money to another subscriber
 *  - chargedWithMoney() - is invoked when subscriber is charging his count 
 *  
 *  In general, package decides, is operation allowed for current producer or consumer, or not
 *  It controls that producer has enough money to perform operation, or if is current operation is allowed 
 *  for consumer's mobile operator - its functionality depends from your implementation
 *  
 *  As usual, package prototypes are stored by the MobileNetwork. When new subscriber subscribe to this Network,
 *  Network invokes clone() method to give the subscriber one of the packages. Its worth to notice, then clone()
 *  method doesn't return the instance of Package class, but returns an object of inherited class 
 *  
 * @author roman
 *
 */
public abstract class Package {
	private Package pack;
	protected String name;
	private Class<? extends Package> prototypeClass;
	
	private MobileNetwork network;
	
	protected Package() {}
	
	public Package(Integer id,MobileNetwork network) {
		this.network = network;
		this.network.addNewPackage(id, this);
	}
	
	private Package(Package pack) {
		this.pack = pack;
		this.prototypeClass = pack.getClass();
	}
	
	/**
	 * Returns the object of inherited class, that performs the same operation as Package prototype
	 * @return a package clone
	 */
	public Package clone() {
			return new Package(this) {
				@Override
				protected boolean perform(PhoneCall call) throws NotEnoughMoney {
					return getPack().perform(call); 
				}
				
				@Override
				protected void chargedWithMoney(Subscriber subscriber,float ammount) {
					getPack().chargedWithMoney(subscriber, ammount);
				}
				
				@Override
				protected boolean performSMS(SMS sms) {
					return getPack().performSMS(sms);
				}
				
				@Override
				protected boolean performMoneyTransfer(TransferMoneyNotification notification) throws MobileNetworkNotSupported {
					return getPack().performMoneyTransfer(notification);
				}
		};
	}
	
	public Package getPack() {
		return pack;
	}

	/**
	 * Checks if the call is available for producer and consumer
	 * 
	 * @param call - call you need to perform
	 * @return is call available?
	 * @throws NotEnoughMoney - when producer has not enough money
	 */
	protected abstract boolean perform(PhoneCall call) throws NotEnoughMoney;

	/**
	 * Method that invokes when subscriber charges his count
	 * 
	 * @param subscriber - subscriber that charges his count
	 * @param ammount - amount of money subscriber wants to charge
	 */
	protected abstract void chargedWithMoney(Subscriber subscriber, float ammount);
	
	/**
	 * Checks if sms is able to be sent
	 * 
	 * @param sms that would be send
	 * @return
	 */
	protected abstract boolean performSMS(SMS sms);
	
	/**
	 * invokes when subscriber transfers money to another one
	 * 
	 * @param notification information about money transfer
	 * @return is transfer available
	 * @throws MobileNetworkNotSupported when consumer you want to transfer money to doesn't belongs to your network
	 */
	protected abstract boolean performMoneyTransfer(TransferMoneyNotification notification) throws MobileNetworkNotSupported;
	
	/**
	 * Invokes chargedWithMoney()
	 * @param s
	 * @param ammount
	 */
	public void charged(Subscriber s, float ammount) {
		this.pack.chargedWithMoney(s, ammount);
	}
	
	/**
	 * Invokes perform()
	 * @param call
	 * @return is call enabled
	 * @throws NotEnoughMoney
	 */
	public boolean performCall(PhoneCall call) throws NotEnoughMoney {	
		return this.pack.perform(call);
	}
	
	/**
	 * Invokes performSMSNotification
	 * 
	 * @param sms
	 * @return is sms sending enabled
	 */
	public boolean performSMSNotification(SMS sms) {
		return this.pack.performSMS(sms);
	}
	
	/**
	 * Invokes performMoneyTransferOperation()
	 * 
	 * @param notification
	 * @return is Money transfer enabled
	 * @throws MobileNetworkNotSupported
	 */
	public boolean performMoneyTransferOperation(TransferMoneyNotification notification) throws MobileNetworkNotSupported {
		return this.pack.performMoneyTransfer(notification);
	}
	
	public void setPack(Package p) {
		this.pack = p;
	}
	
	public MobileNetwork getNetwork() {
		return network;
	}

	public void setNetwork(MobileNetwork network) {
		this.network = network;
	}

	public Class<? extends Package> getPrototypeClass() {
		return prototypeClass;
	}

	@Override
	public String toString(){ 
		return this.getClass().getName();
	}
	
	public class NotEnoughMoney extends Exception {
		private static final long serialVersionUID = 1L;	
	}
	
	public class MobileNetworkNotSupported extends Exception {
		private static final long serialVersionUID = 8094987436009542335L;
	}
}
