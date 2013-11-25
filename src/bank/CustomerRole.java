package bank;

import bank.interfaces.BankCustomer;
import bank.interfaces.Teller;
import bank.utilities.CustInfo;
import city.PersonAgent;
import city.Role;

public class CustomerRole extends Role implements BankCustomer{
	//DATA
	private PersonAgent person;
	private double money;
	private String name;
	private Teller t;
	CustInfo myInfo;
	enum CustState { InLine, AtTeller, AskedForLoan, SentDeposit, Left, ProcessLoan};
	enum CustEvent { GoToTeller, AskedWhatToDo, RecievedMoney, Done, RecievedLoanInfo};
	CustState state;
	CustEvent event;
	public CustomerRole(PersonAgent person) {
		super(person);
		this.money = person.bankMoney;
		this.name = person.getName();
		this.person = person;
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
		// TODO Auto-generated method stub

	}

	@Override
	public void msgHaveANiceDay(double amount) {
		// TODO Auto-generated method stub

	}

	@Override
	public void msgCanDoThisAmount(double approvedAmount) {
		// TODO Auto-generated method stub

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
		this.t.msgHello(this.name, this);;
		state = CustState.AtTeller;

	}
	private void tellTeller(){
		/*TODO if cust wants loan
			t.msgLoan(Amount);
			state = CustState.AskedForLoan;
		 */
		t.msgDeposit(myInfo.depositAmount);
		//TODO determine deposit amount
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


