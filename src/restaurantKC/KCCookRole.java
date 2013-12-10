package restaurantKC;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import market.CookMarketOrder;
import market.CookMarketOrder.MarketOrderState;
import market.MyOrder.OrderState;
import market.Invoice;
import market.Market;
import restaurantKC.gui.CookGui;
import restaurantKC.gui.RestaurantPanel;
import restaurantKC.interfaces.Cook;
import restaurantKC.interfaces.Waiter;
import city.PersonAgent;
import city.Role;

/**
 * Restaurant Host Agent
 */

public class KCCookRole extends Role implements Cook {

	public RestaurantPanel restPanel;

	private List<pcCookOrder> cookOrders;
	private Map<String, Food> foodMap = Collections.synchronizedMap(new HashMap<String, Food>());
	private List<CookMarketOrder> marketOrders = Collections.synchronizedList(new ArrayList<CookMarketOrder>());
	enum State {pending, cooking, done, plated, sent, discard};
	String name;
	public CookGui cookGui = null;
	Timer timer = new Timer();
	private int marketNum = 0;

	int plateNum = 0;
	int orderNumber = 0;
	private Semaphore atFridge = new Semaphore(0, true);
	private Semaphore atGrill = new Semaphore(0, true);
	private Semaphore atPlate = new Semaphore(0, true);
	private Semaphore platingFood = new Semaphore(0, true);
	private Semaphore atHome = new Semaphore(0,true);
	private Semaphore askWaiter = new Semaphore(0,true);

	class CookOrder {
		Waiter waiter;
		String choice;
		int table;
		State state;
		int grill;
		int plate;
		int orderNum;



		public CookOrder(Waiter w, String c, int t, State s) {
			waiter = w;
			choice = c;
			table = t;
			state = s;
			orderNum = orderNumber;
			grill = orderNumber%3;
			plate = grill;
		}
	}




	class Food {
		int amount;
		int cookingTime;
		boolean orderPending = false;
		public Food (int a, int c) {
			amount = a;
			cookingTime = c;
		}
	}

	private List<CookOrder> orders = Collections.synchronizedList(new ArrayList<CookOrder>());
	private List<Market> markets = Collections.synchronizedList(new ArrayList<Market>());

	public KCCookRole(List<pcCookOrder> cookOrders, PersonAgent p, RestaurantPanel restPanel) {
		super(p);
		markets = person.cityData.markets;
		this.cookOrders = cookOrders;
		Food steak = new Food(2, 5);
		Food salad = new Food(100, 2);
		Food pizza = new Food(100, 4);
		Food chicken = new Food(100, 3);
		this.name = person.getName();
		foodMap.put("Steak", steak); 
		foodMap.put("Salad", salad);  
		foodMap.put("Pizza", pizza);  
		foodMap.put("Chicken", chicken);
		this.restPanel = restPanel;
	}

	public void setGui(CookGui gui) {
		cookGui = gui;
	}





	/*public void msgHereIsAnOrder(WaiterAgent w, String choice, int table) {
		orders.add(new CookOrder(w, choice, table, State.pending));
		orderNumber++;
		System.out.println ("Cook: received order of " + choice + " " + orders.size());
		stateChanged();
	}*/

	public void msgOrdersUpdated()
	{
		stateChanged();
	}

	public void msgOrderFulfilled(String type, int amount) {
		foodMap.get(type).amount += amount;
		foodMap.get(type).orderPending = false;

		synchronized(foodMap){
			for (Map.Entry<String, Food> entry : foodMap.entrySet()) {
				System.out.println(entry.getKey() + " " + entry.getValue().amount);
			}
		}
		stateChanged();
	}

	public void msgOrdersFulfilled(List<Invoice> invoice) {
		print ("Order fulfilled");
		for (Invoice i : invoice) {
			foodMap.get(i.type).orderPending = false;
			foodMap.get(i.type).amount += i.amount;
		}
		stateChanged();
	}



	public void msgImTakingTheFood(String choice, int t) {
		synchronized (orders) {
			for (CookOrder o : orders) {
				if (o.state == State.sent)  {
					o.state = State.discard;
					cookGui.removeFood(o.orderNum);
				}
			}
		}
		stateChanged();
	}


