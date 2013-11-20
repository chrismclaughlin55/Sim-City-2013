package city;

import java.util.*;

import mainGUI.MainGui;

public class Apartment extends Building {

	List<PersonAgent> tenants;
	List<Room> rooms;
	
	public Apartment(int xPos, int yPos, int width, int height, MainGui mainGui) {
		super(xPos, yPos, width, height, mainGui);
		
	}
	
	public boolean isFull(){
		return tenants.size() >= 8;
	}

	//returns true or false based on success or failure
	public boolean addTenant(PersonAgent p) {
		if (isFull()) {
			return false;
		}
		else {
			tenants.add(p);
			return true;
		}
	}
	
}
