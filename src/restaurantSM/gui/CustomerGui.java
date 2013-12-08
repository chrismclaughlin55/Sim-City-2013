package restaurantSM.gui;

import restaurantSM.CustomerAgent;
import restaurantSM.HostAgent;

import java.awt.*;

public class CustomerGui implements Gui{

	private CustomerAgent agent = null;
	private boolean isPresent = false;
	private boolean isHungry = false;
	private String statusText = "";

	//private HostAgent host;
	RestaurantGui gui;

	private int xPos, yPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, GoToLine, GoToSeat, LeaveRestaurant};
	private Command command=Command.noCommand;
	private int xHome;
	private int yHome;

	public static final int xTable = 200;
	public static final int yTable = 250;
	public static final int posChange = 20;
	private int index;
	boolean moving = false;

	public CustomerGui(CustomerAgent c, RestaurantGui gui, int i){ //HostAgent m) {
		agent = c;
		xPos = -40;
		yPos = -40;
		xDestination = -40;
		yDestination = -40;
		xHome = 20;
	    yHome = 20 + (i*20) + (10 * i);
		index = i;
		//maitreD = m;
		this.gui = gui;
	}
	
	public int getIndex(){
		return index;
	}

	public void updatePosition() {
		if (xPos < xDestination)
			xPos+= 2;
		else if (xPos > xDestination)
			xPos-= 2;

		if (yPos < yDestination)
			yPos+= 2;
		else if (yPos > yDestination)
			yPos-= 2;

		if (xPos == xDestination && yPos == yDestination) {
			if (command==Command.GoToSeat) agent.msgAnimationFinishedGoToSeat();
			else if (command==Command.LeaveRestaurant) {
				agent.msgAnimationFinishedLeaveRestaurant();
				isHungry = false;
				gui.setCustomerEnabled(agent);
			}
			if (moving == true) {
				moving = false;
				agent.msgAnimationDone();
			}
			command=Command.noCommand;
		}
	}
	
	public void setStatusText(String s){
		statusText = s;
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, posChange, posChange);
		g.drawString(statusText, xPos, yPos);
	}

	public boolean isPresent() {
		return isPresent;
	}
	public void setHungry() {
		isHungry = true;
		agent.gotHungry();
		setPresent(true);
	}
	public void setFull() {
		isHungry = false;
	}
	public boolean isHungry() {
		return isHungry;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}

	public void DoGoToLine() {
		moving = true;
		xDestination = xHome;
		yDestination = yHome;
		
		command = Command.GoToLine;
	}
	
	public void DoGoToSeat(int seatnumber) {//later you will map seatnumber to table coordinates.
		switch (seatnumber) {
    	case 1: 
    		xDestination = 150;
	        yDestination = 200;
    		break;
    	case 2:
    		xDestination = 150;
	        yDestination = 300;
    		break;
    	case 3: 
    		xDestination = 150;
	        yDestination = 400;
    		break;
    	}
		command = Command.GoToSeat;
	}

	public void DoExitRestaurant() {
		xDestination = -40;
		yDestination = -40;
		command = Command.LeaveRestaurant;
	}
}
