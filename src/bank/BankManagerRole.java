package bank;

import java.awt.Component;
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
	enum tellerState {available, needsInfo, notAvailable, updateInfo, offDuty }
	class myTeller{
		TellerRole t;
		tellerState state;
		CustomerRole c;
		CustInfo custInfo;
		myTeller(TellerRole t){
			this.t = t;
			state = tellerState.available;
		}

	}
	public BankManagerRole(PersonAgent person) {
		super(person);
		this.name = person.getName();
		this.me = person;
		CustAccounts = Collections.synchronizedMap(new HashMap<PersonAgent, CustInfo>());
		BusinessAccounts = Collections.synchronizedMap(new HashMap<String, CustInfo>());
	}
	//MESSAGES
	public void msgAddTeller(TellerRole newRole) {
		tellers.add(new myTeller(newRole));

	}
	//Direct Deposit Message
	public void msgDirectDeposit(PersonAgent payer, PersonAgent reciever, double payment){

		CustInfo send = getAccount(payer);
		CustInfo recieve = getAccount(reciever);
		send.moneyInAccount-=payment;
		recieve.moneyInAccount+=payment;
		stateChanged();
	}
	@Override
	public void msgINeedService(CustomerRole c) {
		getLine().add((CustomerRole) c);

		stateChanged();
	}

	@Override
	public void msgGiveMeInfo(CustomerRole c, TellerRole t) {
		for( myTeller mt: tellers){
			if(mt.t.equals(t)){
				mt.state = tellerState.needsInfo;

				print("sent info for "+c.getName());

				mt.c = c;
			}
		}
		stateChanged();

	}

	@Override
	public void msgUpdateInfo(CustInfo info, TellerRole t) {
		for(myTeller mt: tellers){
			if(mt.t.equals(t)){
				mt.state = tellerState.updateInfo;
				print("recieved update info for "+t.currentCustInfo.custName);

			}
		}

		stateChanged();
	}
	public void msgLeavingNow(TellerRole tellerRole) {
		synchronized(tellers){ 
			for(myTeller t: tellers){
				if(t.t.equals(tellerRole)){
					t.state = tellerState.offDuty;

				}
			}
		}
		stateChanged();
	}
	//SCHEDULER
	@Override
	public boolean pickAndExecuteAnAction() {
		for( myTeller t: tellers){
			if(t.state == tellerState.available && getLine().size()>0){
				helpCustomer(getLine().remove(0), t);
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
			if(t.state == tellerState.updateInfo){
				updatedb(t);
				return true;
			}
		}
		for(myTeller t: tellers){
			if(t.state == tellerState.offDuty){
				tellers.remove(t);
				return true;
			}
		}
		if(person.cityData.hour > Bank.CLOSINGTIME + 1 && tellers.size() == 0 && line.size()==0){
			leave();
			return true;
		}
		return false;
	}
	//ACTIONS
	private void leave() {
		person.exitBuilding();
		person.msgDoneWithJob();
		doneWithRole();
	}

	private void helpCustomer(CustomerRole c, myTeller t) {
		t.c = c;
		CustAccounts.put(c.getPerson(), c.getPerson().bankInfo);
		c.msgGoToTeller(t.t);
		t.state = tellerState.notAvailable;
	}

	private void sendInfo(myTeller t) {
		if(CustAccounts.get(t.c.getPerson()) != null )
			t.custInfo = CustAccounts.get(t.c.getPerson());
		else t.custInfo = null;
		t.t.msgHereIsInfo(t.custInfo);
	}

	private void updatedb(myTeller t) {
		print("made it to update database");
		CustAccounts.put(t.custInfo.accountHolder, t.custInfo);
		t.state = tellerState.available;

	}
	public List<CustomerRole> getLine() {
		return line;
	}
	public CustInfo getAccount(PersonAgent person){
		CustInfo personInfo = CustAccounts.get(person);
		if(personInfo == null){
			personInfo = new CustInfo(person.bankInfo);
		}
		return personInfo;
	}
	public List getTellers() {
		return tellers;
	}

}









