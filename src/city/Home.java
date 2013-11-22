package city;

import java.util.*;

public class Home extends Building {

	//temporary hack
	PersonAgent allowedPerson;
	
	
	public Home(int xPos, int yPos, int width, int height) {
		super(xPos, yPos, width, height);
	}

	public void GoIntoBuilding(PersonAgent p){
		if (p == allowedPerson) {
			//allow the person into the building
			
		}
	}
	
	
	
}
