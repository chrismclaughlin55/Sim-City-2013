package bank;

import java.awt.Component;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import trace.AlertLog;
import trace.AlertTag;
import bank.TellerRole.State;
import bank.interfaces.BankCustomer;
import bank.interfaces.BankManager;
import bank.utilities.CustInfo;
import city.PersonAgent;
import city.Role;

public class BankManagerRole extends Role implements BankManager {
	Writer writer;
	private String name;
	private PersonAgent me;
	private List<CustomerRole> line = Collections.synchronizedList(new ArrayList<CustomerRole>());
	public List<myTeller> tellers = Collections.synchronizedList(new ArrayList<myTeller>());
	private Map<PersonAgent, CustInfo> CustAccounts;
	private Map<String, CustInfo> BusinessAccounts;
	private boolean leave = false;
	private boolean allGone = false;
	Bank bank;
	enum tellerState {available, needsInfo, notAvailable, updateInfo, offDuty }
	class myTeller{
		public TellerRole t;
		tellerState state;
		CustomerRole c;
		CustInfo custInfo;
		myTeller(TellerRole t){
			this.t = t;
			state = tellerState.available;
		}

	}
	public BankManagerRole(PersonAgent person, Bank bank) {
		super(person);
		this.name = person.getName();
		this.me = person;
		this.bank = bank;

	}
	//MESSAGES
	public void msgAddTeller(TellerRole newRole) {
		tellers.add(new myTeller(newRole));

	}
	//Direct Deposit Message
//	public void msgDirectDeposit(PersonAgent payer, PersonAgent reciever, double payment){
//		print("recieved deposit from "+payer.getName()+" to "+reciever.getName()+" for $"+payment);
//		CustInfo send = getAccount(payer);
//		CustInfo recieve = getAccount(reciever);
//		send.moneyInAccount-=payment;
//		recieve.moneyInAccount+=payment;
//		stateChanged();
//	}
	@Override
	public void msgINeedService(CustomerRole c) {
		getLine().add((CustomerRole) c);
		stateChanged();
	}

	@Override
	public void msgGiveMeInfo(CustomerRole c, TellerRole t) {
		print(t.getName()+" wants info");
		for( myTeller mt: tellers){
			if(mt.t.equals(t)){
				mt.state = tellerState.needsInfo;
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
		bank.payPerson(bank, tellerRole.getPerson(), 150);
		print(tellerRole.getName()+" gets paid 150 for today");
		stateChanged();
	}
	//SCHEDULER
	@Override
	public boolean pickAndExecuteAnAction() {
		//		try {
		//			if(tellers.size() > 0){
		//		//		writer.write(tellers.get(0).t.getName()+" "+tellers.get(0).state +"my StateChange= "+person.stateChange.availablePermits()+"\n");
		//				continue;
		//			}
		//		} catch (IOException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}
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
		if(person.cityData.hour > Bank.CLOSINGTIME){
			leave = true;
			allGone = true;
			for( myTeller t : tellers){
				if(t.state != tellerState.offDuty)
					allGone = false;

			}
		}
		if(leave && allGone && line.size()==0){
			leave();
			return true;
		}
		return false;
	}
	//ACTIONS
	private void leave() {
		tellers.clear();
		myTeller fakeTeller = new myTeller(null);
		if(person.bankInfo.depositAmount < 0){
			if(person.bankInfo.depositAmount + person.bankInfo.moneyInAccount < 0){
				person.cash += person.bankInfo.moneyInAccount;
				person.bankInfo.moneyInAccount = 0;
				person.bankInfo.depositAmount = 0;
			}
			else{
				person.cash -= person.bankInfo.depositAmount;
				person.bankInfo.moneyInAccount += person.bankInfo.depositAmount;
				person.bankInfo.depositAmount = 0;
			}

		}else if(person.bankInfo.depositAmount > 0){
			if(person.cash - person.bankInfo.depositAmount < 0){
				person.bankInfo.moneyInAccount += person.cash;
				person.cash = 0;
				person.bankInfo.depositAmount = 0;
			}
			else{
				person.bankInfo.moneyInAccount += person.bankInfo.depositAmount;
				person.cash -=person.bankInfo.depositAmount;
				person.bankInfo.depositAmount = 0;

			}
		}
		fakeTeller.custInfo = person.bankInfo;
		updatedb(fakeTeller);
		person.bankInfo.depositAmount = 0;
		for(CustInfo info : bank.CustAccounts.values()){
			print(info.custName+" "+info.accountNumber+" "+info.moneyInAccount);
		}
		print("bank closed. Leaving");
		leave = false;
		bank.payPerson(bank, me, 300);

		AlertLog.getInstance().logMessage(AlertTag.BANK, this.name, "Leaving. I get paid 300 for today");

		AlertLog.getInstance().logMessage(AlertTag.BANK_MANAGER, this.name, "Leaving. I get paid 300 for today");
		AlertLog.getInstance().logInfo(AlertTag.BANK, bank.name, "Bank is closed");
		bank.setClosed(person);
		person.exitBuilding();
		person.msgDoneWithJob();
		doneWithRole();
	}

	private void helpCustomer(CustomerRole c, myTeller t) {
		t.c = c;
		bank.CustAccounts.put(c.getPerson(), c.getPerson().bankInfo);
		c.msgGoToTeller(t.t, tellers.indexOf(t) + 5);
		t.state = tellerState.notAvailable;
	}

	private void sendInfo(myTeller t) {
		print("sending info to "+t.t.getName());
		AlertLog.getInstance().logMessage(AlertTag.BANK_MANAGER, this.name, "Sending info to "+t.t.getName());
		if(bank.CustAccounts.get(t.c.getPerson()) != null )
			t.custInfo = bank.CustAccounts.get(t.c.getPerson());
		else t.custInfo = null;
		t.t.msgHereIsInfo(t.custInfo);
		t.state = tellerState.notAvailable;
	}

	private void updatedb(myTeller t) {
		print("updating db for "+t.custInfo.custName);
		AlertLog.getInstance().logMessage(AlertTag.BANK_MANAGER, this.name, "Updating db for "+t.custInfo.custName);
		bank.CustAccounts.put(t.custInfo.accountHolder, t.custInfo);
		t.state = tellerState.available;
			bank.bankGui.updatebankPanel();

	}
	public List<CustomerRole> getLine() {
		return line;
	}
	public CustInfo getAccount(PersonAgent person){
		CustInfo personInfo = bank.CustAccounts.get(person);
		if(personInfo == null){
			personInfo = new CustInfo(person.bankInfo);
		}
		return personInfo;
	}
	public boolean tellerPresent() {
		return !tellers.isEmpty();
	}

}









