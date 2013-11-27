package market.interfaces;

import java.util.List;

import market.MarketCustomerRole;
import market.MarketManagerRole.MyCookCustomer;
import market.MyOrder;

public interface MarketEmployee {
	
	public abstract void msgGoToDesk(int deskNum);
	public abstract void msgLeave();
	public abstract void msgServiceCustomer(MarketCustomer customer);
	public abstract void msgServiceCookCustomer(MyCookCustomer cook);
	public abstract void msgHereAreMyOrders(List<MyOrder>orders, MarketCustomer cust);
	public abstract void msgDoneProcessing();
	public abstract void msgHereIsPayment(double payment);
	public abstract void msgHereIsRestPayment(double payment);
}
