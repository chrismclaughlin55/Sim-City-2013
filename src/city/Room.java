package city;

import java.util.*;

import city.Building.BuildingType;
import mainGUI.MainGui;

public class Room {
	
	public HomeGui homeGui;
	public Apartment apartment;
	public int roomNumber;
	public PersonAgent tenant;
	public boolean isOccupied = false;

	public Room(Apartment a, int rn) {
		apartment = a;
		roomNumber = rn;
		homeGui = new HomeGui();
	}

	public void EnterBuilding(PersonAgent p, String s){
		apartment.apartmentGui.getAptPanel().removeGui(p.getGui(), roomNumber);
		homeGui.getHomePanel().addGui(p.getGui());
	}
	
	public HomeGui getGui() {
		return homeGui;
	}
	
	public void setTenant(PersonAgent p) {
		isOccupied = true;
		tenant = p;
	}
	
	public void LeaveBuilding(PersonAgent p) {
		homeGui.getHomePanel().removeGui(p.getGui());
		apartment.apartmentGui.getAptPanel().addGui(p.getGui(), roomNumber);
	}
}
