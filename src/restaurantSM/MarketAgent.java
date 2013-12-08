package restaurantSM;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import restaurantSM.interfaces.Market;

import agent.Agent;
import restaurantSM.utils.*;
import restaurantSM.utils.Order.OrderStatus;

public class MarketAgent extends Agent implements Market{
	String name;
	Timer timer = new Timer();
	Menu menu = new Menu();
	public Stock s = new Stock(1);
	public List<Request> requestList = new ArrayList<Request>();
	CookAgent cook;
	CashierAgent cashier;
	public double total = 0.00;
	public double tab = 0.00;
	public DecimalFormat df = new DecimalFormat("#.00");
	
	
	public MarketAgent(String n) {
		name = n;
	}
	
	public double getTab(){
		return tab;
	}
	
	public String getName(){
		return name;
	}
	
	public void setCook(CookAgent c){
		cook = c;
	}
	
	public void setCashier(CashierAgent c){
		cashier = c;
	}
	
	public void msgRequestStock(Request r){
		Do("receieved request");
		r.askedMarkets.add(this);
		requestList.add(r);
		stateChanged();
	}
	
	public void msgReceivePayment(double payment, double tabAmt) {
		total += payment;
		tab += tabAmt;
		Do("Received " + payment + " from cashier and added " + df.format(tabAmt) + " to cashier's tab.");
		stateChanged();
	}
	
	public void msgPayDownTab(double payment) {
		tab -= payment;
		stateChanged();
	}

	protected boolean pickAndExecuteAnAction() {
		if (!requestList.isEmpty()){
			FulfillRequest(requestList.get(0));
			return true;
		}
		
		return false;
	}
	
	public void FulfillRequest(Request r){
		requestList.remove(r);
		Do("fulfilling request");
		final Stock answer = new Stock(0);
		final Request req = r;
		for (String item : r.order.keySet()) {
			if (s.getStock().get(item) > 0) {
				int min = min(s.getStock().get(item), r.order.get(item));
				answer.stock.put(item, min);
				answer.total += menu.prices.get(item);
				s.stock.put(item,  s.stock.get(item) - min);
				r.order.put(item, r.order.get(item) - min);
				Do(r.order.get(item) + " " + item + " left in request");
			}
		}
		
		timer.schedule(new TimerTask() {
			public void run() {
				//cook.msgHeresSomeStock(answer, r);
			}
		}, 10000);
		cook.msgHeresSomeStock(answer, r);
		Do("asking cashier for " + df.format(answer.total) + " for this order");
		cashier.msgPayForStock(this, answer.total);
	}
	
	public int min(int a, int b){
		if (a < b) {
			return a;
		}
		return b;
	}
}
