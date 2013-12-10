package restaurantSM;

import agent.Agent;
import restaurantSM.gui.*;

import java.util.*;
import java.util.concurrent.Semaphore;

import city.PersonAgent;
import city.Role;
import restaurantSM.utils.*;
import restaurantSM.utils.Order.OrderStatus;
import trace.AlertLog;
import trace.AlertTag;

public class SMCookRole extends Role {
	String name;
	List<Order> orders = new ArrayList<Order>();
	Timer timer = new Timer();
	Menu menu = new Menu();
	Stock s = new Stock(10);
	int threshold = 2;
	List<MarketAgent> markets = new ArrayList<MarketAgent>();
	int nextMarket = 0;
	boolean orderFulfilled = true;
	boolean resubmit = false;
	public Request currentReq;
	CookGui cookGui;
	private Semaphore isMoving = new Semaphore(0,true);

	public SMCookRole(PersonAgent p) {
		super(p);
		name = p.getName();
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
		currentReq = r;
		for (String item : stock.stock.keySet()) {
			s.stock.put(item, s.stock.get(item) + stock.stock.get(item));
		}
		for (String item : r.order.keySet()) {
			if (r.order.get(item) > 0) {
				orderFulfilled = false;
				resubmit = true;
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
				stateChanged();
				return;
			}
		}
		resubmit = false;
		orderFulfilled = true;
		stateChanged();
	}


	public boolean pickAndExecuteAnAction() {

			if (resubmit) {
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
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANTSM_COOK, this.getName(), "Cooking order of "+o.getChoice());
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
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANTSM_COOK, this.getName(), "Order of "+o.getChoice()+" is done");
	}

	public void OutOfStock(Order o) {
		menu.removeItem(o.getChoice());
		o.getWaiter().msgOutOfStock(menu, o);
		orders.remove(o);
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANTSM_COOK, this.getName(), "Running out of "+o.getChoice());
	}

	public void RequestStock(Request r){
		for (MarketAgent market : markets) {
			if (!r.askedMarkets.contains(market)) {
				r.askedMarkets.add(market);
				market.msgRequestStock(r);
				AlertLog.getInstance().logMessage(AlertTag.RESTAURANTSM_COOK, this.getName(), "Requesting "+market.name+" for stock");
				return;
			}
		}
		
	}

}