package bank;

import bank.interfaces.BankCustomer;
import bank.interfaces.BankManager;
import bank.utilities.CustInfo;
import city.PersonAgent;
import city.Role;

public class BankManagerRole extends Role implements BankManager {

	public BankManagerRole(PersonAgent person) {
		super(person);
		// TODO Auto-generated constructor stub
	}
	
	//MESSAGES
	@Override
	public void msgINeedService(BankCustomer c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgGiveMeInfo(PersonAgent c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgUpdateInfo(CustInfo info) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}

	
}
