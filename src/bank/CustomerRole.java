package bank;

import java.util.concurrent.Semaphore;

import bank.interfaces.BankCustomer;
import bank.interfaces.Teller;
import bank.utilities.CustInfo;
import bankgui.BankCustomerGui;
import city.PersonAgent;
import city.Role;

public class CustomerRole extends Role implements BankCustomer{
	//DATA
	private PersonAgent me;
	private double cash;
	private String name;
	private Teller t;
	private int position;
	CustInfo myInfo;
	enum CustState { InLine, AtTeller, AskedForLoan, SentDeposit, Left, ProcessLoan};
	enum CustEvent { GoToTeller, AskedWhatToDo, RecievedMoney, Done, RecievedLoanInfo};
	private CustState state;
	private CustEvent event;
	private BankCustomerGui gui;
	private Semaphore atDest = new Semaphore(0, true);
	public CustomerRole(PersonAgent person) {
		super(person);
		this.state = CustState.InLine;
		this.cash = person.cash;
		this.name = person.getName();
		this.me = person;
		person.bankInfo.customer = this;
		this.myInfo = person.bankInfo;
	}
	//GUI MESSAGES
	public void msgAddGui(BankCustomerGui custGui) {
		this.gui = custGui;
	}
	//MESSAGES
	@Override
	public void msgGoToTeller(Teller t, int pos) {
		this.t = t;
		event = CustEvent.GoToTeller;
		position = pos;
		print("going to teller");
		stateChanged();
	}

	@Override
	public void msgWhatWouldYouLike() {
		event = CustEvent.AskedWhatToDo;
		print("recieved asked What to do");
		stateChanged();

	}

	@Override
	public void msgHaveANiceDay(double amount) {
		person.cash+=amount;
		print("total cash: "+ person.cash);
		print("total money in bank "+ this.myInfo.moneyInAccount);
		event = CustEvent.Done;
		print("recieved have nice day");
		stateChanged();

	}

	@Override
	public void msgCanDoThisAmount(double approvedAmount) {
		event = CustEvent.RecievedLoanInfo;
		stateChanged();
	}

	//SCHEDULER
	public boolean pickAndExecuteAnAction() {
		if(state == CustState.InLine && event == CustEvent.GoToTeller){
			sayHello();
			return true;
		}
		if(state == CustState.AtTeller && event == CustEvent.AskedWhatToDo){
			tellTeller();
			return true;
		}
		if(state == CustState.SentDeposit && event == CustEvent.Done){
			leave();
			return true;
		}
		if(state == CustState.AskedForLoan && event == CustEvent.RecievedLoanInfo){
			processLoan(myInfo.loanApproveAmount);
			return true;
		}
		if(state == CustState.ProcessLoan && event == CustEvent.Done){
			leave();
			return true;
		}

		return false;
	}


	//ACTIONS
	private void sayHello(){
		guiGoHere(4);
		guiGoHere(position);
		CustInfo tmp = new CustInfo(myInfo);
		this.t.msgHello(tmp);
		state = CustState.AtTeller;

	}
	private void tellTeller(){
		//		if(myInfo.loanRequestAmount == 0){
		//			t.msgloan(myInfo.loanRequestAmount);
		//			state = CustState.AskedForLoan;
		//			return;
		//		}else{
		/*
			double depositAmount;
			if(myInfo.depositAmount>cash){
				depositAmount = cash;
				cash = 0;
				state = CustState.SentDeposit;
			}
			else{
				depositAmount = myInfo.depositAmount;
				cash-=myInfo.depositAmount;
				state = CustState.SentDeposit;
			}
			t.msgDeposit(depositAmount);
			print("depositing $"+depositAmount);
			print("should be depositing $"+myInfo.depositAmount);
			print("person wants to deposit "+this.person.bankInfo.depositAmount);*/
		print("depositing $"+myInfo.depositAmount);
		if(myInfo.depositAmount < 0){
			if(myInfo.depositAmount + myInfo.moneyInAccount < 0){
				this.cash += person.bankInfo.moneyInAccount;
				myInfo.moneyInAccount = 0;
				t.msgDeposit(myInfo.depositAmount);
				myInfo.depositAmount = 0;
			}
			else{
				person.cash -= myInfo.depositAmount;
				myInfo.moneyInAccount += myInfo.depositAmount;
				t.msgDeposit(myInfo.depositAmount);
				myInfo.depositAmount = 0;
			}

		}else if(myInfo.depositAmount > 0){
			if(person.cash - myInfo.depositAmount < 0){
				myInfo.moneyInAccount += person.cash;
				person.cash = 0;
				t.msgDeposit(myInfo.depositAmount);
				myInfo.depositAmount = 0;
			}
			else{
				myInfo.moneyInAccount += myInfo.depositAmount;
				person.cash -=myInfo.depositAmount;
				t.msgDeposit(myInfo.depositAmount);
				myInfo.depositAmount = 0;

			}
		}
	
	}

	private void leave(){

		state = CustState.Left;	
		guiGoHere(9);
		gui.setPresent(false);
		person.bankInfo = this.myInfo;
		person.exitBuilding();
		person.msgDoneWithJob();
		doneWithRole();	
	}
	private void processLoan(double approvedAmount){
		double requestAmount = approvedAmount;  
		if(approvedAmount< .75 * myInfo.loanRequestAmount)
			requestAmount = 0;

		state = CustState.ProcessLoan;
		t.msgITakeIt(requestAmount);
		myInfo.loanAmount = requestAmount;
		myInfo.loanRequestAmount = 0;
	}
	@Override
	public PersonAgent returnPerson() {
		return this.person;
	}

	public void msgGuiIsAtDest() {
		atDest.release();


	}

	private void guiGoHere(int place){
		if(gui != null)
			gui.goTo(place);
		try {
			atDest.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}


