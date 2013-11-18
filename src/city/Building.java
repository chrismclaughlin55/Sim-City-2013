package city;

import java.util.*;
import java.awt.geom.Rectangle2D;

public class Building extends Rectangle2D.Double {
	String name;
	boolean isClosed;
	Manager manager;
	Map<String, Manager> managers = new HashMap<String, Manager>();
	List<PersonAgent> waitingPeople;
	
	public Building(int xPos, int yPos, int width, int height) {
		super(xPos, yPos, width, height);
	}
	
	public void msgCanIComeIn(PersonAgent p) {
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
	
	private void LeaveBuilding(PersonAgent p) {
		waitingPeople.remove(p);
	}
	
	private void GoIntoBuilding(PersonAgent p) {
		manager = managers.get(name);
		p.msgAssignRole(manager.personRole);
		waitingPeople.remove(p);
	}
	
	public void setClosed() {
		isClosed = true;
	}
	
	public void setManager(Manager m) {
		manager = m;
	}
	
	public Manager getManager() {
		return manager;
	}
	
	public void addManager(Manager m, String name) {
		managers.put(name, m);
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