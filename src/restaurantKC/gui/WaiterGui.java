package restaurantKC.gui;

//Waiter GUI agent has a location map. Host tells Waiter agent which table to go to.
//Waiter GUI agent then looks at location map, finds the table number and finds the coordinates.
//Waiter GUI agent then tells the Customer GUI agent where to go


import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import restaurantKC.KCWaiterRole;
import restaurantKC.interfaces.Waiter;

public class WaiterGui implements Gui {

	public static class Location {
		public Location (int x, int y) {
			this.x = x;
			this.y = y;
		}
		public Location () {
			x=-1;
			y=-1;
		}
		public int getX() {
			return x;
		}
		public void setX(int x) {
			this.x = x;
		}
		public int getY() {
			return y;
		}
		public void setY(int y) {
			this.y = y;
		}
		public int x;
		public int y;
	}

	public static List<Location> locations = null;
	public static List<FoodGui> foodItems = null;
	public static List<Location> plates = null;

	protected KCWaiterRole agent = null;
	private FoodGui food = null;
	private int CookX = 270;
	private int CookY = 180;
	private int plateNum = -1;

	private int homeX = 30;
	private int homeY = 30;

	private int xPos = homeX, yPos = homeY;//default waiter position
	
	int xDestination = homeX;
	int yDestination = homeY;

	private int tableNumber = -1;
	public boolean headingBack = false;
	public boolean leaving = false;
	private AnimationPanel animationPanel = null;
	Timer breakTimer = new Timer();
	private RestaurantPanel restPanel;

	public WaiterGui(Waiter agent, RestaurantPanel rp, int num) {
		locations = new ArrayList<Location>();
		foodItems = new ArrayList<FoodGui>();
		plates = new ArrayList<Location>();
		if (num > 3) 
			num = num%4;
		homeX = homeX+num*35;
		xPos = homeX;
		xDestination = homeX; 
		restPanel = rp;
		this.agent = (KCWaiterRole) agent;
		int n = 50;
		for (int i = 0; i < 3; i++) {
			locations.add(new Location(n, 400));
			n += 150;
		}

		n = 340;
		for (int i = 0; i < 3; i++) {
			plates.add(new Location(n, 200));
			n += 55;
		}
	}

	public void updatePosition() {

		if (xPos < xDestination)
			xPos++;
		else if (xPos > xDestination)
			xPos--;

		if (yPos < yDestination)
			yPos++;
		else if (yPos > yDestination)
			yPos--;

		if (tableNumber > 0) {
			if (xPos == xDestination && yPos == yDestination
					& (xDestination == (locations.get(tableNumber - 1).getX() + 20)) & (yDestination == (locations.get(tableNumber - 1).y - 20))) {
				agent.msgAtTable();
			}                
		}
		if (tableNumber > 0) {
			if (xPos == xDestination && yPos == yDestination
					& (xDestination == (locations.get(tableNumber - 1).getX() + 20)) & (yDestination == (locations.get(tableNumber - 1).y - 70))) {
				agent.msgAtTable();
			}                
		}

		if ((xDestination == -30) && (yDestination ==-30) && (xPos == -30) && (yPos == -30) && leaving) {
			leaving = false;
			agent.msgDoneLeaving();
		}




		if ((xPos == homeX) && (yPos == homeY))
		{
			if (headingBack)
			{
				agent.msgLeftCustomer();
				headingBack = false;
			}
		}    

		if (plateNum == -1) {
			if ((xPos == xDestination) && (yPos == yDestination)) {
				if ((xPos == CookX) && (yPos == CookY) && (xDestination == CookX) && (yDestination == CookY)){ //hack
					agent.msgAtCook();
				}
			}
		}

		if ((plateNum >= 0) && (plateNum < 3)){
			if ((xPos == xDestination) && (yPos == yDestination)) {
				if ((xPos == plates.get(plateNum).getX()+9) && (yPos == plates.get(plateNum).getY()+40)){ 
					agent.msgAtPlate();
				}
			}
		}

	}

	public void draw(Graphics2D g) {
		Color waiterColor = new Color(41, 128, 185);
		g.setColor(waiterColor);
		g.fillRect(xPos, yPos, 30, 30);
	}

	public boolean isPresent() {
		return true;
	}

	public void DoBringToTable(CustomerGui CustGui, int tNum) {
		if ((xPos != homeX) && (yPos != homeY)) {
			DoLeaveCustomer();
			try {
				agent.leftCustomer.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		tableNumber = tNum;
		xDestination = locations.get(tableNumber-1).x + 20;
		yDestination = locations.get(tableNumber-1).y - 20;
		CustGui.setSuggestedDestination(xDestination, yDestination);
	}

	public void DoGoToTable(int tNum) {
		tableNumber = tNum;
		xDestination = locations.get(tableNumber-1).getX() + 20;
		yDestination = locations.get(tableNumber-1).getY() - 70;

	}
	public void DoGoToCook(int plateNum)
	{	
		this.plateNum = plateNum;
		if (plateNum == -1) {
			xDestination = CookX; 
			yDestination = CookY; 
		}
		else {
			xDestination = plates.get(plateNum).getX()+9;
			yDestination = plates.get(plateNum).getY()+40;
		}

	}

	public void DoLeaveCustomer() {
		headingBack = true;
		xDestination = homeX;
		yDestination = homeY;
	}

	public void procureFood(String choice, int t) {
		food = new FoodGui(this, choice, false, xPos, yPos, t);
		foodItems.add(food);
		animationPanel.addGui(food);
		food.moveWithWaiter();
	}

	public int getXPos() {
		return xPos;
	}

	public int getYPos() {
		return yPos;
	}

	public void setAnimationPanel(AnimationPanel ap) {
		animationPanel = ap;
	}

	public void DoDeliverFood(int t, String choice, CustomerGui custGui) {
		food.moveToTable();
	}

	public void setBreak() {
		agent.msgWantBreak();
	}
	public boolean IsOnBreak() {
		return agent.onBreak;
	}
	public void setOffBreak() {
		breakTimer.schedule(new TimerTask() {
			public void run() {
				System.out.println(agent.getName() + ": break is over");
				agent.takingBreak.release();
				agent.offBreak();
			}
		},
		15000);
	}
	public void DoClearTable(int t) {
		for (FoodGui f : foodItems) {
			if (f.tableNumber == t) {
				f.visible = false;
			}
			//break;
		}
	}
	public boolean isHome() {
		if ((xPos == homeX) && (yPos == homeY)) {
			return true;
		}
		return false;
	}

	public void DefaultAction() {
		xDestination = homeX;
		yDestination = homeY;
	}

	public void DoLeaveRestaurant()
	{	
		leaving = true;
		xDestination = -30;
		yDestination = -30;
	}

}
