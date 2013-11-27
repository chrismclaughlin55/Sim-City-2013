package market.test.mock;

import java.util.List;

import market.MarketCustomerRole;
import market.MarketManagerRole.MyCookCustomer;
import market.MyOrder;
import market.interfaces.MarketCustomer;
import market.interfaces.MarketEmployee;
import restaurantMQ.test.mock.EventLog;

public class MockMarketEmployee extends Mock implements MarketEmployee {

	public EventLog log;
	
	public MockMarketEmployee(String name) {
		super(name);
		log = new EventLog();
	}


	@Override
	public void msgHereIsPayment(double payment) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereAreMyOrders(List<MyOrder> orders, MarketCustomer cust) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void msgGoToDesk(int deskNum) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void msgLeave() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void msgServiceCustomer(MarketCustomer customer) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void msgServiceCookCustomer(MyCookCustomer cook) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void msgDoneProcessing() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void msgHereIsRestPayment(double payment) {
		// TODO Auto-generated method stub
		
	}



	
	/*public void msgHereIsAnOrder(String type, int quantity, MarketCustomer cust) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsPayment(double payment) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereAreMyOrders(List<MarketOrder> orders, MarketCustomer cust) {
		// TODO Auto-generated method stub
		
	}*/

}
