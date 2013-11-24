package bank;


import city.*;

import city.PersonAgent;
import city.Role;


public class TellerRole extends Role implements Teller{


	public TellerRole(PersonAgent person) {
		super(person);
		// TODO Auto-generated constructor stub
	}
	
	//MESSAGES
	@Override
	public void msgHello(String name, BankCustomer c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsInfo(CustInfo info) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgDeposit(double money) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loan(double amount) {
		// TODO Auto-generated method stub
		
	}
	
	
	//SCHEDULER
	@Override
	public boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}

}
