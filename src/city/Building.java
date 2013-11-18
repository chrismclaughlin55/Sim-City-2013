package city;

import java.util.*;
import java.awt.geom.Rectangle2D;
import city.*;

public class Building extends Rectangle2D.Double {
	String name;
	boolean isClosed;
	Manager manager;
	List<PersonAgent> waitingPeople;
	
	public Building(int xPos, int yPos, int width, int height) {
		super(xPos, yPos, width, height);
	}
	
	protected boolean msgIsItOpen() {
		if (isClosed)
			return false;
		else
			return true;
	}
	
	protected void msgGoIntoBuilding(PersonAgent p) {
		waitingPeople.add(p);
	}
	
	protected boolean pickAndExecuteAnAction() {
		if(!waitingPeople.isEmpty()) {
			if(isClosed) {
				LeaveBuilding(waitingPeople.get(0));
				return true;
			}
			else {
				GoIntoBuilding(waitingPeople.get(0));
				return true;
			}
		}
		return false;
	}
	
	protected void LeaveBuilding(PersonAgent p) {
		waitingPeople.remove(p);
	}
	
	protected void GoIntoBuilding(PersonAgent p) {
		p.msgAssignRole(manager.personRole);
		waitingPeople.remove(p);
	}
	
	//only manager have access to the setClosed
	protected void setClosed() {
		isClosed = true;
	}
	
	//assign customer/waiter/cook role
	
	protected void setManager(Manager m) {
		manager = m;
	}
	
	protected Manager getManager() {
		return manager;
	}
	
	class Manager {
		PersonAgent person;
		String name;
		Role personRole;
		
		Manager(Role role) {
			personRole = role;
		}
	}
}