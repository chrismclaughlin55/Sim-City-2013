package bank.interfaces;

import city.PersonAgent;
import bank.CustomerRole;
import bank.TellerRole;
import bank.utilities.CustInfo;

public interface BankManager {
public abstract void msgINeedService(CustomerRole c);
public abstract void msgUpdateInfo(CustInfo info, TellerRole t);
public abstract void msgGiveMeInfo(CustomerRole c, TellerRole t);
}
