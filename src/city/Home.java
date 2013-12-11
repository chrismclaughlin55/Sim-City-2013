package city;

import java.util.*;

import javax.swing.JFrame;

import city.Building.BuildingType;
import mainGUI.MainGui;

public class Home extends Building {
	
	public HomeGui homeGui;
	CityData cityData;
	
	//constructor for non-apartment homes
	public Home(int xPos, int yPos, int width, int height, MainGui mainGui) {
		super(xPos, yPos, width, height, mainGui);
		homeGui = new HomeGui();
		super.type = BuildingType.home;
	}

	public Home(int i, int j, int wIDTH, int hEIGHT, String string,
			BuildingType home, MainGui mainGui, CityData cd) {
		super(i, j, wIDTH, hEIGHT, string, home, mainGui);
		homeGui = new HomeGui();
		cityData = cd;
		super.type = BuildingType.home;
	}

	public void EnterBuilding(PersonAgent p, String s){
		//if (p.equals(manager)) { 
			//allow the person into the building
			
		//}
		cityData.removeGui(p.getGui());
		homeGui.getHomePanel().addGui(p.getGui());
	}
	
	public void LeaveBuilding(PersonAgent p) {
		homeGui.getHomePanel().removeGui(p.getGui(), p.currentBuilding.buildingNumber);
		cityData.addGui(p.getGui(), p.currentBuilding.buildingNumber);
	}

	public JFrame getBuildingGui() {
		return homeGui;
	}
}
