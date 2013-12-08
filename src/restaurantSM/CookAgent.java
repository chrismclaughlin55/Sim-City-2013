package restaurantSM;

import agent.Agent;
import restaurantSM.gui.*;

import java.util.*;
import java.util.concurrent.Semaphore;

import restaurantSM.utils.*;
import restaurantSM.utils.Order.OrderStatus;

public class CookAgent extends Agent {
	String name;
	List<Order> orders = new ArrayList<Order>();
	Timer timer = new Timer();
	Menu menu = new Menu();
	Stock s = new Stock(1);
	int threshold = 2;
	List<MarketAgent> markets = new ArrayList<MarketAgent>();
	int nextMarket = 0;
	boolean orderFulfilled = true;
	boolean resubmit = false;
	public Request currentReq;
	CookGui cookGui;
	private Semaphore isMoving = new Semaphore(0,true);

	public CookAgent(String n) {
		name = n;
	}
	
	public void setGui(CookGui g) {
		cookGui = g;
	}

	public String getName(){
		return name;
	}

	public void addMarket(MarketAgent m) {
		markets.add(m);
	}
	
	public void msgDoneMoving() {
		isMoving.release();
		stateChanged();
	}

	public void msgHeresAnOrder(Order o){
		orders.add(o);
		if (s.getStock().get(o.getChoice()) == 0) {
			o.orderStatus = OrderStatus.OutOfStock;
		}
		else {
			o.orderStatus = OrderStatus.Received;
		}
		stateChanged();
	}

	public void msgHeresSomeStock(Stock stock, Request r){
		Do("got request back");
		currentReq = r;
		for (String item : stock.stock.keySet()) {
			Do("changing " + item + " from " + s.stock.get(item) + " to " + (s.stock.get(item) + stock.stock.get(item)));
			s.stock.put(item, s.stock.get(item) + stock.stock.get(item));
		}
		for (String item : r.order.keySet()) {
			if (r.order.get(item) > 0) {
				orderFulfilled = false;
				resubmit = true;
				Do("resubmit is true");
				stateChanged();
				return;
			}
		}
		orderFulfilled = true;
		resubmit = false;
		stateChanged();
	}
	
	public void msgSorryOutOfThat(Request r){
		currentReq = r;
		for (MarketAgent market : markets) {
			if (!r.askedMarkets.contains(market)){
				resubmit = true;
				Do("Asking another market for that request.");
				stateChanged();
				return;
			}
		}
		resubmit = false;
		orderFulfilled = true;
		stateChanged();
	}


	protected boolean pickAndExecuteAnAction() {

			if (resubmit) {
				Do("Resubmitting old request");
				RequestStock(currentReq);
				resubmit = false;
				return true;
			}
		
		
			for (String key : menu.getItems()){
				if (s.getStock().get(key) < threshold && orderFulfilled){
					orderFulfilled = false;
					Request r = new Request();
					for (String item : s.getStock().keySet()) {
						if (s.getStock().get(item) < threshold) {
							r.order.put(item, threshold);
						}
					}
					if (!r.order.keySet().isEmpty()) {
						Do("Making request");
						RequestStock(r);
						orderFulfilled = false;
					}					
					return true;
				}
			}

			if (!orders.isEmpty()){
				for (Order o : orders){
					if (o.orderStatus == OrderStatus.OutOfStock){
						OutOfStock(o);
						return true;
					}
				}

			if (orders.get(0).orderStatus == OrderStatus.Cooked){
				OrderIsCooked(orders.get(0));
				return true;
			}
			if (orders.get(0).orderStatus == OrderStatus.Received){
				orders.get(0).orderStatus = OrderStatus.Cooking;
				s.getStock().put(orders.get(0).getChoice(), s.getStock().get(orders.get(0).getChoice()) - 1);
				CookOrder(orders.get(0));
				return true;
			}
		}
		return false;
	}

	public void CookOrder(Order o){
		cookGui.DoGoToPlating();
		try {
			isMoving.acquire();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		cookGui.setStatusText("Fridging");
		cookGui.DoGoToFridge();
		try {
			isMoving.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cookGui.DoGoToGrills();
		cookGui.addFood();
		cookGui.setStatusText("Grilling");
		try {
			isMoving.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		timer.schedule(new TimerTask() {
			public void run() {
				orders.get(0).orderStatus = OrderStatus.Cooked;
				stateChanged();
				cookGui.DoGoToPlating();
				cookGui.setStatusText("Plating");
				try {
					isMoving.acquire();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				cookGui.DoGoToHome();
				cookGui.removeFood();
				cookGui.setStatusText("");
			}
		}, 5000);
	}

	public void OrderIsCooked(Order o){
		o.getWaiter().msgOrderIsReady(o);
		orders.remove(o);
	}

	public void OutOfStock(Order o) {
		menu.removeItem(o.getChoice());
		o.getWaiter().msgOutOfStock(menu, o);
		orders.remove(o);
	}

	public void RequestStock(Request r){
		for (MarketAgent market : markets) {
			if (!r.askedMarkets.contains(market)) {
				Do("Sending order to " + market.getName());
				r.askedMarkets.add(market);
				market.msgRequestStock(r);
				return;
			}
		}
		
	}

}