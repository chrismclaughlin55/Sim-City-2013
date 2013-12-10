package city.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.*;

import javax.swing.ImageIcon;

import Gui.Gui;
import mainGUI.MainGui;
import city.BusAgent;
import city.BusStopAgent;
import city.PersonAgent;
import city.Building;

public class PersonGui implements Gui{
	
	private PersonAgent agent = null;
	private boolean isPresent = true;
	boolean moving = false;
	boolean inCar = false;
	
	private static final int INITX = 40;
	private static final int INITY = 40;
	public static final int WIDTH = 20;

	private int xPos, yPos;
	private int xDestination, yDestination;
	
	public static final int xBuilding[] = {60, 60, 60, 60, 240, 380, 560, 560, 560, 560, 380, 240, 260, 400, 260, 400, 260, 400, 240, 380};
	public static final int yBuilding[] = {200, 320, 480, 600, 740, 740, 600, 480, 320, 200, 60, 60, 200, 200, 320, 320, 600, 600, 480, 480};
	public static final int xStop[] = {120, 120, 120, 120, 260, 400, 500, 500, 500, 500, 440, 300};
	public static final int yStop[] = {200, 320, 480, 600, 680, 680, 600, 480, 320, 200, 60, 60};
	
	MainGui gui;

	public PersonGui(PersonAgent p, MainGui g){ //HostAgent m) {
		agent = p;
		gui = g;
		xPos = xBuilding[agent.getHomeNumber()]-50;
		yPos = yBuilding[agent.getHomeNumber()]+10;
		xDestination = xBuilding[agent.getHomeNumber()]-50;
		yDestination = yBuilding[agent.getHomeNumber()]+10;
	}
	
	public void setXPos(int x) {
		xPos = x;
	}
	
	public void setYPos(int y) {
		yPos = y;
	}
	
	public void setXDes(int x) {
		xDestination = x;
	}
	
	public void setYDes(int y) {
		yDestination = y;
	}

	public void updatePosition() {
		
		if(inCar) {
			//MOVE LIKE A CAR DOES WHICH MOVES LIKE A BUS DOES.
			
			
		}
		
		else {
			
			/*if(nextGrid is !RGrid, keep going) {
				
			}
			else {
				find closest not bgrid walk towards it 
				
				acquire that grid's semaphore, and continue walking
				"get next grid" will be based on their walking position
				use same calculate direction to decide which way they want t ogo
				if next grid they want to go is a BGrid, don't allow it, if it's an RGrid
				make them acquire the semaphore first, and then go.
			}*/
			
			if (xPos < xDestination)
				xPos++;
			else if (xPos > xDestination)
				xPos--;
	
			if (yPos < yDestination)
				yPos++;
			else if (yPos > yDestination)
				yPos--;
			
			for (int i = 0; i < 20; i++) {
	        	if (xPos == xDestination && yPos == yDestination
	        			& (xDestination == xBuilding[i]) & (yDestination == yBuilding[i])) {
	        		agent.msgAtBuilding();
	        	}
	        }
			if (xPos == xDestination && yPos == yDestination && moving == true) {
	            moving = false;
	            agent.msgDoneMoving();
			}
			if (xPos == xDestination && yPos == yDestination
	        			& (xDestination == 360) & (yDestination == 20)) {
				agent.msgAtBed();
			}
			if (xPos == xDestination && yPos == yDestination
	        			& (xDestination == 0) & (yDestination == 340)) {
				agent.msgAtEntrance();
			}
		}
	}
	
	public void DoWalkToRGrid(int number) {
		//WALK TO cd.buildings.get(number).getClosestRGrid(); 
	}

	public void getInOrOutCar() {
		inCar=!inCar;
	}
	@Override
	public void draw(Graphics2D g) {
		if(!inCar) {
			Graphics2D g2 = (Graphics2D)g;
			ImageIcon person = new ImageIcon("res/person.png");
			g2.drawImage(person.getImage(), xPos, yPos, null);
            g2.drawString(agent.getName(), xPos, yPos);
		}
		else {
			Graphics2D g2 = (Graphics2D)g;
			ImageIcon person = new ImageIcon("res/car.png");
			g2.drawImage(person.getImage(),xPos,yPos,null);
		}
	}
	
	public void DoGoToEntrance() {
		moving = true;
		xDestination = 0;
		yDestination = 340;
	}
	
	public boolean isInBedroom() {
		return xPos >= 200;
	}
	
	public void DoGoToHallway() {
		moving = true;
		xDestination = 0;
		yDestination = 176;
	}
	
	public void DoGoToRefridgerator() {
		moving = true;
		xDestination = 30;
		yDestination = 30;
	}
	
	public void DoGoToBed() {
		moving = true;
		xDestination = 360;
		yDestination = 20;
	}
	
	public void DoGoToStove() {
		moving = true;
		xDestination = 105;
		yDestination = 25;
	}
	
	public void DoGoToWall() {
		moving = true;
		xDestination = 190;
		yDestination = 340;
	}
	
	public void DoAlmostWall() {
		moving = true;
		xDestination = 220;
		yDestination = 340;
	}
	
	public void DoReverseWall() {
		moving = true;
		xDestination = 160;
		yDestination = 340;
	}
	
	public void DoGoToCouch() {
		moving = true;
		xDestination = 170;
		yDestination = 190;
	}
	
	public void DoGoToRoom(int roomNumber) {
		moving = true;
		if (roomNumber < 4) {
			xDestination = 45 + 100*roomNumber;
			yDestination = 140;
		}
		else {
			xDestination = 40 + 100*(roomNumber - 4);
			yDestination = 217;
		}
	}

	@Override
	public boolean isPresent() {
		return isPresent;
	}
	
	public void setPresent(boolean p) {
		isPresent = p;
	}
	
	public void DoGoToBuilding(int buildingNumber) {
		xDestination = xBuilding[buildingNumber];
		yDestination = yBuilding[buildingNumber];
	}

	public void DoGoIntoBuilding() {
		gui.removeGui(this);
	}
	
	public void DoLeaveBuilding() {
		isPresent = true;
	}

	public void DoGoToBusStop(BusStopAgent destinationBusStop) {
		moving = true;
		xDestination = xStop[destinationBusStop.getStopNumber()];
		yDestination = yStop[destinationBusStop.getStopNumber()];
		
	}

	public void DoGoToBus(BusAgent bus) {
		moving = true;
		xDestination = bus.getX();
		yDestination = bus.getY();
		
	}
}
