package bank.interfaces;

import city.PersonAgent;
import bank.utilities.CustInfo;



public interface Teller {
public abstract void msgHello(String name, BankCustomer c);
public abstract void msgHereIsInfo(CustInfo info);
public abstract void msgDeposit(double money);
public abstract void loan(double amount);
public abstract void msgITakeIt(double loanAmount);


}
