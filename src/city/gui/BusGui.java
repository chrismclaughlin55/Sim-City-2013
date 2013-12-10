package city.gui;

import java.awt.Graphics2D;

import javax.swing.ImageIcon;

import mainGUI.MainGui;
import Gui.Gui;
import city.CityData;
import city.RGrid;
import city.RGrid.Direction;
import city.interfaces.Bus;
import city.Grid;

public class BusGui implements Gui {
	CityData cd;
	Direction direction;
	private Bus agent = null;
	public int xPos;
	public int yPos;
	boolean xmove = false;
	boolean xFirst = false;
	boolean yFirst = false;
	private int xDestination, yDestination;//default start position
    boolean released = true;
    boolean moving = false;
    boolean isPresent = true;
    int stop;
    int gridding=0;
    int route;
    RGrid nextGrid, currGrid, prevGrid;
    MainGui m;

    public BusGui(Bus ba, MainGui main, CityData cd) {
    	this.cd = cd;
    	agent = ba;
    	if (agent.getRouteNumber() == 1) {
    		xPos = cd.busStops.get(0).getX();
    		yPos = cd.busStops.get(0).getY();
    	}
    	else if (agent.getRouteNumber() == 2) {
    		xPos = cd.busStops.get(12).getX();
    		yPos = cd.busStops.get(12).getY();
    	}
    	route = agent.getRouteNumber();
    	nextGrid = (RGrid)cd.cityGrid[xPos/20][yPos/20];
    	direction = nextGrid.direction;
    	agent.setCurrentGrid(nextGrid);
    	currGrid = nextGrid;
    	nextGrid = cd.getNextRGrid(nextGrid);
    	m = main;
    	//currGrid = ....;
    }
    
	public void updatePosition() {
		
		if(!moving)
		{
			return;
		}
		
		if(xPos == xDestination && yPos == yDestination
	        		&& (xDestination >= 0) && (yDestination >= 0)) {
	        	moving = false;
	            agent.msgAtDestination();
	    }
		
		/*if(!released && yPos % 20 == 0 && (currGrid.direction == Direction.north || currGrid.direction == Direction.south)) {
			moving = false;
			agent.msgAcquireGrid(nextGrid);
			prevGrid = currGrid;
			currGrid = nextGrid;
			nextGrid = cd.getNextRGrid(nextGrid);
			if(nextGrid == null) { //we are at an intersection
				if(xPos == xDestination) { //in line with target, no need to turn
					if(prevGrid.direction == Direction.north) {
						//select the space 3 spaces up
						nextGrid = (RGrid)cd.cityGrid[prevGrid.index1()][prevGrid.index2()-3];
					}
					else { //prevGrid.direction == Direction.south
						//select the space 3 spaces down
						nextGrid = (RGrid)cd.cityGrid[prevGrid.index1()][prevGrid.index2()+3];
					}
				}
				else {
					if(route == 1) { //outer route, left turn
						if(prevGrid.direction == Direction.north) {
							nextGrid = (RGrid)cd.cityGrid[prevGrid.index1()-2][prevGrid.index2()-2];
						}
						else { //prevGrid.direction == Direction.south
							nextGrid = (RGrid)cd.cityGrid[prevGrid.index1()+2][prevGrid.index2()-2];
						}
					}
					else { //route == 2, inner route, right turn
						if(prevGrid.direction == Direction.north) {
							nextGrid = (RGrid)cd.cityGrid[prevGrid.index1()+1][prevGrid.index2()-1];
						}
						else { //prevGrid.direction == Direction.south
							nextGrid = (RGrid)cd.cityGrid[prevGrid.index1()-1][prevGrid.index2()+1];
						}
					}
				}
			}
			return;
		}
		else if(!released && xPos % 20 == 0 && (currGrid.direction == Direction.east || currGrid.direction == Direction.west)) {
			moving = false;
			agent.msgAcquireGrid(nextGrid);
			prevGrid = currGrid;
			currGrid = nextGrid;
			nextGrid = cd.getNextRGrid(nextGrid);
			return;
		}
		else if(currGrid.direction == Direction.none)
		{
			moving = false;
			agent.msgAcquireGrid(nextGrid);
			prevGrid = currGrid;
			currGrid = nextGrid;
			nextGrid = cd.getNextRGrid(nextGrid);
			return;
		}
		else if(released)
			released = false;*/
		/////DO ALL OF THE BELOW CALCULATIONS IF GRIDDING == 0
		/*if(gridding==0) {
			
		}
		else {
			xPos+= or -= something based on currGrid type
			yPos+= or -= something based on currGrid type
			gridding++;
			gridding%=4;
		}*/
		
		//Check to see if we are crossing from one grid square to another
		
		
		//At intersection
		//direction = CalculateDirection();
		//System.out.println(direction);
		//String prevDir = currGrid.getDirection();
		//currGrid = (RGrid) cd.getNextRGrid(currGrid);
		/*if(currGrid.getDirection().equals("none")) {
			//DO SOMETHING SPECIAL
			if(direction==Direction.east) {
				//and prevDir == east, go straight
				 * if prevDir = north, turn right;
				 * if prevDir = south, turn left;
				 *
				 *SAME MECHANISM FOR CARS... but stuffed in persongui
				//do a left or right turn
				//if same, then go straight
				//ACQUIRE SPECIAL INTERSECTION semaphore and do the proper animation
			}
			
		}
		else {
			//ACQUIRE CURRGRID'S SEMAPHORE
			//GO IN SAME DIRECTION/
			//GO TO NEXT GRID
			//RELEASE THAT ISH
			//when and how does it then check for next grid?
		}*/
		
		// TODO Auto-generated method stub
		//keep going in direction 4 times until they have to release
		if(xFirst) {
			if (xPos < xDestination)
		       xPos+=5;
		    if (xPos > xDestination)
		       xPos-=5;
			if (xPos == xDestination) {
				xFirst = false;
			}
		}	
		if(yFirst) {
			if (yPos < yDestination)
			   yPos+=5;
			if (yPos > yDestination)
			   yPos-=5;
			if (yPos == yDestination) {
				yFirst = false;
			}
		}
		if(!(xFirst||yFirst)) {
			if (xPos < xDestination) {
				xPos+=5;
			}
			if (xPos > xDestination) {
				xPos-=5;
			}
			if (xPos == xDestination) {
				xFirst = false;
			}	
			if (yPos < yDestination) {
				yPos+=5;
			}
			if (yPos > yDestination) {
				yPos-=5;
			}
			if (yPos == yDestination) {
				yFirst = false;
			}
		}
		
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
	
	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		Graphics2D g2 = (Graphics2D)g;
		ImageIcon person = new ImageIcon("res/bus.png");
		g2.drawImage(person.getImage(), xPos, yPos, null);
	}


	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return isPresent;
	}
	
	public void DoGoToNextStop(int x, int y) {
		//System.out.println("What");
		for(int i=0; i<20; i++) {
			if(cd.busStops.get(i).getX()==x&&cd.busStops.get(i).getY()==y) {
				stop = i;
			}
			
		}
		if(stop==0||stop==6||stop==14||stop==19) {
			xFirst = true;
		}
		else if(stop==4||stop==10||stop==13||stop==18) {
			yFirst=true;
		}
		moving = true;
		xDestination = x;
		yDestination = y;
		
	}


	@Override
	public void setPresent(boolean b) {
		// TODO Auto-generated method stub
		
	}

	public void moveOn() {
		moving = true;
		released = true;
	}
}
