package bank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import bank.interfaces.BankCustomer;
import bank.interfaces.BankManager;
import bank.utilities.CustInfo;
import city.PersonAgent;
import city.Role;

public class BankManagerRole extends Role implements BankManager {
	private String name;
	private PersonAgent me;
	private List<CustomerRole> line = Collections.synchronizedList(new ArrayList<CustomerRole>());
	private List<myTeller> tellers = Collections.synchronizedList(new ArrayList<myTeller>());
	private Map<PersonAgent, CustInfo> CustAccounts;
	private Map<String, CustInfo> BusinessAccounts;
	enum tellerState {available, needsInfo, notAvailable, updateInfo }
	class myTeller{
		TellerRole t;
		tellerState state;
		CustomerRole c;
		CustInfo custInfo;
		
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
	public void msgINeedService(CustomerRole c) {
		line.add((CustomerRole) c);
		stateChanged();
	}

	@Override
	public void msgGiveMeInfo(CustomerRole c, TellerRole t) {
		for( myTeller mt: tellers){
			if(mt.t.equals(t))
		mt.state = tellerState.needsInfo;
			mt.c = c;
		}
		
	}

	@Override
	public void msgUpdateInfo(CustInfo info, TellerRole t) {
		for(myTeller mt: tellers){
			if(mt.t.equals(t))
				mt.state = tellerState.updateInfo;
			
		}
		
	}
//SCHEDULER
	@Override
	public boolean pickAndExecuteAnAction() {
		for( myTeller t: tellers){
			if(t.state == tellerState.available){
				helpCustomer(line.remove(0), t);
			return true;
		}
		}
		for(myTeller t: tellers){
			if(t.state == tellerState.needsInfo){
				sendInfo(t);
				return true;
			}
		}
		for(myTeller t: tellers){
			updatedb(t);
			return true;
		}
		
		
		return false;
	}
//ACTIONS
	private void helpCustomer(CustomerRole c, myTeller t) {
		t.c = c;
		c.msgGoToTeller(t.t);
		t.state = tellerState.notAvailable;
	}
	
	private void sendInfo(myTeller t) {
		if(CustAccounts.get(t.c.getPerson()) != null)
			t.custInfo = CustAccounts.get(t.c.getPerson());
		else 
			t.custInfo = new CustInfo(t.c.getName(), t.c.getPerson(), t.c);
		t.state = tellerState.notAvailable;
	}
	
	private void updatedb(myTeller t) {
		// TODO Auto-generated method stub
		
	}

	

	
	
	
}
