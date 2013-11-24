package city;

import java.util.*;

import mainGUI.MainGui;

public class Home extends Building {
	
	HomeGui homeGui;
	
	//constructor for non-apartment homes
	public Home(int xPos, int yPos, int width, int height, MainGui mainGui) {
		super(xPos, yPos, width, height, mainGui);
		homeGui = new HomeGui();
	}

	public void EnterBuilding(PersonAgent p, String s){
		//if (p.equals(manager)) { 
			//allow the person into the building
			
		//}
		p.getGui().setPresent(true); System.err.println("Present = true");
		homeGui.getHomePanel().addGui(p.getGui()); System.err.println("added to home panel");
	}	
}
