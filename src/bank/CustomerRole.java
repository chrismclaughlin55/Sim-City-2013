package bank;

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
	CustInfo myInfo;
	enum CustState { InLine, AtTeller, AskedForLoan, SentDeposit, Left, ProcessLoan};
	enum CustEvent { GoToTeller, AskedWhatToDo, RecievedMoney, Done, RecievedLoanInfo};
	private CustState state;
	private CustEvent event;
	private BankCustomerGui gui;
	public CustomerRole(PersonAgent person) {
		super(person);
		//this.myInfo = person.bankInfo;
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
	public void msgGoToTeller(Teller t) {
		this.t = t;
		event = CustEvent.GoToTeller;
		print("going to teller");
		stateChanged();
	}

	@Override
	public void msgWhatWouldYouLike() {
		event = CustEvent.AskedWhatToDo;

	}

	@Override
	public void msgHaveANiceDay(double amount) {
		person.cash+=amount;
		event = CustEvent.Done;

	}

	@Override
	public void msgCanDoThisAmount(double approvedAmount) {
		event = CustEvent.RecievedLoanInfo;
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
		//TODO GUI SHIT
		this.t.msgHello(new CustInfo(myInfo));
		state = CustState.AtTeller;

	}
	private void tellTeller(){
		if(myInfo.loanRequestAmount>0){
			t.msgloan(myInfo.loanRequestAmount);
			state = CustState.AskedForLoan;
			return;
		}else{
			double depositAmount;
			if(myInfo.depositAmount>cash){
				depositAmount = cash;
				cash = 0;
			}
			else{
				depositAmount = myInfo.depositAmount;
				cash-=myInfo.depositAmount;
			}
			t.msgDeposit(depositAmount);

		}
	}
	private void leave(){
		//TODO GUI SHIT
		state = CustState.Left;	
		// make instance of CustInfo

	}
	private void processLoan(double approvedAmount){
		/* double requestAmount = loanAmount;
		 * if(loanAmount < .75 * approvedAmount)
		 * requestAmount = 0;
		 *  
		 */
		state = CustState.ProcessLoan;
		//TODO t.msgITakeIt(requestAmount);
	}
	@Override
	public PersonAgent returnPerson() {
		// TODO Auto-generated method stub
		return this.person;
	}

}