	@Override
	public boolean pickAndExecuteAnAction() {

		synchronized(foodMap) {
			for (Map.Entry<String, Food> entry : foodMap.entrySet()) {
				if ((entry.getValue().amount <= 3) && (!entry.getValue().orderPending)){
					marketOrders.add(new CookMarketOrder(entry.getKey(), 10));
					entry.getValue().orderPending = true;
				}
			}
			if ((!marketOrders.isEmpty()) && (restPanel.cashier != null)) {
				orderFromMarket();
				return true;
			}
		}

		synchronized (orders) {
			if (!orders.isEmpty())
			{
				for (CookOrder o : orders){
					if (o.state == State.done) {
						o.state = State.plated;
						PlateIt(o);	
						return true;

					}
				}
			}
		}

		synchronized (orders) {
			if (!orders.isEmpty()) {
				for (CookOrder o : orders){
					if (o.state == State.pending) {
						CookIt(o);
						o.state = State.cooking;
						return true;
					}
				}
			}
		}


		//Check for new orders
		synchronized(cookOrders) {
			for(pcCookOrder o : cookOrders) {
				orders.add(new CookOrder(o.waiter, o.choice, o.table, State.pending));
				orderNumber++;
				cookOrders.remove(o);
				return true;
			}
		}

		for(Iterator<CookOrder> iter = orders.iterator(); iter.hasNext(); ) {
			CookOrder o = iter.next();
			if(o.state == State.discard) {
				iter.remove();
			}
		}


		if(person.cityData.hour >= restPanel.CLOSINGTIME && orders.isEmpty() && cookOrders.isEmpty() /*&& restPanel.activeWaiters() == 0*/) {
			LeaveRestaurant();
			return true;
		}

		return false; 
	}


	private void CookIt(CookOrder o){
		if (foodMap.get(o.choice).amount > 0) {
			foodMap.get(o.choice).amount--; 
			CookFood(o);
		}
		else {
			o.waiter.msgImOutOfFood(o.table);
		}
	}

	private void PlateIt(CookOrder o) {
		PlateFood(o);
		platingFood.drainPermits();
		try {
			platingFood.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		o.waiter.msgOrderIsReady(o.choice, o.table, o.plate); 
		o.state = State.sent;
	}

	private void PlateFood(CookOrder o) {
		cookGui.DoGoToGrill2(o.grill);
		atGrill.drainPermits();
		try {
			atGrill.acquire(); 
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		cookGui.DoGoToPlate(o.plate, o.orderNum);
		atPlate.drainPermits();
		try {
			atPlate.acquire(); 
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void CookFood(final CookOrder o) {

		DoGetIngredients();

		cookGui.procureFood(o.choice, o.orderNum);

		cookGui.DoGoToGrill(o.grill, o.orderNum);

		atGrill.drainPermits();
		try {
			atGrill.acquire(); 
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		cookGui.DoGoHome();
		atHome.drainPermits();
		try {
			atHome.acquire(); 
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		timer.schedule(new TimerTask() {
			public void run() {
				markFoodDone(o);
			}
		},
		foodMap.get(o.choice).cookingTime*1000);
	}

	private void orderFromMarket() {
		List<CookMarketOrder> ordersToSend = Collections.synchronizedList(new ArrayList<CookMarketOrder>());
		Random rand = new Random();
		int marketNum = rand.nextInt(1);

		for (CookMarketOrder m : marketOrders) {
			if (m.state == MarketOrderState.pending) {
				m.state = MarketOrderState.ordered;
				ordersToSend.add(m);
			}
		}
		marketOrders.clear();

		markets.get(marketNum).currentManager.msgHereIsMarketOrder(this, (KCCashierRole)restPanel.cashier, ordersToSend);
	}


	public void markFoodDone(CookOrder or) {
		synchronized(orders) {
			for (CookOrder o : orders){
				if (o.orderNum == or.orderNum) {
					o.state = State.done;
				}
			}
		}
		stateChanged();
	}

	/*public void addMarkets(List<MarketAgent> markets) {
		this.markets = markets;
	}*/

	public void drainInventory() {
		synchronized(foodMap) {
			for (Map.Entry<String, Food> entry : foodMap.entrySet()) {
				entry.getValue().amount = 0;
			}
		}
	}

	// GUI Stuff

	private void DoGetIngredients() {
		cookGui.DoGoToFridge(); 
		atFridge.drainPermits();
		try {
			atFridge.acquire(); 
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public void msgAtFridge() {
		atFridge.release();
		stateChanged();
	}

	public void msgAtGrill() {
		atGrill.release();
		stateChanged();
	}

	public void msgAtHome() {
		atHome.release();
		stateChanged();
	}

	public void msgAtPlate() {
		atPlate.release();
		askWaiter.release();
		platingFood.release();
		stateChanged();
	}


	private void LeaveRestaurant() {
		person.hungerLevel = 0;
		cookGui.DoLeaveRestaurant();
		person.exitBuilding();
		person.msgDoneWithJob();
		doneWithRole();
	}



}

