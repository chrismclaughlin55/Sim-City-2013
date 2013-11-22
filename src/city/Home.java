package city;

import java.util.*;

import mainGUI.MainGui;

public class Home extends Building {

	//temporary hack
	PersonAgent allowedPerson;
	
	//constructor for non-apartment homes
	public Home(int xPos, int yPos, int width, int height, MainGui mainGui) {
		super(xPos, yPos, width, height, mainGui);
	}

	public void GoIntoBuilding(PersonAgent p){
		if (p.equals(manager)) { //fix once brian and kartik fix manager
			//allow the person into the building
			
		}
	}
	
	
	
}
