package bank;

import java.util.ArrayList;
import java.util.Queue;

import restaurantMQ.interfaces.Customer;
import bank.interfaces.BankCustomer;
import bank.interfaces.BankManager;
import bank.utilities.CustInfo;
import city.PersonAgent;
import city.Role;

public class BankManagerRole extends Role implements BankManager {
	Queue<Customer> line;
	ArrayList<myTeller> tellers;
	enum tellerState {available, needsInfo, notAvailable, updateInfo }
	class myTeller{
		tellerState state;
		CustInfo c;
		
		
	}
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
