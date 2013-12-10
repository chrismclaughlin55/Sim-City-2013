package restaurantBK;

import agent.Agent;
//import restaurant.gui.WaiterGui;



import java.util.*;
import java.util.concurrent.Semaphore;

import city.PersonAgent;
import city.Role;
import restaurantBK.BKCustomerRole.AgentEvent;
import restaurantBK.gui.CookGui;
import restaurantBK.gui.RestaurantPanel;
import restaurantBK.gui.WaiterGui;
import restaurantBK.interfaces.Cook;
import restaurantBK.interfaces.Market;
import restaurantBK.interfaces.Waiter;
import trace.AlertLog;
import trace.AlertTag;


/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class BKCookRole extends Role implements Cook {
	
	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.
	//Later we will see how it is implemented
	RestaurantPanel rest;
	private int amountorder=3;
	private class Food {
		String choice;
		int cookingTime;
		int inventory;
		public Food(String c, int ct, int i) {
			choice = c;
			cookingTime = ct;
			inventory = i;
		}
	}
	private class myMarket {
		Market m;
		String name;
		Map<String,Food> stuff;
		boolean outoffood;
	}
	private List<myMarket> markets;
	public Timer timer = new Timer();
	public Map<String,Food> foods;
	boolean orderedFromMarket = false;
	public CookGui cookGui = null;
	//public HashMap<String,Food> times;
	private String name;
	//private Semaphore cooking = new Semaphore(0);
	public class Order{
		String name;
		OrderState os;
		Waiter w;
		int tablenumber;
		public Order(String choice, Waiter w, int tn) {
			this.name=choice;
			this.w=w;
			this.tablenumber=tn;
			this.os=OrderState.pending;
		}
	}
	public List<Order> orders = Collections.synchronizedList(new ArrayList<Order>());
	public enum OrderState {pending,cooking,plated,done};
	private Semaphore atDestination = new Semaphore(0,true);
	public BKCookRole(PersonAgent person, String name, RestaurantPanel rest) {
		super(person);
		this.rest = rest;
		markets = new ArrayList<myMarket>();
		Food st = new Food("Steak",3000,5);
		Food ch = new Food("Chicken",2000,5);
		Food sa = new Food("Salad",2000,5);
		Food pi = new Food("Pizza", 2000, 5);
		foods=new HashMap<String,Food>();
		foods.put("Steak", st);
		foods.put("Chicken", ch);
		foods.put("Salad", sa);
		foods.put("Pizza", pi);
		this.name = name;		
		
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Cook#getName()
	 */
	@Override
	public String getName() {
		return name;
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Cook#addMarket(restaurant.MarketAgent)
	 */
	@Override
	public void addMarket(Market m) {
		myMarket mark = new myMarket();
		mark.m = m;
		mark.name= m.getName();
		mark.outoffood=false;
		markets.add(mark);
		
	}
	// Messages

	/* (non-Javadoc)
	 * @see restaurant.Cook#msgHereIsAnOrder(restaurant.interfaces.Waiter, java.lang.String, int)
	 */
	@Override
	public void msgAtDestination() {//from animation
		atDestination.release();// = true;
		stateChanged();										
	}
	
	@Override
	public void msgPickedUpOrder(int tn) {
		synchronized(orders) {
			for(Order o:orders) {
				if(o.tablenumber==tn) {
					o.os=OrderState.done;
					stateChanged();
				}
			}
		}
	}
	
	@Override
	public void msgHereIsAnOrder(Waiter w, String choice, int tn) {
		Order o = new Order(choice,w,tn);
		orders.add(o);
		print("Received order " + choice);
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Cook#msgCantMakeMarketOrder(restaurant.MarketAgent)
	 */
	@Override
	public void msgCantMakeMarketOrder(Market m,HashMap<String,Integer>ship) {
		for(myMarket mark : markets) {
			if(mark.m==m) {
				mark.outoffood=true;
				orderedFromMarket=false;
				print("Gotta order from market again...");
				stateChanged();
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Cook#msgMarketOrderResponse(restaurant.MarketAgent, java.util.HashMap)
	 */
	@Override
	public void msgMarketOrderResponse(Market m,HashMap<String,Integer>shipment) {
		//INSTEAD OF SINGULAR FOODNAME, give and send list of pairs(string,amount);
		
		//foods.get(foodname).inventory+=shipment;
		foods.put("Steak",new Food("Steak",foods.get("Steak").cookingTime,foods.get("Steak").inventory+shipment.get("Steak")));
		foods.put("Chicken",new Food("Chicken",foods.get("Chicken").cookingTime,foods.get("Chicken").inventory+shipment.get("Chicken")));
		foods.put("Salad",new Food("Salad",foods.get("Salad").cookingTime,foods.get("Salad").inventory+shipment.get("Salad")));
		foods.put("Pizza",new Food("Pizza",foods.get("Pizza").cookingTime,foods.get("Pizza").inventory+shipment.get("Pizza")));
		print("Thanks market!");
		orderedFromMarket=false;
	}
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		
		CheckInventory();
		synchronized(orders) {
			for (Order order : orders) {	
				if (order.os==OrderState.pending) {
					{//if (!waitingCustomers.isEmpty()) {
						CookIt(order);
						return true;//return true to the abstract agent to reinvoke the scheduler.
					}
				}
			}
		}
		synchronized(orders) {
			for(Order o: orders) {
				if(o.os==OrderState.done) {
					RemoveOrder(o);
					return true;
				}
			}
		}
		/*catch(ConcurrentModificationException e) {
			System.out.println(orders.size());
			return false;
		}*/
		if(orders.isEmpty()) {
			GoToHomePosition();
		}
		//if there's an order, cook it
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}
	
	
	private void GoToHomePosition() {
		cookGui.DoGoToHome();
		try {
			//System.out.println("Semaphore acqui															red");
			atDestination.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void CheckInventory() {
		//print("CheckingInventory");
		if(!orderedFromMarket) {
			if(foods.get("Steak").inventory<3||foods.get("Chicken").inventory<3||foods.get("Salad").inventory<3||foods.get("Pizza").inventory<3) {
				HashMap<String,Integer> shipment = new HashMap<String,Integer>();
				shipment.put("Steak",amountorder-foods.get("Steak").inventory);
				shipment.put("Chicken",amountorder-foods.get("Chicken").inventory);
				shipment.put("Salad",amountorder-foods.get("Salad").inventory);
				shipment.put("Pizza",amountorder-foods.get("Pizza").inventory);
				OrderFromMarket(shipment);
				amountorder=10;
			}
		}
	}
	private void RemoveOrder(Order o) {
		int x = o.tablenumber;
		orders.remove(o);
		cookGui.flipPlated(x);
	}
	private void CookIt(final Order o){
		//ANIMATION DoCooking(o);
		//timer.start();
		//find the food, however long it takes to cook
		
		//CHECK INVENTORY
		
		//MAKE ORDER
		if(foods.get(o.name).inventory==0) {
			//KEEP TRACK OF ALL INVENTORY, NOT JUST FOODS WHEN INVENTORY = 01
			print("We're out of " +o.name);
			AlertLog.getInstance().logMessage(AlertTag.RESTAURANTBK_COOK, this.getName(), "We're out of " +o.name);
			o.w.msgOutOfOrder(o.name, o.tablenumber);
			orders.remove(o);
			//WE HAVE TO ORDER FROM MARKET NOW
		}
		else {
			foods.get(o.name).inventory--;
			o.os=OrderState.cooking;
			cookGui.DoMoveToCook(o.name,o.tablenumber);
			try {
				atDestination.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			print("Cooking "+o.name);
			AlertLog.getInstance().logMessage(AlertTag.RESTAURANTBK_COOK, this.getName(), "Cooking " +o.name);
			timer.schedule(new TimerTask() {
				public void run() {		
					o.os = OrderState.plated;
					print("ORDER UP for" + o.name + " at table " + o.tablenumber);
					cookGui.DoMoveToPlating();
					try {
						//System.out.println("Semaphore acquired");
						atDestination.acquire();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					cookGui.flipPlated(o.tablenumber);
					o.w.msgOrderIsReady(o.tablenumber, o.name);
				}	
			},
			foods.get(o.name).cookingTime);
		}
	}
	
	private void OrderFromMarket(HashMap<String,Integer> order) {
		print("Ordering from market");
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANTBK_COOK, this.getName(), "Ordering from market");
		for(myMarket mark : markets) {
			if(mark.outoffood==false) {
				mark.m.msgFoodRequest(order,this);
				orderedFromMarket=true;
				break;
			}
			//GET RESPONSE THAT I CAN'T DO IT...
		}
		
	}

	@Override
	public void setGui(CookGui gui) {
		cookGui = gui;
	}

	/* (non-Javadoc)
	 * @see restaurant.Waiter#getGui()
	 */
	@Override
	public CookGui getGui() {
		return cookGui;
	}

	// Actions

	//utilities


	
}

