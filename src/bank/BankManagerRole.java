package bank;


import city.PersonAgent;
import city.Role;


public class BankManagerRole extends Role implements BankManager {

	public BankManagerRole(PersonAgent person) {
		super(person);
		// TODO Auto-generated constructor stub
	}
	
	//MESSAGES
	@Override
	public void msgINeedServer(BankCustomer c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgGiveMeInfo(BankCustomer c) {
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
