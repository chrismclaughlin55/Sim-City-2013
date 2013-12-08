package restaurantLY.gui;

import restaurantLY.CustomerAgent;

import java.awt.*;

public class CustomerGui implements Gui{

	private CustomerAgent agent = null;
	private boolean isPresent = false;
	private boolean isHungry = false;

	RestaurantGui gui;

	private int xPos, yPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, GoToSeat, LeaveRestaurant};
	private Command command=Command.noCommand;

    public static final int xTable[] = {55, 145, 235, 325};
    public static final int yTable[] = {230, 230, 230, 230};
    
    private int custX, custY;
    private String order;

	public CustomerGui(CustomerAgent c, RestaurantGui gui){
		agent = c;
		xPos = -40;
		yPos = -40;
		xDestination = -40;
		yDestination = -40;
		//maitreD = m;
		this.gui = gui;
	}
	
	public CustomerGui(CustomerAgent c, RestaurantGui gui, int x, int y){
		agent = c;
		xPos = x;
		yPos = y;
		xDestination = x;
		yDestination = y;
		//maitreD = m;
		this.gui = gui;
		custX = x;
		custY = y;
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
		
		for (int i = 0; i < 4; i++) {
        	if (xPos == xDestination && yPos == yDestination
        			& (xDestination == xTable[i]) & (yDestination == yTable[i])) {
        		agent.msgAtTable();
        	}
        }
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, 20, 20);
		g.setColor(Color.BLACK);
		if (order != null) {
        	g.drawString(order, xPos+10, yPos+30);
        }
	}

	public boolean isPresent() {
		return isPresent;
	}
	public void setHungry() {
		isHungry = true;
		xPos = custX;
		yPos = custY;
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
		xDestination = xTable[seatnumber];
		yDestination = yTable[seatnumber];
		command = Command.GoToSeat;
	}

	public void DoExitRestaurant() {
		xDestination = -40;
		yDestination = -40;
		command = Command.LeaveRestaurant;
	}
	
	public void placeFood(String order) {
    	this.order = order;
    }
    
    public void removeFood() {
    	this.order = null;
    }
}
