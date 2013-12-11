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
import city.Grid;
import city.PersonAgent;
import city.Building;
import city.RGrid;
import city.RGrid.Direction;
import city.CityData;

public class PersonGui implements Gui{
	
	private PersonAgent agent = null;
	private boolean isPresent = true;
	private boolean crossRoad = false;
	boolean moving = false;
	boolean inCar = false;
	
	private static final int INITX = 40;
	private static final int INITY = 40;
	public static final int WIDTH = 20;

	Direction d;
	CityData cd;
	private int xPos, yPos;
	private int xDestination, yDestination;
	private int xNextGrid,yNextGrid;
	
	public static final int xBuilding[] = {60, 60, 60, 60, 240, 380, 560, 560, 560, 560, 380, 240, 260, 400, 260, 400, 260, 400, 240, 380, 60, 560};
	public static final int yBuilding[] = {200, 320, 480, 600, 740, 740, 600, 480, 320, 200, 60, 60, 200, 200, 320, 320, 600, 600, 480, 480, 740, 740};
	public static final int xStop[] = {120, 120, 120, 120, 260, 400, 500, 500, 500, 500, 440, 300};
	public static final int yStop[] = {200, 320, 480, 600, 680, 680, 600, 480, 320, 200, 60, 60};
	
	MainGui gui;

	public PersonGui(PersonAgent p, MainGui g){ //HostAgent m) {
		agent = p;
		gui = g;
		xPos = xBuilding[agent.getHomeNumber()]-50;
		yPos = yBuilding[agent.getHomeNumber()]+10;
		cd=agent.cityData;
		xDestination = xBuilding[agent.getHomeNumber()]-50;
		yDestination = yBuilding[agent.getHomeNumber()]+10;
	}
	
	public int getX() {
		return xPos;
	}
	public int getY() {
		return yPos;
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
			if(agent.driving) {
				//move like a car
			}
			//MOVE LIKE A CAR DOES WHICH MOVES LIKE A BUS DOES.
			
			
		}
		
		else {
			//agent.acquireGridSemaphore((RGrid)cd.cityGrid[cd.busStops.get(0).getX()/20][cd.busStops.get(0).getX()/20]);
			if(agent.walking) {
				
				if (xPos < xNextGrid)
					xPos++;
				else if (xPos > xNextGrid)
					xPos--;
				if (yPos < yNextGrid)
					yPos++;
				else if (yPos > yNextGrid)
					yPos--;
				if(xPos==xNextGrid&&yPos==yNextGrid) {
					agent.msgDoneGridding();
				}
			}
			else {
				if(xPos<xDestination)
					xPos++;
				else if (xPos > xDestination)
					xPos--;
				if (yPos < yDestination)
					yPos++;
				else if (yPos > yDestination)
					yPos--;
			}
			
			//agent.currGrid = cd.getNextGrid(agent.currGrid,d);
			//GRIDDING PERSONAGENT SEMAPHORE, acquire
			
			//if(agent.currGrid instanceof RGrid) {
				
				//acquire cd.getNextGrid(agent.currGrid, d);
				//AND acquire cd.getNextGrid(cd.getNextGrid(agent.currGrid, d),d);
			//}

			
			//eventually the coordinates
			//yPos go to yNextGrid
			
			//if you hit next grid, go to next
			/*if(nextGrid is !RGrid, keep going) {
				
				person
					needs to acquire grid semaphores
				persongui
					needs to tell 
			}
			else {
				find closest not bgrid walk towards it 
				
				acquire that grid's semaphore, and continue walking
				"get next grid" will be based on their walking position
				use same calculate direction to decide which way they want t ogo
				if next grid they want to go is a BGrid, don't allow it, if it's an RGrid
				make them acquire the semaphore first, and then go.
			}*/
			/*
			if (xPos < xDestination)
				xPos++;
			else if (xPos > xDestination)
				xPos--;
	
			if (yPos < yDestination)
				yPos++;
			else if (yPos > yDestination)
				yPos--;
			*/
			for (int i = 0; i < 20; i++) {
	        	if (xPos == xDestination && yPos == yDestination
	        			& (xDestination == xBuilding[i]) & (yDestination == yBuilding[i])) {
	        		agent.msgAtBuilding();
	        		agent.walking = false;
	        	}
	        }
			if (xPos == xDestination && yPos == yDestination && moving == true) {
	            moving = false;
	            agent.msgDoneMoving();
	            agent.walking = false;
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
	
	public void MoveToNextGrid(Grid g) {
		d = CalculateDirection();
		agent.currGrid = cd.getNextGrid(g, d );
		xNextGrid = agent.currGrid.index1()*20;
		yNextGrid = agent.currGrid.index2()*20;
		//System.out.println(xNextGrid);
		//System.out.println(yNextGrid);
		//System.out.println(d);
		if(agent.currGrid instanceof RGrid) {
			//SOMETHING'S AMISS HERE.... BECAUSE RGRID...
			//
			//System.out.println("");
			agent.crossingRoad = true;
			agent.currRGrid = (RGrid) agent.currGrid;
			agent.nextRGrid = (RGrid) cd.getNextRGrid(agent.currGrid,d);
			//System.out.println(agent.currRGrid.index1()*20);
			//fSystem.out.println(agent.currRGrid.index2()*20);
			//THIS IS JUST A REGULAR GRID agent.nextRGrid =  cd.getNextRGrid(agent.currRGrid,d);
			agent.msgDoneGridding();
		}

	}
	
	public void crossRoad() {
		xNextGrid = cd.getNextGrid((Grid)agent.nextRGrid,d).index1()*20;
		yNextGrid = cd.getNextGrid((Grid)agent.nextRGrid,d).index2()*20;
		agent.currGrid = cd.getNextGrid((Grid)agent.nextRGrid, d);
		agent.crossingRoad = false;
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
		//WALK VS. CAR
		xDestination = xBuilding[buildingNumber];
		yDestination = yBuilding[buildingNumber];
		//BUT IN HERE , WE MUST DO SOMETHING SPECIAL...
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
	public Direction CalculateDirection() {
		if(xPos-xDestination>=0) {
			if(xPos-xDestination>Math.abs(yPos-yDestination)) {
				return Direction.west;
			}
			else if(yPos-yDestination>=0) {
				return Direction.north;
			}
			else {
				return Direction.south;
			}
		}
		else {
			if(Math.abs(xPos-xDestination)>Math.abs(yPos-yDestination)) {
				return Direction.east;
			}
			else if(yPos - yDestination>0) {
				return Direction.north;
			}
			else {
				return Direction.south;
			}
		}
	}
}
