package market;

import market.MarketEmployeeRole.orderState;
import market.interfaces.MarketCustomer;

public class MarketOrder {
	public String type;
	public int quantity;
	MarketCustomer cust;
	//CookRole cookCust
	orderState state;
	String custType;

	public MarketOrder(String type, int quantity, MarketCustomer cust, orderState state, String custType) {
		this.type = type;
		this.quantity = quantity;
		this.cust = cust;
		this.state = state;
		this.custType = custType; 
	}

	/*public MarketOrder(String type, int quantity, CookRole cookCust, orderState state, String custType) {
        this.type = type;
        this.quantity = quantity;
        this.cookCust = cookCust;
        this.state = state;
        this.custType = custType;
    }*/
}