package restaurantBK.test.mock;

import java.util.List;

import restaurantBK.BKCustomerRole;
import restaurantBK.gui.WaiterGui;
import restaurantBK.interfaces.Cashier;
import restaurantBK.interfaces.Cook;
import restaurantBK.interfaces.Customer;
import restaurantBK.interfaces.Host;
import restaurantBK.interfaces.Waiter;

public class MockWaiter extends Mock implements Waiter{

	public Cashier cashier;
	
	public MockWaiter(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean wantsBreak() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean waitingForResponse() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List getCustomers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCook(Cook cook) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setHost(Host host) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCashier(Cashier cash) {
		// TODO Auto-generated method stub
		cashier=cash;
	}

	@Override
	public void msgGoBackToWork() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgChangeWantsBreak() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgGoOnBreak() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgCantBreak() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgReadyToOrder(Customer cust) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void msgHereIsMyChoice(Customer c, String choice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOrderIsReady(int tn, String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtDestination() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOutOfOrder(String order, int tn) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgDoneEatingAndWantCheck(Customer c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgCheckMade(double cost, int table) {
		// TODO Auto-generated method stub
		//System.out.println("Got check, delivering to customer");
	}

	@Override
	public void msgLeavingEarly(Customer c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgLeavingTableToPay(Customer c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGui(WaiterGui gui) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public WaiterGui getGui() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getRestX() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void msgSitAtTable(Customer cust, int tn, int pos) {
		// TODO Auto-generated method stub
		
	}

}
