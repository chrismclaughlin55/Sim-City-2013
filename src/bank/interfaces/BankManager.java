package bank.interfaces;

import city.PersonAgent;
import bank.utilities.CustInfo;

public interface BankManager {
public abstract void msgINeedService(BankCustomer c);
public abstract void msgUpdateInfo(CustInfo info);
public abstract void msgGiveMeInfo(PersonAgent c);
}
