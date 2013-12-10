package restaurantCM;

import agent.Agent;
import restaurantCM.myCust.*;
import restaurantCM.gui.CMWaiterGui;

import java.util.*;
import java.util.concurrent.Semaphore;

import city.PersonAgent;
import city.Role;




public class CMWaiterRole extends Role{
	//DATA
	public List<myCust> Customers = new ArrayList<myCust>();
	private CMWaiterGui myGui;
	private String name;
	private CMHostRole host;
	private CMCookRole cook;
	private Semaphore atTable = new Semaphore(0,true);
	private Semaphore atLobby = new Semaphore(0,true);
	private Semaphore atCook = new Semaphore(0,true);
	private Semaphore atHome = new Semaphore(0, true);
	private Semaphore shouldBreak = new Semaphore(0, true);
	private enum State {goToLobby, goToCook, None}
	public Menu menu = new Menu();
	private boolean onBreak = false;
	private boolean leave = false;
	private CMCashierRole cashier;
	//Messages
	public void msgSitAtTable(CMCustomerRole c, int tablenum, CMHostRole host){
		//DONE
		this.host = host;
		seatCustomer(c, tablenum);
		stateChanged();
	}
	public void msgImReadyToOrder(CMCustomerRole c){
		//DONE
		int index = findCustomer(c);
		Customers.get(index).state = AgentState.readyToOrder;
		stateChanged();
	}

	public void msgHeresMyChoice(String choice, CMCustomerRole c){
		int index = findCustomer(c);
		Customers.get(index).state = AgentState.sendOrderToCook;
		Customers.get(index).setChoice(choice);
		stateChanged();
	}

	public void msgOrderIsReady(String choice, CMCustomerRole c){
		int index = findCustomer(c);
		Customers.get(index).state = AgentState.orderReady;
		stateChanged();
	}

	public void msgDoneAndLeaving(CMCustomerRole c){
		print("recieved msgDoneAndLeaving from "+ c.getCustomerName());
		int index = findCustomer(c);
		Customers.get(index).state = AgentState.done;
		stateChanged();
	}
	public void msgBadOrder(CMCustomerRole c){
		int index =findCustomer(c);
		myCust C = Customers.get(index);
		C.state = AgentState.badOrder;
		stateChanged();
	}
	public void msgNoBreak() {
		onBreak = false;
		print("no break");
		shouldBreak.release();
		stateChanged();

	}
	public void msgGoOnBreak(){
		onBreak = true;
		stateChanged();
	}
	public void msgAskForBill(CMCustomerRole c) {
		print(c.getCustomerName()+" asked for bill");
		Customers.get(findCustomer(c)).state = AgentState.askedForBill;
		stateChanged();
	}
	public void msgPayBill(CMCustomerRole c, double total){
		int i = findCustomer(c);
		Customers.get(i).setTotalBill(total);
		Customers.get(i).state = AgentState.needsToPay;
		stateChanged();
	}

	//ANIMATION MESSAGES

	public void msgDoneAtTable(){
		if(atTable.availablePermits()==1){return;}
		atTable.release();
		myGui.carryOrder=true;
	}
	public void msgAtLobby(){
		if(atLobby.availablePermits()==1){return;}
		atLobby.release();
	}
	public void msgAtCook(){
		if(atCook.availablePermits()==1){return;}
		atCook.release();
		myGui.carryOrder=true;
	}
	public void msgAtHome(){
		if(atHome.availablePermits()==1){return;}
		atHome.release();
	}
	
	//Actions
	public CMWaiterRole(PersonAgent person){
		super(person);
		print("waiter created");
		this.name = person.getName();
		atLobby.drainPermits();
	}

