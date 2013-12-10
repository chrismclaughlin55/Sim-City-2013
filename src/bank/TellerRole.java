package bank;

import java.util.concurrent.Semaphore;

import trace.AlertLog;
import trace.AlertTag;
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
	boolean wantToLeave = false;
	private TellerGui gui;
	private boolean bankRobbery = false;
	private BankRobber bankRobber;

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
	}
	public void msgWaitForGui(){
		try {
			atHome.acquire();
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
	}
	
	public void msgStickEmUp(BankRobber br) {
		bankRobbery = true;
		bankRobber = br;
		stateChanged();
	}

	//MESSAGES
	public void msgAddManager(BankManagerRole bm){
		this.bm = bm;
		stateChanged();
	}
	@Override
	public void msgHello(CustInfo c) {
		currentCustInfo = c;
		event = Event.recievedHello;
		stateChanged();
	}

	@Override
	public void msgHereIsInfo(CustInfo info) {
		event = Event.recievedInfo;
		if(info != null){
			this.currentCustInfo.moneyInAccount = info.moneyInAccount;
			this.currentCustInfo.loanApproveAmount = info.loanApproveAmount;
			this.currentCustInfo.loanAmount = info.loanAmount;
		}

		stateChanged();
	}

	@Override
	public void msgDeposit(double money) {
		currentCustInfo.depositAmount = 0;
		currentCustInfo.moneyInAccount += money;
		event = Event.recievedDeposit;
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
		if (bankRobbery) {
			payTheMan();
			return true;
		}
		
		if(state == State.available && event == Event.recievedHello){
			getInfo();
			return true;
		}
		if(state == State.waitingForInfo && event == Event.recievedInfo){
			ask();
			return true;
		}
		if(state == State.waitingForResponse && event == Event.recievedDeposit){
			print("made it to sched procOrd");
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

		if(person.cityData.hour > Bank.CLOSINGTIME){
			wantToLeave = true;
		}
		if(wantToLeave && bm.getLine().size() == 0){
			this.guiGoHere(9);
		}

		return false;
	}
	//ACTIONS
	private void ask() {
		currentCustInfo.customer.msgWhatWouldYouLike();
		state = State.waitingForResponse;
		AlertLog.getInstance().logMessage(AlertTag.BANK, this.name, "Serving "+currentCustInfo.custName);
		AlertLog.getInstance().logMessage(AlertTag.BANK_TELLER, this.name, "Serving "+currentCustInfo.custName);
	}

	private void getInfo() {
		AlertLog.getInstance().logMessage(AlertTag.BANK, this.name, "Getting info for "+currentCustInfo.custName);
		AlertLog.getInstance().logMessage(AlertTag.BANK_TELLER, this.name, "Getting info for "+currentCustInfo.custName);
		bm.msgGiveMeInfo(currentCustInfo.customer, this);
		state = State.waitingForInfo;
	}

	private void makeAvailable() {	
		currentCustInfo = null;
		state = State.available;

	}

	private void payTheMan() {
		System.err.println("IM BEING ROBBED");
		AlertLog.getInstance().logError(AlertTag.BANK, this.name, "IM BEING ROBBED");
		AlertLog.getInstance().logError(AlertTag.BANK_TELLER, this.name, "IM BEING ROBBED");
		bankRobber.msgPleaseDontShoot(400);
		bankRobbery = false;
	}
	
	private void processLoan() {
		AlertLog.getInstance().logMessage(AlertTag.BANK, this.name, "Processing loan for "+currentCustInfo.custName);
		AlertLog.getInstance().logMessage(AlertTag.BANK_TELLER, this.name, "Processing loan for "+currentCustInfo.custName);
		if(currentCustInfo.loanRequestAmount>currentCustInfo.loanApproveAmount)
			currentCustInfo.customer.msgCanDoThisAmount(currentCustInfo.loanApproveAmount);
		else 
			currentCustInfo.customer.msgCanDoThisAmount(currentCustInfo.loanRequestAmount);
		state = State.customerDecidingLoan;
	}

	private void processOrder() {
		//
		print("processing order for "+currentCustInfo.custName);
		AlertLog.getInstance().logMessage(AlertTag.BANK, this.name, "Processing order for "+currentCustInfo.custName);
		AlertLog.getInstance().logMessage(AlertTag.BANK_TELLER, this.name, "Processing order for "+currentCustInfo.custName);
		bm.msgUpdateInfo(currentCustInfo, this);
		currentCustInfo.customer.msgHaveANiceDay(currentCustInfo.depositAmount);
		state = State.doneWithCustomer;
		event = Event.updatedBank;
	}
	public void msgGuiIsAtDest() {
		atDest.release();

	}

	private void guiGoHere(int place) {
		if(place == 9)
			bm.msgLeavingNow(this);
		gui.goTo(place);
		try
		{
			atDest.acquire();
		}
		catch(Exception e){}
		if(place == 9){
			gui.setPresent(false);
			person.exitBuilding();
			person.msgDoneWithJob();
			doneWithRole();
		}
	}


}
