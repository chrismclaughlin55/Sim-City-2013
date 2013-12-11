package restaurantSM.test.mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import restaurantSM.SMCashierRole;
import restaurantSM.SMCookRole;
import restaurantSM.interfaces.Cashier;
import restaurantSM.interfaces.Market;
import restaurantSM.utils.Menu;
import restaurantSM.utils.Request;
import restaurantSM.utils.Stock;

public class MockMarket extends Mock implements Market {

	public Stock s = new Stock(1);
	public double total = 0.00;
	Cashier cashier;
	public double billPrice = 40.00;
	public double tab = 0.00;
	
	
	public MockMarket(String name, Cashier c) {
		super(name);
		cashier = c;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgRequestStock(Request r) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("receieved msgRequestStock"));
		cashier.msgPayForStock(this, billPrice);
		for (String item : s.stock.keySet()) {
			r.order.put(item, r.order.get(item) - 1);
		}
	}

	@Override
	public void msgReceivePayment(double payment, double tabAmt) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("receieved msgReceivePayment"));
		total += payment;
	}

	@Override
	public void msgPayDownTab(double payment) {
		// TODO Auto-generated method stub
		
	}

}
