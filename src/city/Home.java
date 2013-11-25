package city;

import java.util.*;

import city.Building.BuildingType;
import mainGUI.MainGui;

public class Home extends Building {
	
	public HomeGui homeGui;
	CityData cityData;
	
	//constructor for non-apartment homes
	public Home(int xPos, int yPos, int width, int height, MainGui mainGui) {
		super(xPos, yPos, width, height, mainGui);
		homeGui = new HomeGui();
	}

	public Home(int i, int j, int w, int h, String string,
			BuildingType home, MainGui mainGui, CityData cd) {
		super(i, j, w, h, string, home, mainGui);
		homeGui = new HomeGui();
		cityData = cd;
		
	}

	public void EnterBuilding(PersonAgent p, String s){
		//if (p.equals(manager)) { 
			//allow the person into the building
			
		//}
		homeGui.getHomePanel().addGui(p.getGui());
		cityData.removeGui(p.getGui());
	}
}
