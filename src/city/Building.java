package city;

import java.util.*;
import java.awt.geom.Rectangle2D;

public abstract class Building extends Rectangle2D.Double {
	String name;
	boolean isOpen;
	Manager manager;
	List<PersonAgent> waitingPeople;
	
	// need a list of all roles that have been in the building (for non-norm)
	List<Role> existedRoles = new ArrayList<Role>();
	
	// need a list of all roles
	List<Role> roles = new ArrayList<Role>();
	
	public Building(int xPos, int yPos, int width, int height) {
		super(xPos, yPos, width, height);
	}
	
	protected boolean msgIsItOpen() {
		return isOpen;
	}
	
	// assign relations between new comer and person in charge both ways
	protected void GoIntoBuilding(PersonAgent p) {
		p.msgAssignRole(manager.role);
		waitingPeople.add(p);
	}
	
	protected void ComeIntoBuilding() {
		manager = new Manager();
	}
	
	protected void LeaveBuilding(PersonAgent p) {
		waitingPeople.remove(p);
	}
	
	//only manager have access to the setOpen
	protected void setOpen() {
		isOpen = true;
	}
	
	public abstract boolean pickAndExecuteAnAction();
	
	class Manager {
		PersonAgent person;
		Role role;
		
		Manager() {
		}
	}
}