package city;

import java.util.*;

import city.Building.BuildingType;
import mainGUI.MainGui;

public class Apartment extends Building {

	List<PersonAgent> tenants;
	List<Room> rooms = new ArrayList<Room>();
	public ApartmentGui apartmentGui;
	
	public Apartment(int xPos, int yPos, int width, int height, MainGui mainGui) {
		super(xPos, yPos, width, height, mainGui);
		
	}
	
	public Apartment(int i, int j, int wIDTH, int hEIGHT, String string,
			BuildingType apartment, MainGui mainGui, CityData cd) {
		super(i, j, wIDTH, hEIGHT, string, apartment, mainGui);
		apartmentGui = new ApartmentGui();
		for (int k = 0; k < 8; k++) {
			Room r = new Room(this, k);
			rooms.add(r);
			apartmentGui.getAptPanel().addRoomGui(r.getGui());
		}
		
		cityData = cd;
		
	}
	
	public void EnterBuilding(PersonAgent p, String s) {
		apartmentGui.getAptPanel().addGui(p.getGui());
		cityData.removeGui(p.getGui());
	}
	
	public Room getRoom(int rn) {
		return rooms.get(rn);
	}
	
	public boolean isFull() {
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
