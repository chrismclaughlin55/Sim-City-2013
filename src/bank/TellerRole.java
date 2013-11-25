package bank;

import bank.interfaces.BankCustomer;
import bank.interfaces.Teller;
import bank.utilities.CustInfo;
import bankgui.TellerGui;
import city.PersonAgent;
import city.Role;
/*
 *  
 */
public class TellerRole extends Role implements Teller{
	String name;
	PersonAgent me;
	BankManagerRole bm;
	CustInfo currentCustInfo;
	enum State {available, waitingForInfo, waitingForResponse, doneWithCustomer, customerDecidingLoan}
	enum Event {none, recievedHello, recievedInfo, recievedDeposit,updatedBank,loanReq, iTakeIt}
	State state;
	Event event;
	private TellerGui gui;
	
	//Constructor
	public TellerRole(PersonAgent person) {
		super(person);
		this.name = person.getName();
		this.me = person;
		state = State.available;
		event = Event.none;
		// TODO Auto-generated constructor stub
	}
	//GUI messages
	public void msgAddGui(TellerGui tellerGui) {
		this.gui = tellerGui;
		
	}
	//MESSAGES
	@Override
	public void msgHello(String name, BankCustomer c) {
		currentCustInfo = new CustInfo(name, c.returnPerson(), c );
		event = Event.recievedHello;
		stateChanged();
	}

	@Override
	public void msgHereIsInfo(CustInfo info) {
		event = Event.recievedInfo;
		this.currentCustInfo = info;
		stateChanged();
	}

	@Override
	public void msgDeposit(double money) {
		currentCustInfo.depositAmount = money;
		currentCustInfo.moneyInAccount += money;
		event = Event.recievedDeposit;
		stateChanged();
	}

	@Override
	public void loan(double amount) {
		currentCustInfo.loanRequestAmount = amount;
		event = Event.loanReq;
		stateChanged();
	}
	@Override
	public void msgITakeIt(double loanAmount) {
		currentCustInfo.loanAmount = loanAmount;
		currentCustInfo.loanApproveAmount-= loanAmount;
		event = Event.iTakeIt;
		stateChanged();
		
	}
	
	
	//SCHEDULER
	@Override
	public boolean pickAndExecuteAnAction() {
		if(state == State.available && event == Event.recievedHello){
			getInfo();
			return true;
		}
		if(state == State.waitingForInfo && event == Event.recievedInfo){
			ask();
			return true;
		}
		if(state == State.waitingForResponse && event == Event.recievedDeposit){
			processOrder();
			return true;
		}
		if(state == State.doneWithCustomer && event == Event.updatedBank){
			makeAvailable();
			return true;
		}
		if(state == State.waitingForResponse && event == Event.loanReq){
			processLoan();
			return true;
		}
		if(state == State.customerDecidingLoan && event == Event.iTakeIt){
			processOrder();
			return true;
		}
			
		return false;
	}
	//ACTIONS
	private void ask() {
		state = State.waitingForResponse;
		currentCustInfo.customer.msgWhatWouldYouLike();
	}

	private void getInfo() {
		state = State.waitingForInfo;
		bm.msgGiveMeInfo(currentCustInfo.accountHolder);
		
	}

	private void makeAvailable() {	
		//TODO may need to change logic
		currentCustInfo = null;
		state = State.available;
		
	}

	private void processLoan() {
		if(currentCustInfo.loanRequestAmount>currentCustInfo.loanApproveAmount)
			currentCustInfo.customer.msgCanDoThisAmount(currentCustInfo.loanApproveAmount);
		else 
			currentCustInfo.customer.msgCanDoThisAmount(currentCustInfo.loanRequestAmount);
		state = State.customerDecidingLoan;
	}

	private void processOrder() {
		//TODO this could cause problems. could lose semaphore by updating event in action
		bm.msgUpdateInfo(currentCustInfo);
		state = State.doneWithCustomer;
		event = Event.updatedBank;
	}



	

}