	private int findCustomer(CMCustomerRole c){
		for( myCust C : Customers){
			if(C.getC().equals(c)){
				return Customers.indexOf(C);

			}
		}
		return -1;
	}
	private void animationGoToTable(int index){
		myGui.DoGoToTable(index);
		try {
			print("going to table"+index);
			atTable.acquire();
			print("acquired table"+index);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
	}
	private void animationGoToHome() {
		myGui.DoGoToHome();
		try {
			print("going home");
			atHome.acquire();
			print("leaving home acquired");
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
	}
		
	
	private void animationGoToLobby(){
		myGui.DoGoToLobby();
		try {
			print("going to lobby");
			atLobby.acquire();
			print("at lobby acquired");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	private void animationGoToCook() {
		myGui.DoGoToCook();
		try {
			print("going to cook");
			atCook.acquire();
			print("at cook acquired");
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
	}

	private void seatCustomer(CMCustomerRole c, int tablenum) {

		Customers.add(new myCust(tablenum, null, c));
	}
	private void followMeToTable(myCust C){
		C.state = AgentState.seated;
		animationGoToLobby();
		C.getC().msgFollowMeToTable(C.getTablenum(), this);
		animationGoToTable(C.getTablenum());
	}
	private void whatWouldYouLike(myCust C){
		C.getC().msgWhatWouldYouLike(menu);
		C.state = AgentState.waitingForOrder;
		animationGoToTable(C.getTablenum());
	}
	public void msgHereIsBill(CMCustomerRole c, double totalBill) {
		Customers.get(findCustomer(c)).setTotalBill(totalBill);
		Customers.get(findCustomer(c)).state =AgentState.haveBill;
		stateChanged();
	}
	private void orderToCook(String choice, CMCustomerRole c) {
		int index = findCustomer(c);
		myCust C = Customers.get(index);
		C.state = AgentState.foodCooking;
		animationGoToCook();
		print("going to cook");
		cook.msgCookOrder(this, C.getChoice(), C.getTablenum(), C.getC());
	}


	private void deliverOrder(String choice, CMCustomerRole c) {
		int index = findCustomer(c);
		int tablenum = Customers.get(index).getTablenum();
		animationGoToTable(tablenum);
		print("delivering order to "+ c.getCustomerName());
		try {
			Thread.sleep(1000, 0);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
		c.msgHereIsYourFood(choice);

	}

	private void clearTable(CMCustomerRole c) {
		int tablenum = Customers.get(findCustomer(c)).getTablenum();
		animationGoToTable(tablenum);
		host.msgLeavingTable(c);
		animationGoToLobby();
		print("clearing table "+ tablenum);
	}	
	private void goOnBreak() {
		host.msgImOnBreak(this);

		try {
			shouldBreak.acquire();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		animationGoToLobby();
		if(onBreak){
			try {
				Thread.sleep(15000, 0);
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
			host.msgImOffBreak(this);
			this.onBreak = false;
		}
	}

	//Scheduler
	@Override
	public boolean pickAndExecuteAnAction() {
		boolean shouldGoOnBreak = true;
		for(myCust C : Customers)
			if(C.state != AgentState.left )
				shouldGoOnBreak = false;
		for(myCust C : Customers)
			if(C.state == AgentState.orderReady){
				deliverOrder(C.getChoice(), C.getC());
				C.state = AgentState.eating;
				return true;
			}
		for(myCust C : Customers)
			if(C.state == AgentState.badOrder){
				C.state = AgentState.readyToOrder;
				badOrder(C.getC());
				return true;
			}
		for(myCust C : Customers)
			if(C.state == AgentState.sendOrderToCook){
				orderToCook(C.getChoice(), C.getC());
				return true;
			}
		for(myCust C : Customers)
			if(C.state == AgentState.readyToOrder){
				whatWouldYouLike(C);
				return true;
			}
		for(myCust C : Customers)
			if(C.state == AgentState.waiting){
				followMeToTable(C);

				return true;
			}
		for(myCust C : Customers)
			if(C.state == AgentState.askedForBill){
				cashierCalcOrder(C);
				C.state = AgentState.sentBill;
				return true;
			}
		for(myCust C: Customers)
			if(C.state == AgentState.haveBill){
				giveBill(C);
				C.state = AgentState.needsToPay;
				return true;
			}
		for(myCust C: Customers)
			if(C.state == AgentState.needsToPay){
				payBill(C);
				C.state = AgentState.done;
				return true;
			}
		for(myCust C : Customers)
			if(C.state == AgentState.done){
				clearTable(C.getC());
				C.state = AgentState.left;
				return true;
			}


		if(shouldGoOnBreak && onBreak){
			print("going on break");
			goOnBreak();
			return true;
		}
		if(leave){
			print("time to leave");
			leave();
		}
		
		animationGoToHome();
		return false;
	}

	
	private void leave() {
		this.myGui.DoGoToLobby();
		myGui.setPresent(false);
		person.exitBuilding();
		person.msgDoneWithJob();
		doneWithRole();	
		leave = false;
	}
	private void cashierCalcOrder(myCust C) {
		cashier.msgCalcOrder(this, C.getC(), C.getChoice());
		print("calculate order for "+C.getC().getCustomerName());
	}
	private void payBill(myCust c) {
	cashier.msgGiveMeBill(this, c.getC(),c.getTotalBill());
	print(c.getC().getName()+" paid bill amount of "+ c.getTotalBill());
	}
	private void giveBill(myCust c) {
		c.getC().msgHereIsBill(c.getTotalBill());
		print("total bill for "+c.getC().getCustomerName()+" is "+c.getTotalBill());

	}
	private void badOrder(CMCustomerRole C) {
		int index = findCustomer(C);
		myCust c= Customers.get(index);
		animationGoToTable(c.getTablenum());
		c.getC().msgBadOrder();
		print("msg bad order " + c.state+" "+ c.getC().getCustomerName());

	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public CMCookRole getCook() {
		return cook;
	}
	public void setCook(CMCookRole cook) {
		this.cook = cook;
	}
	public CMWaiterGui getMyGui() {
		return myGui;
	}
	public void setMyGui(CMWaiterGui myGui) {
		this.myGui = myGui;
	}
	public boolean isOnBreak() {
		return onBreak;
	}
	public void setOnBreak(boolean onBreak) {
		this.onBreak = onBreak;
	}
	public void setCashier(CMCashierRole cashier) {
		this.cashier = cashier;

	}
	public void setHost(CMHostRole host){
		this.host = host;
	}
	public void msgLeave() {
		leave = true;
		stateChanged();
		
	}




}
