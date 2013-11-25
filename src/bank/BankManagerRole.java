package bank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import restaurantMQ.interfaces.Customer;
import bank.interfaces.BankCustomer;
import bank.interfaces.BankManager;
import bank.utilities.CustInfo;
import city.PersonAgent;
import city.Role;

public class BankManagerRole extends Role implements BankManager {
	private String name;
	private PersonAgent me;
	private List<Customer> line = Collections.synchronizedList(new ArrayList<Customer>());
	private List<myTeller> tellers = Collections.synchronizedList(new ArrayList<myTeller>());
	private Map<PersonAgent, CustInfo> CustAccounts;
	private Map<String, CustInfo> BusinessAccounts;
	enum tellerState {available, needsInfo, notAvailable, updateInfo }
	class myTeller{
		tellerState state;
		CustInfo c;
		
		
	}
	public BankManagerRole(PersonAgent person) {
		super(person);
		this.name = person.getName();
		this.me = person;
		CustAccounts = Collections.synchronizedMap(new HashMap<PersonAgent, CustInfo>());
		BusinessAccounts = Collections.synchronizedMap(new HashMap<String, CustInfo>());
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
