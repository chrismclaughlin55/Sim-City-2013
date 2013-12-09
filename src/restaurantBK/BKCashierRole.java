package restaurantBK;

import agent.Agent;
//import restaurant.gui.WaiterGui;










import java.util.*;
import java.util.concurrent.Semaphore;

import city.Role;
import restaurantBK.BKCustomerRole.AgentEvent;
import restaurantBK.gui.RestaurantPanel;
import restaurantBK.interfaces.Cashier;
import restaurantBK.interfaces.Customer;
import restaurantBK.interfaces.Market;
import restaurantBK.interfaces.Waiter;
import restaurantBK.test.mock.EventLog;
import city.PersonAgent;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class BKCashierRole extends Role implements Cashier {
	
	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.
	//Later we will see how it is implemented
	
	RestaurantPanel rest;
	public Timer timer = new Timer();
	public EventLog log = new EventLog();
	private String name;
	public double capital;
	public List<Shipment> ships = Collections.synchronizedList(new ArrayList<Shipment>());
	public class Shipment {
		public Market m;
		public double bill;
		public ShipmentState ss;
		public Shipment(Market mark, double cost) {
			m=mark;
			bill=cost;
			ss=ShipmentState.pending;
		}
	}
	public enum ShipmentState {pending, beingpaidfor, cantpay, paid};
	public class Check{
		public Customer c;
		public Waiter w;
		String name;
		public CheckState chs;
		public double price;
		double moneyGiven;
		int tablenumber;
		public Check( Customer c, Waiter w, double money, int tn) {
			//this.name=choice;
			this.w=w;
			this.c=c;
			this.price=money;
			this.tablenumber=tn;
			this.chs=CheckState.pending;
		}
	}
	public Map<Customer,Double> oweme;
	public List<Check> checks = Collections.synchronizedList(new ArrayList<Check>());
	public enum CheckState {pending,delivered,beingPaid,paid};
	public BKCashierRole(PersonAgent person, String name, RestaurantPanel rest) {//, PersonAgent person) {
		super(person);
		this.rest = rest;
		//getPersonAgent, super(Person)
		oweme = new HashMap<Customer,Double>();
		this.name = name;
		capital=500.00;
	}

	/* (non-Javadoc)
	 * @see restaurant.Cashier#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	
	// Messages
	/* (non-Javadoc)
	 * @see restaurant.Cashier#msgMakeCheck(restaurant.CustomerAgent, restaurant.WaiterAgent, double, int)
	 */
	@Override
	public void msgMakeCheck(Customer c, Waiter w, double price, int tablenumber) {
		Check ch = new Check(c,w,price,tablenumber);
		checks.add(ch);
		stateChanged();
		
	}
	
	public void msgHereIsMarketBill(Market m, double cost) {
		print("Got Market bill");
		Shipment a = new Shipment(m,cost);
		ships.add(a);
		stateChanged();
	}
	
	public void msgThanksForPayingBill(Market m,double cost) {
		synchronized(ships) {
			for(Shipment s:ships) {
				if(s.m==m&&s.bill==cost) {
					print("Confirmed that I paid for bill. Money left: "+capital);
					s.ss=ShipmentState.paid;
					stateChanged();
				}
			}
		}
	}
	/* (non-Javadoc)
	 * @see restaurant.Cashier#msgTakeMyMoney(double, restaurant.CustomerAgent)
	 */
	@Override
	public void msgTakeMyMoney(double money,Customer c) {
		synchronized(checks) {
			for(Check ch: checks) {
				if(ch.c==c) {
					if(oweme.containsKey(c)) {
						ch.price+=oweme.get(c);
					}
					ch.moneyGiven=money;
					ch.chs=CheckState.beingPaid;	
					stateChanged();
				}
			}
		}		
		//here's the money I have okay
	}	

	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		/* Think of this next rprint("HELLO?");ule as:
            Does there exist a table and customer,
            so that table is unoccupied and customer is waiting.
            If so seat him at the table.
		 */
		//print("HELLO?");
		synchronized(checks) {
			for (Check check : checks) {
				if (check.chs==CheckState.pending) {
					//print("Is it me you're looking for?");
					{
						ComputeIt(check);
						return true;//return true to the abstract agent to reinvoke the scheduler.
					}
				}
			}
		}
		synchronized(checks) {
			for(Check check: checks) {
				if(check.chs==CheckState.beingPaid) {
					Transaction(check);
					return true;
				}			
			}
		}
		//if there's an order, cook it 
		synchronized(checks) {
			for(Check ch :checks) {
				if(ch.chs == CheckState.paid) {
					checks.remove(ch);
					return true;
				}
			}
		}
		synchronized(ships) {
			for(Shipment s: ships) {
				if(s.ss==ShipmentState.cantpay) {
					if(capital>=s.bill) {
						s.ss=ShipmentState.beingpaidfor;
						PayForShipment(s);
						return true;
					}
				}
			}
		}
		synchronized(ships) {
			for(Shipment s: ships) {
				if(s.ss==ShipmentState.pending) {
					s.ss=ShipmentState.beingpaidfor;
					PayForShipment(s);
					return true;
				}
			}
		}
		synchronized(ships) {
			for(Shipment s: ships) {
				if(s.ss==ShipmentState.paid) {
					ships.remove(s);
					return true;
				}
			}
		}
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}
	
	private void PayForShipment(Shipment s) {
		if(capital>=s.bill) {
			print("Paying the market bill");
			capital-=s.bill;
			s.m.msgPayingMarketBill(this, s.bill);
		}
		else {
			print("Can't pay the market right now");
			s.ss=ShipmentState.cantpay;
		}
	}
	private void Transaction(Check ch) {
		if(ch.moneyGiven<ch.price) {
			print("PUNK, YOU OWE ME MONEY NEXT TIME YOU COME BACK");
			oweme.put(ch.c,ch.price-ch.moneyGiven);
			ch.c.msgAllPaidPlusChange(0);
			ch.chs=CheckState.paid;
		}
		else {
			ch.chs=CheckState.paid;
			ch.c.msgAllPaidPlusChange(ch.price-ch.moneyGiven);
			capital+=ch.price;
			print("You're all set");
			if(oweme.containsKey(ch.c)) {
				oweme.remove(ch.c);
			}
		}
		
	}
	private void ComputeIt(Check ch){
		ch.w.msgCheckMade(ch.price,ch.tablenumber);
		ch.chs=CheckState.delivered;
		print("Waiter, here's the check");
	}
	

	// Actions

	//utilities


	
}

