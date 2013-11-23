package city;

import java.util.*;
import java.awt.geom.Rectangle2D;

import mainGUI.BuildingPanel;
import mainGUI.MainGui;

public class Building extends Rectangle2D.Double {
	public String name;
	public BuildingType type;

	public enum BuildingType {home, apartment, restaurant, bank, market, room};
	boolean isOpen;
	protected PersonAgent manager;
	List<PersonAgent> waitingPeople;
	BuildingPanel buildingPanel;
	MainGui mainGui;
	// need a list of all roles that have been in the building (for non-norm)

	//private List<Role> roles = Collections.synchronizedList(new ArrayList<Role>());
	protected Map<PersonAgent, Role> existingRoles = Collections.synchronizedMap(new HashMap<PersonAgent, Role>());




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

	public void EnterBuilding(PersonAgent p, String roleRequest) {

		/* Each specific building that extends Building.java will have to override this function according to its needs/capabilities.
		 * 
		 * Here is a general outline of how that code should look using the Market as an example:
		 * 
		 * if (market.isOpen()) {
		 * 		if existingRoles.get(p) {
		 * 			p.msgAssignRole(existingRoles.get(p));
		 *		 }
		 * 	   else if (roleRequest.equals("customer")) {
		 *     		p.msgAssignRole(new MarketCustomerRole(p, manager));
		 *     }
		 *     else if (roleRequest.equals("employee)) {
		 *     		p.msgAssignRole(new MarketEmployeeRole(p, manager));
		 *     }
		 * }
		 * else {
		 * 		if (p.equals(manager)) {
		 * 			if existingRoles.get(p) {
		 * 				p.msgAssignRole(existingRoles.get(p));
		 *			 }
		 *			else
		 * 				p.msgAssignRole(new MarketManagerRole(p));
		 * 		}
		 * 		else msgAccessDenied()
		 * }
		 * 
		 * 
		 * 
		 * 
		 */
	}
	public boolean isOpen() {
		return isOpen;
	}
	
	public void setOpen(PersonAgent p) {
		if (p.equals(manager)) {
			isOpen = true;
		}
	}

	public void setClosed(PersonAgent p) {
		if (p.equals(manager)) {
			isOpen = false;
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