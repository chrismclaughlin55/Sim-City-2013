package bank;


import city.PersonAgent;
import city.Role;

public class CustomerRole extends Role implements BankCustomer{

	public CustomerRole(PersonAgent person) {
		super(person);
		// TODO Auto-generated constructor stub
	}
	//MESSAGES
	@Override
	public void msgGoToTeller(Teller t) {
		// TODO Auto-generated method stub
		
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

	
	public boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}

	
}
