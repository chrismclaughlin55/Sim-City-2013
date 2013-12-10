package restaurantCM.gui;

import restaurantCM.CMCustomerRole;
import restaurantCM.CMHostRole;

import java.awt.*;

/*
 * TODO Pass table coordinates from host GUI to cust GUI
 * after seating look into sched
 * 
 */

public class CMCustomerGui implements Gui{

	private CMCustomerRole agent = null;
	private boolean isPresent = false;
	private boolean isHungry = false;

	//private HostAgent host;
	CMRestaurantGui gui;

	private int xPos, yPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, GoToSeat, LeaveRestaurant};
	private Command command=Command.noCommand;
		public int xTable, yTable;
		public static final int xTable1 = CMHostGui.xTable1;
		public static final int yTable1 = CMHostGui.yTable1;
		public static final int xTable2 = CMHostGui.xTable2;
		public static final int yTable2 = CMHostGui.yTable2;
		public static final int xTable3 = CMHostGui.xTable3;
		public static final int yTable3 = CMHostGui.yTable3;
		public static final int xTable4 = CMHostGui.xTable4;
		public static final int yTable4 = CMHostGui.yTable4;
		public static final int xTable5 = CMHostGui.xTable5;
		public static final int yTable5 = CMHostGui.yTable5;
		public static final int xTable6 = CMHostGui.xTable6;
		public static final int yTable6 = CMHostGui.yTable6;
	    public static final int xTable7 = 160;
	    public static final int yTable7 = 100;
	    public static final int xTable8 = 160;
	    public static final int yTable8 = 160;
	    public static final int xTable9 = 160;
	    public static final int yTable9 = 220;
	    public static final int xTable10 = 160;
	    public static final int yTable10 = 280;
	    public static final int xTable11 = 160;
	    public static final int yTable11 = 340;
	    public static final int xTable12 = 160;
	    public static final int yTable12 = 400;

	public CMCustomerGui(CMCustomerRole c, CMRestaurantGui gui){ //HostAgent m) {
		agent = c;
		xPos = 60;
		yPos = 40;
		xDestination = 60;
		yDestination = 40;
		//maitreD = m;
		this.gui = gui;
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

		if (xPos == xDestination && yPos == yDestination) {
			if (command==Command.GoToSeat) agent.msgAnimationFinishedGoToSeat();
			else if (command==Command.LeaveRestaurant) {
				agent.msgAnimationFinishedLeaveRestaurant();
				isHungry = false;
				gui.setCustomerEnabled(agent);
			}
			command=Command.noCommand;
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, 20, 20);
	}

	public boolean isPresent() {
		return isPresent;
	}
	public void setHungry() {
		isHungry = true;
		agent.gotHungry();
		setPresent(true);
	}
	public boolean isHungry() {
		return isHungry;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}

	public void DoGoToSeat(int seatnumber) {//later you will map seatnumber to table coordinates.
		switch(seatnumber){
		case 1:
			xDestination = xTable1 + 20;
			yDestination = yTable1 - 20;
			yTable = yTable1;
			xTable = xTable1;
			
			break;
		case 2:
			xDestination = xTable2 + 20;
	        yDestination = yTable2 - 20;
	        yTable = yTable2;
			xTable = xTable2;
	        break;
		case 3:
			xDestination = xTable3 + 20;
			yDestination = yTable3 - 20;
			yTable = yTable3;
			xTable = xTable3;
			break;
		case 4:
			xDestination = xTable4 + 20;
			yDestination = yTable4 - 20;
			yTable = yTable4;
			xTable = xTable4;
			break;
		case 5:
			xDestination = xTable5 + 20;
			yDestination = yTable5 - 20;
			yTable = yTable5;
			xTable = xTable5;
			break;
		case 6:
			xDestination = xTable6 + 20;
	        yDestination = yTable6 - 20;
	        yTable = yTable6;
			xTable = xTable6;
	        break;
		case 7:
			xDestination = xTable7 + 20;
			yDestination = yTable7 - 20;
			yTable = yTable7;
			xTable = xTable7;
			break;
		case 8:
			xDestination = xTable8 + 20;
			yDestination = yTable8 - 20;
			yTable = yTable8;
			xTable = xTable8;
			break;
		case 9:
			xDestination = xTable9 + 20;
			yDestination = yTable9 - 20;
			yTable = yTable9;
			xTable = xTable9;
			break;
		case 10:
			xDestination = xTable10 + 20;
	        yDestination = yTable10 - 20;
	        yTable = yTable10;
			xTable = xTable10;
	        break;
		case 11:
			xDestination = xTable11 + 20;
			yDestination = yTable11 - 20;
			yTable = yTable11;
			xTable = xTable11;
			break;
		case 12:
			xDestination = xTable12 + 20;
			yDestination = yTable12 - 20;
			yTable = yTable12;
			xTable = xTable12;
			break;
	}
		
		xDestination = xTable;
		yDestination = yTable;
		command = Command.GoToSeat;
	}

	public void DoExitRestaurant() {
		xDestination = -40;
		yDestination = -40;
		command = Command.LeaveRestaurant;
	}
}
