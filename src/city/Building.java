package city;

import java.util.*;
import java.awt.geom.Rectangle2D;

import mainGUI.BuildingPanel;
import mainGUI.MainGui;

public class Building extends Rectangle2D.Double {
	public String name;
	public BuildingType type;
	public enum BuildingType {home, apartment, restaurant, bank, market};
	boolean isOpen;
	PersonAgent manager;
	List<PersonAgent> waitingPeople;
	BuildingPanel buildingPanel;
	MainGui mainGui;
	// need a list of all roles that have been in the building (for non-norm)
	
	// need a list of all roles
	List<Role> roles = new ArrayList<Role>();
	
	public Building(int xPos, int yPos, int width, int height, MainGui mainGui) {
		super(xPos, yPos, width, height);
		this.mainGui = mainGui;
		buildingPanel = new BuildingPanel(mainGui);
	}
	
	public Building(int xPos, int yPos, int width, int height, String name, BuildingType type, MainGui mainGui) {
		super(xPos, yPos, width, height);
		this.name = name;
		this.type = type;
		this.mainGui = mainGui;
		buildingPanel = new BuildingPanel(mainGui);
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
	
	class Manager {
		PersonAgent person;
		Role role;
		
		Manager() {
		}
	}
	
	public void setType(BuildingType type) {
		this.type = type;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void display(Building building) {
		buildingPanel.displayBuildingPanel(building);
	}
}