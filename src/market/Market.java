package market;

import mainGUI.MainGui;
import city.Building;
import city.PersonAgent;

public class Market extends Building {

	public Inventory inventory = null;
	MarketManagerRole currentManager = null;
	private int marketNum;

	public Market(int xPos, int yPos, int width, int height, MainGui mainGui, int marketNum) {
		super(xPos, yPos, width, height, mainGui);
		this.marketNum = marketNum;
		inventory = new Inventory();
	}

	public void EnterBuilding(PersonAgent person, String roleRequest) {
		if (isOpen()) {
			if (existingRoles.get(person) != null) {
				person.msgAssignRole(existingRoles.get(person));
			}
			else if (roleRequest.equals("customer")) {
				MarketCustomerRole custRole = new MarketCustomerRole(person, currentManager, marketNum, person.thingsToOrder);
				person.msgAssignRole(custRole);
				existingRoles.put(person, custRole);
			}
		}
		else {
			if (person.equals(manager)) {
				if (existingRoles.get(person) != null) {
					person.msgAssignRole(existingRoles.get(person));
					currentManager = (MarketManagerRole) existingRoles.get(person);
				}
				else {
					currentManager = new MarketManagerRole(person, inventory, this);
					person.msgAssignRole(currentManager);
				}
			}
			//else p.msgAccessDenied()
		}

	}
}
