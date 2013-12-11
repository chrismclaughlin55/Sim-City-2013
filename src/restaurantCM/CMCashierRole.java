package restaurantCM;

import java.util.*;

import city.PersonAgent;
import city.Role;
import restaurantCM.*;
import restaurantCM.gui.CMRestaurantBuilding;
import agent.Agent;

public class CMCashierRole extends Role{
	private String name;
	private double totalMoney;
	private enum customerState { notPaid, calculatedOrder, billIsReady, askedForBill, paid, sentBill};
	private List<custOrder> Bills = new ArrayList<custOrder>();
//	private List<marketOrder> Orders = new ArrayList<marketOrder>();
	private enum marketState{ needsPay, notPaid, Paid, setBill}
	private Menu menu = new Menu();
	private CMCookRole cook;
	private boolean leave = false;
	public CMRestaurantBuilding building;
	public double buildingMoney;
	//private class
//	private class marketOrder{
//		public double bill;
//		public marketState state;
//		public MarketAgent m;
//		public marketOrder(double bill, MarketAgent m){
//			this.bill = bill;
//			this.m = m;
//			this.state = marketState.needsPay;
//		
//	}
	private class custOrder{
		public String choice;
		public CMCustomerRole c;
		public CMWaiterRole w;
		public customerState state;
		public double totalBill;
		public custOrder(CMWaiterRole w, CMCustomerRole c, String choice, double bill){
			this.choice = choice;
			this.c = c;
			this.w = w;
			this.state = customerState.calculatedOrder;
			this.totalBill = bill;
		}
	}

	//Actions
	//Constructor
	//TODO 
	public CMCashierRole(PersonAgent person, CMRestaurantBuilding building){
		super(person);
		print("cashier created");
		this.building = building;
		this.name = name;
		this.totalMoney = 10.0;
	}
	public custOrder findWaiter(CMWaiterRole w){
		for(custOrder c: Bills){
			if(w.equals(c.w))
				return c;
		}
		return null;
	}
	public custOrder findCust(CMCustomerRole c){
		for(custOrder cust : Bills){
			if(cust.c.equals(c))
				return cust;
		}
		return null;
	}
	//MESSAGES
//	public void msgPayThisMarket(MarketAgent m, double bill){
//		print("I need to pay "+m.getName());
//		Orders.add(new marketOrder(bill, m));
//		stateChanged();
//	}
	
	public void msgCalcOrder(CMWaiterRole w, CMCustomerRole c, String choice){
		double bill = menu.Menu.get(choice);
		print("calculating order for "+c.getCustomerName()+" and it is "+ bill);
		Bills.add(new custOrder(w, c, choice, bill));
		stateChanged();
	}
	public void msgGiveMeBill(CMWaiterRole w, CMCustomerRole c , double totalPay){
		custOrder cc = findCust(c);
		findCust(c).state = customerState.paid;
		stateChanged();
	}

	@Override
	public boolean pickAndExecuteAnAction() {
	for(custOrder order: Bills)
		if(order.state == customerState.calculatedOrder){
			sendBill(order);
			return true;
		}
	if(leave){
		leave();
		return true;
	}
//	for(marketOrder m : Orders){
//		if(m.state == marketState.notPaid && m.bill <= this.totalMoney){
//			print(" total money = "+ totalMoney);
//			payMarket(m);
//			return true;
//		}
//	}
//	for(marketOrder m : Orders){
//		if(m.state == marketState.needsPay){
//			print("made it here 3");
//			payMarket(m);
//			
//			return true;
//		}
//	}
			
		return false;
	}
//	private void payMarket(marketOrder m) {
//		if(m.bill>this.totalMoney){
//			m.state = marketState.notPaid;
//			return;
//		}
//		m.m.msgPayment(m.bill);
//		totalMoney-=m.bill;
//		
//	}
	private void sendBill(custOrder order) {
		order.w.msgHereIsBill(order.c, order.totalBill);
		order.state = customerState.sentBill;
		
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void msgAddCook(CMCookRole cook) {
		this.cook = cook;
		
	}
	private void leave() {
		person.exitBuilding();
		person.msgDoneWithJob();
		doneWithRole();	
		leave = false;
	}
	public void msgLeave() {
		leave = true;
		stateChanged();
		
	}
	
	

}
