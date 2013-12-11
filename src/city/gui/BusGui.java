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
	private Bus agent = null;
	public int xPos;
	public int yPos;
	boolean xmove = false;
	boolean xFirst = false;
	boolean yFirst = false;
	private int xDestination, yDestination;//default start position
	boolean released = true;
	boolean moving = false;
	boolean atStop = false;
	boolean moveOn = false;
	boolean readyForUpdate = true;
	boolean crossingIntersection = false;
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
		agent.setCurrentGrid(nextGrid);
		currGrid = nextGrid;
		nextGrid = cd.getNextRGrid(nextGrid);
		m = main;
		//currGrid = ....;
	}

	public void updatePosition() {

		if(moveOn)
		{
			moveOn = false;
			released = true;
			
			moving = true;
		}

		if(!moving)
		{
			return;
		}

		if(!atStop && xPos == xDestination && yPos == yDestination) {
			moving = false;
			atStop = true;
			agent.msgAtDestination();
			return;
		}

		atStop = false;

		if(readyForUpdate){
			readyForUpdate = false;
			if(yPos % 20 == 0 && (currGrid.direction == Direction.north || currGrid.direction == Direction.south)) {
				moving = false;
				agent.msgAcquireGrid(nextGrid);
				prevGrid = currGrid;
				currGrid = nextGrid;
				nextGrid = cd.getNextRGrid(nextGrid);
			}
			else if(xPos % 20 == 0 && (currGrid.direction == Direction.east || currGrid.direction == Direction.west)) {
				moving = false;
				agent.msgAcquireGrid(nextGrid);
				prevGrid = currGrid;
				currGrid = nextGrid;
				nextGrid = cd.getNextRGrid(nextGrid);
			}
			//Moving through an intersection
			else if(currGrid.direction == Direction.none 
					&& (((prevGrid.direction == Direction.east || prevGrid.direction == Direction.west) && xPos == currGrid.index1()*20) 
					|| ((prevGrid.direction == Direction.north || prevGrid.direction == Direction.south) && yPos == currGrid.index2()*20))) {
				moving = false;
				agent.msgAcquireGrid(nextGrid);
				prevGrid = currGrid;
				currGrid = nextGrid;
				nextGrid = cd.getNextRGrid(nextGrid);
			}
			if(nextGrid == null) {
				if(xPos == xDestination) {
					//no turning required, so we simply need to pass straight through the intersection
					if(prevGrid.direction == Direction.north) {
						nextGrid = (RGrid)cd.cityGrid[prevGrid.index1()][prevGrid.index2()-3];
						currGrid = (RGrid)cd.cityGrid[prevGrid.index1()][prevGrid.index2()-2];
						crossingIntersection = true;
						readyForUpdate = true;
						return;
					}
					else if(prevGrid.direction == Direction.south) {
						nextGrid = (RGrid)cd.cityGrid[prevGrid.index1()][prevGrid.index2()+3];
						currGrid = (RGrid)cd.cityGrid[prevGrid.index1()][prevGrid.index2()+2];
						crossingIntersection = true;
						readyForUpdate = true;
						return;
					}
				}
				if(route == 1) { //outer route, always performs left turns
					if(prevGrid.direction == Direction.north) {
						nextGrid = (RGrid)cd.cityGrid[prevGrid.index1()-2][prevGrid.index2()-2];
						currGrid = (RGrid)cd.cityGrid[prevGrid.index1()-1][prevGrid.index2()-2];
					}
					else if(prevGrid.direction == Direction.south) {
						nextGrid = (RGrid)cd.cityGrid[prevGrid.index1()+2][prevGrid.index2()+2];
						currGrid = (RGrid)cd.cityGrid[prevGrid.index1()+1][prevGrid.index2()+2];
					}
					else if(prevGrid.direction == Direction.east) {
						nextGrid = (RGrid)cd.cityGrid[prevGrid.index1()+2][prevGrid.index2()-2];
						currGrid = (RGrid)cd.cityGrid[prevGrid.index1()+2][prevGrid.index2()-1];
					}
					else if(prevGrid.direction == Direction.west) {
						nextGrid = (RGrid)cd.cityGrid[prevGrid.index1()-2][prevGrid.index2()+2];
						currGrid = (RGrid)cd.cityGrid[prevGrid.index1()-2][prevGrid.index2()+1];
					}
				}
				else { //inner route, always performs right turns
					if(prevGrid.direction == Direction.north) {
						nextGrid = (RGrid)cd.cityGrid[prevGrid.index1()+1][prevGrid.index2()-1];
					}
					else if(prevGrid.direction == Direction.south) {
						nextGrid = (RGrid)cd.cityGrid[prevGrid.index1()-1][prevGrid.index2()+1];
					}
					else if(prevGrid.direction == Direction.east) {
						nextGrid = (RGrid)cd.cityGrid[prevGrid.index1()+1][prevGrid.index2()+1];
					}
					else if(prevGrid.direction == Direction.west) {
						nextGrid = (RGrid)cd.cityGrid[prevGrid.index1()-1][prevGrid.index2()-1];
					}
				}
				readyForUpdate = true;
				return;
			}
		
				
		//move depending on the desired direction
		if(currGrid.direction == Direction.north){
			yPos -= 5;
		}
		else if(currGrid.direction == Direction.south) {
			yPos += 5;
		}
		else if(currGrid.direction == Direction.east) {
			xPos += 5;
		}
		else if(currGrid.direction == Direction.west) {
			xPos -= 5;
		}
		//Reaching this point means that currGrid.direction == none, meaning we are crossing an intersection
		
		else if(prevGrid.direction == Direction.north) {
			yPos -= 5;
		}
		else if(prevGrid.direction == Direction.south) {
			yPos += 5;
		}
		else if(prevGrid.direction == Direction.east) {
			xPos += 5;
		}
		else if(prevGrid.direction == Direction.west) {
			xPos -= 5;
		}
		readyForUpdate = true;
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
		xDestination = x;
		yDestination = y;
		moving = true;
	}


	@Override
	public void setPresent(boolean b) {
		// TODO Auto-generated method stub

	}

	public void moveOn() {
		moveOn = true;
	}
}
