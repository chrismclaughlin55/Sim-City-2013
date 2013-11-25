package bank;

import bank.interfaces.BankCustomer;
import bank.interfaces.Teller;
import bank.utilities.CustInfo;
import city.PersonAgent;
import city.Role;
/*
 *  Added events, states, customer info. TODO actions, scheduler
 */
public class TellerRole extends Role implements Teller{
	String name;
	BankManagerRole bm;
	CustInfo currentCustInfo;
	enum State {available, waitingForInfo, waitingForResponse, doneWithCustomer, customerDecidingLoan}
	enum Event {none, recievedHello, recievedInfo, recievedDeposit,updatedBank,loanReq, iTakeIt}
	State state;
	Event event;
	public TellerRole(PersonAgent person) {
		super(person);
		this.name = person.getName();
		state = State.available;
		event = Event.none;
		// TODO Auto-generated constructor stub
	}
	
	//MESSAGES
	@Override
	public void msgHello(String name, BankCustomer c) {
		currentCustInfo = new CustInfo(name, c.returnPerson() );
		
	}

	@Override
	public void msgHereIsInfo(CustInfo info) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgDeposit(double money) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loan(double amount) {
		// TODO Auto-generated method stub
		
	}
	
	
	//SCHEDULER
	@Override
	public boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void iTakeIt(double loanAmount) {
		// TODO Auto-generated method stub
		
	}

}
