package bank;

import java.util.concurrent.Semaphore;

import trace.AlertLog;
import trace.AlertTag;
import bank.gui.BankCustomerGui;
import bank.interfaces.BankCustomer;
import bank.interfaces.Teller;
import bank.utilities.CustInfo;
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
		print("event: "+ event+" state: "+state);
		stateChanged();
	}

	@Override
	public void msgWhatWouldYouLike() {
		event = CustEvent.AskedWhatToDo;
		print("recieved asked What to do");
		print("event: "+ event+" state: "+state);
		stateChanged();

	}

	@Override
	public void msgHaveANiceDay(double amount) {
		person.cash+=amount;
		print("total cash: "+ person.cash);
		print("total money in bank "+ this.myInfo.moneyInAccount);
		event = CustEvent.Done;
		print("recieved have nice day");
		print("event: "+ event+" state: "+state);
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
		print("say hello");
		CustInfo tmp = new CustInfo(myInfo);
			state = CustState.AtTeller;
			this.t.msgHello(tmp);
	

	}
	private void tellTeller(){

		print("depositing $"+myInfo.depositAmount);
		AlertLog.getInstance().logMessage(AlertTag.BANK_CUSTOMER, this.name, "Depositing $"+myInfo.depositAmount);
		if(myInfo.depositAmount < 0){
			if(myInfo.depositAmount + myInfo.moneyInAccount < 0){
				this.cash += person.bankInfo.moneyInAccount;
				myInfo.moneyInAccount = 0;
				t.msgDeposit(myInfo.depositAmount);
				myInfo.depositAmount = 0;
				state = CustState.SentDeposit;
			}
			else{
				person.cash -= myInfo.depositAmount;
				myInfo.moneyInAccount += myInfo.depositAmount;
				t.msgDeposit(myInfo.depositAmount);
				myInfo.depositAmount = 0;
				state = CustState.SentDeposit;
			}

		}else if(myInfo.depositAmount > 0){
			if(person.cash - myInfo.depositAmount < 0){
				myInfo.moneyInAccount += person.cash;
				person.cash = 0;
				t.msgDeposit(myInfo.depositAmount);
				myInfo.depositAmount = 0;
				state = CustState.SentDeposit;
			}
			else{
				myInfo.moneyInAccount += myInfo.depositAmount;
				person.cash -=myInfo.depositAmount;
				t.msgDeposit(myInfo.depositAmount);
				myInfo.depositAmount = 0;
				state = CustState.SentDeposit;

			}
		}
	
	}

	private void leave(){
		AlertLog.getInstance().logMessage(AlertTag.BANK_CUSTOMER, this.name, "Leaving the bank");
		state = CustState.Left;	
		guiGoHere(9);
		gui.setPresent(false);
		person.bankInfo = this.myInfo;
		doneWithRole();
		person.msgDoneWithJob();
		person.exitBuilding();
	}
	private void processLoan(double approvedAmount){
		AlertLog.getInstance().logMessage(AlertTag.BANK_CUSTOMER, this.name, "Asking for loan");
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


