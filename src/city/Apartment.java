package city;

import java.util.*;

import javax.swing.JFrame;

import city.Building.BuildingType;
import mainGUI.MainGui;

public class Apartment extends Building {

	public List<Room> rooms = new ArrayList<Room>();
	public ApartmentGui apartmentGui;
	
	public Apartment(int xPos, int yPos, int width, int height, MainGui mainGui) {
		super(xPos, yPos, width, height, mainGui);
		super.type = BuildingType.apartment;
		
	}
	
	public Apartment(int i, int j, int wIDTH, int hEIGHT, String string,
			BuildingType apartment, MainGui mainGui, CityData cd) {
		super(i, j, wIDTH, hEIGHT, string, apartment, mainGui);
		for (int k = 0; k < 8; k++) {
			rooms.add(new Room(this, k));
			
		}
		apartmentGui = new ApartmentGui(rooms);
		for (int k = 0; k < 8; k++) {
			apartmentGui.getAptPanel().addRoomGui(rooms.get(k).getGui());
		}
		
		cityData = cd;
		
	}
	
	public void EnterBuilding(PersonAgent p, String s) {
		cityData.removeGui(p.getGui());
		apartmentGui.getAptPanel().addGui(p.getGui());
		apartmentGui.addName(p);
	}
	
	public void LeaveBuilding(PersonAgent p) {
		apartmentGui.getAptPanel().removeGui(p.getGui(), p.currentBuilding.buildingNumber);
		cityData.addGui(p.getGui(), p.currentBuilding.buildingNumber);
	}
	
	public Room getRoom(int rn) {
		return rooms.get(rn);
	}
	
	public int getRoomNumber(PersonAgent p) {
		for (int i = 0; i < 8; i++) {
			if (p == rooms.get(i).tenant) {
				return i;
			}
		}
		return -1;
	}

	public JFrame getBuildingGui() {
		return apartmentGui;
	}
	
	
	
}
