package restaurantBK;

import agent.Agent;
//import restaurant.gui.WaiterGui;










import java.util.*;
import java.util.concurrent.Semaphore;

import restaurantBK.BKCookRole.OrderState;
import restaurantBK.BKCustomerRole.AgentEvent;
import restaurantBK.interfaces.Cashier;
import restaurantBK.interfaces.Cook;
import restaurantBK.interfaces.Market;


/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class MarketAgent extends Agent implements Market {
	
	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.
	//Later we will see how it is implemented
	
	public Timer timer = new Timer();
	private Cook cook;
	private Cashier cashier;
	//public HashMap<String,Food> times;
	private String name;
	private double steakCost = 7.00;
	private double chickenCost = 5.00;
	private double saladCost = 3.00;
	private double pizzaCost = 4.00;
	private double foodscost=0.00;
	/*
	private class Food {
		String nam;
		int inventory;
		public Food(String c, int i) {
			nam = c;
			inventory = i;
		}
	}
	*/
	public class Bill {
		double cost;
		BillState bs;
		public Bill(double price) {
			cost=price;
			bs=BillState.made;
		}
	}
	List<Bill> bills = Collections.synchronizedList(new ArrayList<Bill>());
	public enum BillState {made,paid};
	public HashMap<String,Integer> foods;
	//private Semaphore cooking = new Semaphore(0);
	public class Request{
		HashMap<String,Integer>desire;
		RequestState rs;
		double cost;
		public Request(HashMap<String,Integer> req) {
			desire=req;
			this.rs=RequestState.pending;
			this.cost=0.0;
		}
	}
	public List<Request> requests = Collections.synchronizedList(new ArrayList<Request>());
	public enum RequestState {pending,fulfilling,done};
	public MarketAgent(String name,int steak, int chicken,int salad,int pizza) {
		super();
		this.name = name;
		/*
		Food st = new Food("Steak",20);
		Food ch = new Food("Chicken",15);
		Food sa = new Food("Salad",15);
		Food pi = new Food("Pizza", 15);
		*/
		foods=new HashMap<String,Integer>();
		foods.put("Steak", steak);
		foods.put("Chicken", chicken);
		foods.put("Salad", salad);
		foods.put("Pizza", pizza);
	}

	
	/* (non-Javadoc)
	 * @see restaurant.Market#getName()
	 */
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public void setCashier(Cashier c) {
		cashier = c;
	}
	// Messages

	/* (non-Javadoc)
	 * @see restaurant.Market#msgFoodRequest(java.util.HashMap, restaurant.interfaces.Cook)
	 */
	@Override
	public void msgFoodRequest(HashMap<String,Integer>order, Cook cook) {
		this.cook = cook;
		print("Received order");
		Request req = new Request(order);
		requests.add(req);
		stateChanged();
		
		//Order o = new Order(choice,w,tn);
		//orders.add(o);
		//print("Received order " + choice);
	}
	
	public void msgPayingMarketBill(Cashier cashier, double payment) {
		synchronized(bills) {
			for(Bill b: bills) {
				if(b.cost==payment) {
					b.bs=BillState.paid;
					print("Thanks for the shipment payment.");
					stateChanged();
				}
			}
		}
		
	}
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		/* Think of this next rprint("HELLO?");ule as:
            Does there exist a table and customer,
            so that table is unoccupied and customer is waiting.
            If so seat him at the table.
		 */
		//print("HELLO?");
		//print("Why not me?");
		synchronized(requests) {
			for (Request r : requests) {
				if (r.rs==RequestState.pending) {
					FulfillRequest(r);
					return true;
				}
			}
		}
		synchronized(bills) {	
			for(Bill b: bills) {
				if(b.bs==BillState.paid) {
					RemoveBill(b);
					return true;
				}
			}
		}
		//if there's an order, cook it 

		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}
	
	private void RemoveBill(Bill b) {
		cashier.msgThanksForPayingBill(this,b.cost);
		bills.remove(b);
	}
	
	private void FulfillRequest(final Request r){
		if(foods.get("Steak")>=r.desire.get("Steak")&&foods.get("Chicken")>=r.desire.get("Chicken")&&foods.get("Salad")>=r.desire.get("Salad")&&foods.get("Pizza")>=r.desire.get("Pizza")) {
			r.cost+=(double)r.desire.get("Steak")*steakCost;
			r.cost+=(double)r.desire.get("Chicken")*chickenCost;
			r.cost+=(double)r.desire.get("Salad")*saladCost;
			r.cost+=(double)r.desire.get("Pizza")*pizzaCost;
			r.rs=RequestState.fulfilling;	
			foods.put("Steak",foods.get("Steak")-r.desire.get("Steak"));
			foods.put("Chicken",foods.get("Chicken")-r.desire.get("Chicken"));
			foods.put("Salad",foods.get("Salad")-r.desire.get("Salad"));
			foods.put("Pizza",foods.get("Pizza")-r.desire.get("Pizza"));
			timer.schedule(new TimerTask() {
				public void run() {		
					ShipOrder(r.desire,r.cost);
					r.rs=RequestState.done;
					
				}	
			},
			45000);
		}
		else {
			foodscost+=(double)foods.get("Steak")*steakCost;
			foodscost+=(double)foods.get("Chicken")*chickenCost;
			foodscost+=(double)foods.get("Salad")*saladCost;
			foodscost+=(double)foods.get("Pizza")*pizzaCost;
			print("Can't fulfill order, but I'll give what I can");
			cook.msgCantMakeMarketOrder(this,foods);
			if(foodscost>0) {
				
				timer.schedule(new TimerTask() {
					public void run() {		
						ShipOrder(foods,foodscost);					
					}	
				},
				25000);
			}
			else {
				print("Sorry I'm all out");
			}
			//GIVE WHAT YOU CAN
			requests.remove(r);
		}		
	}
	
	private void ShipOrder(HashMap<String,Integer> shipment,double bill) {
		bill+=0.0;
		print("Order fulfilled");
		cook.msgMarketOrderResponse(this,shipment);
		Bill a = new Bill(bill);
		bills.add(a);
		print("Cashier, here's the bill " + bill);
		cashier.msgHereIsMarketBill(this,bill);
		
	}

	// Actions

	//utilities


	
}

