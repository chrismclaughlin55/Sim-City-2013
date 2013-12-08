package restaurantSM;

import agent.Agent;
import java.util.*;
import java.util.concurrent.Semaphore;
import restaurantSM.utils.*;

public class HostAgent extends Agent {
	
	List<Table> tables;
	static final int NTABLES = 3;
	List<CustomerAgent> waitingCustomers = new ArrayList<CustomerAgent>();
	List<WaiterAgent> waiters = new ArrayList<WaiterAgent>();
	List<CustomerAgent> fullCustomers = new ArrayList<CustomerAgent>();
	String name;
	
	public HostAgent(String n){
		name = n;
		tables = new ArrayList<Table>(NTABLES);
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new Table(ix));
		}
	}

	public List<WaiterAgent> getWaiters(){
		return waiters;
	}
	
	public String getName() {
		return name;
	}
	
	public void msgAskForBreak(WaiterAgent w){
		if (waiters.size() > 1){
			waiters.remove(w);
			w.msgGoOnBreak();
			Do("break approved");
		}
		else {
			Do("break denied");
		}
		stateChanged();
	}
	
	public void msgIWillWait(CustomerAgent c) {
		waitingCustomers.add(c);
		stateChanged();
	}
	
	public void msgDoneWithBreak(WaiterAgent w) {
		this.addWaiter(w);
		stateChanged();
	}
	
	public void addWaiter(WaiterAgent w) {
		waiters.add(w);
		stateChanged();
	}
	
	public boolean restIsFull(){
		for (Table table : tables){
			if (!table.isOccupied()){
				return false;
			}
		}
		return true;
	}
	
	public void msgIWantFood(CustomerAgent c){
		if (!restIsFull()) {
			waitingCustomers.add(c);
		}
		else {
			fullCustomers.add(c);
		}
		stateChanged();
	}
	
	public void msgTableIsClear(Table t){
		t.setUnoccupied();
		stateChanged();
	}
	
	protected boolean pickAndExecuteAnAction() {
		
		if (!fullCustomers.isEmpty()) {
			askToWait(fullCustomers.get(0));
			fullCustomers.remove(0);
			return true;
		}
		
		if (!waitingCustomers.isEmpty()) {	
			for (Table table : tables){
				if (!table.isOccupied()) {
					seatCustomer(waitingCustomers.get(0), table);
					return true;
				}
			}
		}
		return false;
	}
	
	private void askToWait(CustomerAgent customer){
		customer.msgWaitPlease();
	}
	
	private void seatCustomer(CustomerAgent customer, Table table) {
		if (!waiters.isEmpty()){
			WaiterAgent min = waiters.get(0);
			for (WaiterAgent w : waiters){
				if (w.getNumCustomers() < min.getNumCustomers()){
					min = w;
				}
			}
			min.msgSeatAtTable(customer, table);
			table.setOccupant(customer);
			waitingCustomers.remove(customer);
		}
	}
	
}

