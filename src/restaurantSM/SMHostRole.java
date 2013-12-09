package restaurantSM;

import agent.Agent;

import java.util.*;
import java.util.concurrent.Semaphore;

import city.PersonAgent;
import city.Role;
import restaurantSM.utils.*;

public class SMHostRole extends Role {
	
	List<Table> tables;
	static final int NTABLES = 3;
	List<SMCustomerRole> waitingCustomers = Collections.synchronizedList(new ArrayList<SMCustomerRole>());
	List<SMWaiterRole> waiters = Collections.synchronizedList(new ArrayList<SMWaiterRole>());
	List<SMCustomerRole> fullCustomers = new ArrayList<SMCustomerRole>();
	String name;
	
	public SMHostRole(PersonAgent p, List<SMCustomerRole> custs, List<SMWaiterRole> waits){
		super(p);
		name = p.getName();
		waitingCustomers = custs;
		waiters = waits;
		tables = new ArrayList<Table>(NTABLES);
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new Table(ix));
		}
	}

	public List<SMWaiterRole> getWaiters(){
		return waiters;
	}
	
	public String getName() {
		return name;
	}
	
	public void msgAskForBreak(SMWaiterRole w){
		if (waiters.size() > 1){
			waiters.remove(w);
			w.msgGoOnBreak();
		}
		stateChanged();
	}
	
	public void msgIWillWait(SMCustomerRole c) {
		waitingCustomers.add(c);
		stateChanged();
	}
	
	public void msgDoneWithBreak(SMWaiterRole w) {
		this.addWaiter(w);
		stateChanged();
	}
	
	public void addWaiter(SMWaiterRole w) {
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
	
	public void msgIWantFood(SMCustomerRole c){
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
	
	public boolean pickAndExecuteAnAction() {
		
		if (!fullCustomers.isEmpty()) {
			askToWait(fullCustomers.get(0));
			fullCustomers.remove(0);
			return true;
		}
		
		synchronized(waitingCustomers) {
			if (!waitingCustomers.isEmpty()) {	
				for (Table table : tables){
					if (!table.isOccupied()) {
						seatCustomer(waitingCustomers.get(0), table);
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private void askToWait(SMCustomerRole customer){
		customer.msgWaitPlease();
	}
	
	private void seatCustomer(SMCustomerRole customer, Table table) {
		if (!waiters.isEmpty()){
			SMWaiterRole min = waiters.get(0);
			for (SMWaiterRole w : waiters){
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

