package restaurantBK.test.mock;

import java.util.Collection;
import java.util.List;

import restaurantBK.CustomerAgent;
import restaurantBK.interfaces.Customer;
import restaurantBK.interfaces.Host;
import restaurantBK.interfaces.Waiter;

public class MockHost extends Mock implements Host{

	public MockHost(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getMaitreDName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getWaitingCustomers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getWaiters() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection getTables() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void msgIWantFood(Customer cust) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgBreakPlease(Waiter w) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgComingBackFromBreak(Waiter w) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgTableIsFree(int table) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgNoTablesImLeaving(Customer c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addWaiter(Waiter waiter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void gotCustomer() {
		// TODO Auto-generated method stub
		
	}

}
