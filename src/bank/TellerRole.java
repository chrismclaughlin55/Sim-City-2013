package bank;

import java.util.concurrent.Semaphore;

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

	private Semaphore atDest = new Semaphore(0 ,true);

	private Semaphore atHome = new Semaphore(0, true);


	//Constructor
	public TellerRole(PersonAgent person) {
		super(person);
		this.name = person.getName();
		this.me = person;
		state = State.available;
		event = Event.none;
	}
	//GUI messages
	public void msgAddGui(TellerGui tellerGui) {
		this.gui = tellerGui;
		if(gui != null)
			print("gui added");
		else
			print("gui doesnt add");
	}
	public void msgWaitForGui(){
		try {
			atHome.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//MESSAGES
	public void msgAddManager(BankManagerRole bm){
		this.bm = bm;
		stateChanged();
	}
	@Override
	public void msgHello(CustInfo c) {
		currentCustInfo = c;
		if(currentCustInfo != null)
		print(c.custName + " said hello");
		else print("currentCustInfo is null");
		event = Event.recievedHello;
		stateChanged();
	}

	@Override
	public void msgHereIsInfo(CustInfo info) {
		event = Event.recievedInfo;

//TODO Problem here
		if(info != null)
			this.currentCustInfo = info;
		else{ 
			this.currentCustInfo = person.bankInfo;
			print("recieved info for "+ currentCustInfo.custName);
		}

		stateChanged();
	}

	@Override
	public void msgDeposit(double money) {
		currentCustInfo.depositAmount = 0;
		currentCustInfo.moneyInAccount += money;
		event = Event.recievedDeposit;
		print("recieved deposit "+ money);
		print("account now has " + currentCustInfo.moneyInAccount);
		stateChanged();
	}

	@Override
	public void msgloan(double amount) {
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

		if(person.cityData.hour > Bank.CLOSINGTIME && bm.getLine().size()==0){
			this.leaveBank();
			return true;
		}

		return false;
	}
	//ACTIONS
	private void ask() {
		print("asked what to do");
		currentCustInfo.customer.msgWhatWouldYouLike();
		state = State.waitingForResponse;
	}

	private void getInfo() {
		print("asking for info from manager");
		bm.msgGiveMeInfo(currentCustInfo.customer, this);
		state = State.waitingForInfo;
	}

	private void makeAvailable() {	
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
		bm.msgUpdateInfo(currentCustInfo, this);
		currentCustInfo.customer.msgHaveANiceDay(currentCustInfo.depositAmount);
		state = State.doneWithCustomer;
		event = Event.updatedBank;
	}
	public void msgGuiIsAtDest() {
		print("released atDest");
		atDest.release();

	}

	private void leaveBank() {
		bm.msgLeavingNow(this);
		gui.DoLeaveBank();
		try
		{
			atDest.acquire();
		}
		catch(Exception e){}
		gui.setPresent(false);
		person.exitBuilding();
		person.msgDoneWithJob();
		doneWithRole();	
	}


}
