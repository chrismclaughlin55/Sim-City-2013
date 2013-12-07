package bank.utilities;

import bank.CustomerRole;
import bank.interfaces.BankCustomer;
import city.PersonAgent;

public class CustInfo {
	public double moneyInAccount = 0;
	public double depositAmount = 0;
	public double loanRequestAmount = 0;
	public double loanApproveAmount = 0;
	public double loanAmount = 0;
	public String custName;
	public int accountNumber = 0;
	public static int accountNumberCounter = 0;
	public CustomerRole customer;
	public PersonAgent accountHolder;
	
	public CustInfo(String name, PersonAgent p, CustomerRole b){
		this.custName = name;
		this.accountHolder = p;
		this.customer = b;
		this.accountNumber = accountNumberCounter;
		accountNumberCounter++;
		this.moneyInAccount = 200;
		
	}
	public CustInfo(CustInfo c){
		this.moneyInAccount = c.moneyInAccount;
		this.depositAmount = c.depositAmount;
		this.accountHolder = c.accountHolder;
		this.loanAmount = c.loanAmount;
		this.loanRequestAmount = c.loanRequestAmount;
		this.loanApproveAmount = c.loanApproveAmount;
		this.custName = c.custName;
		this.accountNumber = c.accountNumber;
		this.customer = c.customer;
	}
}
