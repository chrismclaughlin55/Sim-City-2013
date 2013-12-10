package restaurantCM;

import java.util.*;

import city.PersonAgent;
import city.Role;
import city.PersonAgent.BigState;
import restaurantCM.*;
import agent.Agent;




public class CMCookRole extends Role{
	//DATA
	private List<myMarket> Markets = new ArrayList<myMarket>();
	private String name;
	private List<Order> Orders = new ArrayList<Order>();
	private enum MarketState { notHave, have};
	private enum mstate {needspayment, good};
	private enum OrderState {badOrder, pending, cooking,ready, done };
	private boolean leave = false;
	private Timer timer= new Timer();
	private Map<String , Food> foodInventory = new HashMap<String , Food >();

	private Menu pricelookup = new Menu();
	private CMCashierRole cashier;
	private class myMarket{
		public mstate state;
		public double bill;
		//		public MarketAgent m;
		//		public HashMap<String, MarketState> inventory = new HashMap<String, MarketState>();
		//		public myMarket(MarketAgent m){
		//			this.state = mstate.good;
		//			this.m = m;
		//			for(String s : foodInventory.keySet()){
		//				this.inventory.put(s, MarketState.have);
		//			}
		//		}
	}
	private class Order{
		public CMWaiterRole w;
		public String choice;
		public int tablenum;
		public CMCustomerRole c;
		public OrderState state;
		public Order(CMWaiterRole w, String choice, int tablenum, CMCustomerRole c){
			this.w = w;
			this.c = c;
			this.choice = choice;
			this.tablenum = tablenum;
			this.state = OrderState.pending;

		}
		public Order(CMWaiterRole w, String choice, int tablenum, CMCustomerRole c, OrderState state){
			this.w = w;
			this.c = c;
			this.choice = choice;
			this.tablenum = tablenum;
			this.state = state;
		}
	}

	private class Food{
		public String choice;
		public int cookingTime;
		public int inventory;
		Food(String choice, int cookingTime){
			this.choice = choice;
			this.cookingTime = cookingTime;
			this.inventory = 3;
		}
	}
	//Actions
	public CMCookRole(PersonAgent person){
		super(person);
		this.name = name;
		print("cook created");
		foodInventory.put("chicken", new Food("chicken", 15));
		foodInventory.put("steak", new Food("steak", 20));
		foodInventory.put("pasta", new Food("pasta", 35));
		foodInventory.put("pizza", new Food("pizza", 30));
		foodInventory.put("donuts", new Food("donuts", 20));

	}
	private void cookIt(Order o){
		//DoCooking(0); TODO
		o.state = OrderState.cooking;
		timer.schedule(new FoodDone(o), foodInventory.get(o.choice).cookingTime*1000);
	}


	class FoodDone extends TimerTask{
		private Order o;
		public FoodDone(Order o){
			this.o = o;
		}
		@Override
		public void run() {
			msgFoodDone(o);			
		}

	}

	private void OrderDone(Order o) {
		//DoPlaceOnCounter(o) //Animation TODO
		o.w.msgOrderIsReady(o.choice, o.c);
		print("Order Done come here "+ o.w.getName());
		o.state = OrderState.done;
	}


	//	private myMarket findMarket(MarketAgent m){
	//		for(myMarket mm : Markets){
	//			if(mm.m.equals(m)){
	//				return mm;
	//				
	//			}
	//		}
	//		return null;
	//	}
	//	private void orderFromMarket(String s) {
	//		for( myMarket m : Markets){
	//			if(m.inventory.get(s) == MarketState.have){
	//				m.m.fillOrder(this, new ArrayList<String>(Arrays.asList(s)));
	//				
	//				stateChanged();
	//				return;
	//			}
	//		}
	//
	//	}
	private void badOrder(Order o) {
		o.w.msgBadOrder(o.c);
		Orders.remove(o);
	}

	//Messages
	public void msgThanks(){
		print("I was thanked");
		stateChanged();
	}
	public void msgAddCashier(CMCashierRole cashier) {
		this.cashier = cashier;
		stateChanged();
	}
	public void msgCookOrder(CMWaiterRole w, String choice, int tablenum, CMCustomerRole c){
		print("One order of "+choice);
		if(foodInventory.get(choice).inventory>0){
			Orders.add(new Order(w, choice, tablenum, c));
			foodInventory.get(choice).inventory--;
			if(foodInventory.get(choice).inventory <=2)

				print(choice + " left: " + foodInventory.get(choice).inventory);
		}
		else{ 
			print(choice + " inventory empty");
			print(c.getCustomerName()+" will have to order again");
			Orders.add(new Order(w, choice, tablenum, c, OrderState.badOrder));
			if(foodInventory.get(choice).inventory <=2)

				print(choice + " " + foodInventory.get(choice).inventory);
		}
		stateChanged();
	}
	public void msgFoodDone(Order o){
		o.state = OrderState.ready;
		stateChanged();
	}
	//	public void msgIDontHave(MarketAgent marketAgent, String choice) {
	//		myMarket m = findMarket(marketAgent);
	//		m.inventory.put(choice, MarketState.notHave);
	//		stateChanged();
	//	}
	//	public void msgHereIsOrder(String choice, Integer i, MarketAgent m) {
	//		this.foodInventory.get(choice).inventory+=i;
	//		print("recieved "+i+" more "+choice);
	//		if(i >= 5){
	//
	//		}
	//		else{ 
	//
	//			myMarket mm = findMarket(m);
	//			mm.inventory.put(choice, MarketState.notHave);
	//		}
	//		myMarket mm = findMarket(m);
	//		mm.state = mstate.needspayment;
	//		mm.bill = pricelookup.Menu.get(choice) * i / 2;
	//		print("cashier owes "+mm.m.getName()+" "+mm.bill+ " dollars");
	//		stateChanged();
	//	}
	//	public void msgAddMarket(MarketAgent m){
	//		Markets.add(new myMarket(m));
	//		stateChanged();
	//	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	//Scheduler
	@Override
	public boolean pickAndExecuteAnAction() {
		for( Order o : Orders){
			if(o.state == OrderState.badOrder){
				badOrder(o);
				print("made it here 3");
				return true;
			}
		}

		for( Order o : Orders){
			if(o.state == OrderState.ready){
				OrderDone(o);
				print("made it here 4");
				return true;
			}
		}
		for( Order o : Orders){
			if(o.state == OrderState.pending){
				cookIt(o);
				print("made it here 5");
				return true;
			}
		}
		if(leave){
			leave();
			return true;
		}
		return false;
	}
	//	private void tellCashier(myMarket mm) {
	//		cashier.msgPayThisMarket(mm.m, mm.bill);
	//		print("telling cashier to pay");
	//		mm.bill = 0;
	//		mm.state = mstate.good;
	//	}
	public Map<String, Food> getFoodInventory() {
		return foodInventory;
	}
	public void setFoodInventory(Map<String, Food> foodInventory) {
		this.foodInventory = foodInventory;
	}
	private void leave() {
		person.bigState = BigState.goHome;
		person.setHunger(0);
		person.exitBuilding();
		person.msgDoneWithJob();
		doneWithRole();	
		
		leave = false;
		print("leaving job");
	}
	public void msgLeave() {
		leave = true;
		stateChanged();

	}






}
