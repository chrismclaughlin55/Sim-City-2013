package bank.interfaces;

import city.PersonAgent;

public interface BankCustomer {
	public abstract void msgGoToTeller(Teller t);
	public abstract void msgWhatWouldYouLike();
	public abstract void msgHaveANiceDay(double amount);
	public abstract void msgCanDoThisAmount(double approvedAmount);
	public abstract PersonAgent returnPerson();
}
