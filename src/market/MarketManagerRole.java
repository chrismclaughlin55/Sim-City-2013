package market;

import bank.BankManagerRole;
import market.interfaces.MarketManager;
import city.PersonAgent;
import city.Role;

public class MarketManagerRole extends Role implements MarketManager{
	
	double undepositedMoney;
	boolean endOfDay = false;
	int bankAccountNum;
	BankManagerRole bankManager;
	Inventory inventory;

	public MarketManagerRole(PersonAgent person, Inventory inventory) {
		super(person);
		this.inventory = inventory;
		// TODO Auto-generated constructor stub
	}
	
	public void msgHereIsMoney(double money) {
	    undepositedMoney += money;
	    stateChanged();
	}



	@Override
	public boolean pickAndExecuteAnAction() {
		if ((undepositedMoney > 0) && (endOfDay)) {
		    DepositMoney();
		    return true;
		}
		return false;
	}
	
	private void DepositMoney() {
	    //bankManager.msgDepositMoney(bankAccountNum, undepositedMoney);
	    endOfDay = false;
	}

	

}
