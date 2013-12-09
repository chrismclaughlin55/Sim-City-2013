package restaurantLY.test.mock;

import java.util.List;

import restaurantLY.gui.RestaurantGui;
import restaurantLY.gui.WaiterGui;
import restaurantLY.interfaces.Cashier;
import restaurantLY.interfaces.Cook;
import restaurantLY.interfaces.Customer;
import restaurantLY.interfaces.Host;
import restaurantLY.interfaces.Waiter;

public class MockWaiter extends Mock implements Waiter {
	public EventLog log = new EventLog();
	
	public MockWaiter(String name) {
		super(name);
	}
	
	@Override
	public String getMaitreDName() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "MockWaiter";
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "MockWaiter";
	}
	
	@Override
	public List getCustomers() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void msgOnBreak() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void msgSitAtTable(Customer customer, int tableNumber, int custNumber) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void msgImReadyToOrder(Customer customer) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void msgHereIsMyChoice(Customer customer, String choice) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void msgOrderIsReady(int tableNumber) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void msgDoneEatingAndLeaving(Customer customer) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void msgLeaving(Customer customer) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void msgAtTable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtCook() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtDoor() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void msgAtCust() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void msgDecisionOnBreak(boolean working) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgCookRunOutOfFood(int tableNumber) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void msgAskForCheck(Customer customer, double check) {
		log.add(new LoggedEvent("Asking cashier for " + customer.getName() + "'s check of $" + check));
		
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
	public void setHost(Host host) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCashier(Cashier cashier) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isOnBreak() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setOnBreak(boolean isOnBreak) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGuiPanel(RestaurantGui gui) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgLeft() {
		// TODO Auto-generated method stub
		
	}
}
