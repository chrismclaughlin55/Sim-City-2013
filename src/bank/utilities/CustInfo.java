package bank.utilities;

import bank.CustomerRole;
import bank.interfaces.BankCustomer;
import city.PersonAgent;

public class CustInfo {
	public double moneyInAccount;
	public double depositAmount;
	public double loanRequestAmount;
	public double loanApproveAmount;
	public double loanAmount;
	public String custName;
	public int accountNumber;
	public BankCustomer customer;
	public PersonAgent accountHolder;
	
	public CustInfo(String name, PersonAgent p, BankCustomer b){
		custName = name;
		this.accountHolder = p;
		this.customer = b;
	}
}
