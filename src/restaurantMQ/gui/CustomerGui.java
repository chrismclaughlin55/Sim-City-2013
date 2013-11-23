package restaurantMQ.gui;

import restaurantMQ.CustomerAgent;
import restaurantMQ.HostAgent;
import restaurantMQ.MQCustomerRole;
import restaurantMQ.interfaces.Customer;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import Gui.Gui;

public class CustomerGui implements Gui{

	private static final int NTABLES = 4;
	private static final int NSPOTS = 4;
	private static final int WINDOWX = 550;
    private static final int WINDOWY = 350;
	
	private Customer agent = null;
	private boolean isPresent = false;
	private boolean isHungry = false;
	private boolean moving = false;
	
	String order = "";

	//private HostAgent host;
	RestaurantGui gui;
	
	private static final int INITX = -40;
	private static final int INITY = -40;
	public static final int WIDTH = 20;

	private int xPos, yPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, GoToSeat, LeaveRestaurant};
	private Command command=Command.noCommand;

	public static final int xTable = 150;
	public static final int yTable = 200;
	public static final int TABLEDIST = 100;
	public static final int NUMROWS = 2;
	
	private class Coordinates
	{
		int x, y;
		
		Coordinates(int x, int y)
		{
			this.x = x;
			this.y = y;
		}
	}
	
	private Map<Integer, Coordinates> tableMap = new HashMap<Integer, Coordinates>();
	private Map<Integer, Coordinates> waitingMap = new HashMap<Integer, Coordinates>();

	//For CustomerAgents
	public CustomerGui(CustomerAgent c, RestaurantGui gui){ //HostAgent m) {
		agent = c;
		xPos = INITX;
		yPos = INITY;
		xDestination = INITX;
		yDestination = INITY;
		//maitreD = m;
		this.gui = gui;
		
		for(int i = 1; i <= NTABLES; ++i)
		{
			tableMap.put(i, new Coordinates(xTable + (((i -1) / NUMROWS)*TABLEDIST), (yTable + ((i-1) % NUMROWS)*TABLEDIST)));
		}
		
		for(int i = 1; i <= NSPOTS; ++i)
		{
			waitingMap.put(i, new Coordinates(WIDTH + 2*(((i-1) / NUMROWS)*WIDTH), (WIDTH + 2*((i - 1) % NUMROWS)*WIDTH)));
		}
	}
	
	//For Roles
	public CustomerGui(MQCustomerRole c, RestaurantGui gui){ //HostAgent m) {
		agent = c;
		xPos = INITX;
		yPos = INITY;
		xDestination = INITX;
		yDestination = INITY;
		//maitreD = m;
		this.gui = gui;
		
		for(int i = 1; i <= NTABLES; ++i)
		{
			tableMap.put(i, new Coordinates(xTable + (((i -1) / NUMROWS)*TABLEDIST), (yTable + ((i-1) % NUMROWS)*TABLEDIST)));
		}
		
		for(int i = 1; i <= NSPOTS; ++i)
		{
			waitingMap.put(i, new Coordinates(WIDTH + 2*(((i-1) / NUMROWS)*WIDTH), (WIDTH + 2*((i - 1) % NUMROWS)*WIDTH)));
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

		if (xPos == xDestination && yPos == yDestination) 
		{
			if(moving)
			{
				moving = false;
				agent.msgAnimationDone();
			}
			if (command==Command.GoToSeat) agent.msgAnimationFinishedGoToSeat();
			else if (command==Command.LeaveRestaurant) {
				agent.msgAnimationFinishedLeaveRestaurant();
				agent.msgAnimationDone();
				isHungry = false;
				gui.setCustomerEnabled(agent);
			}
			command=Command.noCommand;
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, WIDTH, WIDTH);
		if(!order.isEmpty())
		{
			g.setColor(Color.BLACK);
			g.drawRect(xPos+WIDTH, yPos, WIDTH, WIDTH);
			g.drawString(order, xPos+WIDTH, yPos+WIDTH);
		}
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
		moving = true;
		xDestination = tableMap.get(seatnumber).x;
		yDestination = tableMap.get(seatnumber).y;
		command = Command.GoToSeat;
	}
	
	public void DoGoToSpot(int number)
	{
		moving = true;
		xDestination = waitingMap.get(number).x;
		yDestination = waitingMap.get(number).y;
	}
	
	public void DoGoToSeat(int xPos, int yPos) {//later you will map seatnumber to table coordinates.
		xDestination = xPos;
		yDestination = yPos;
		command = Command.GoToSeat;
	}

	public void DoExitRestaurant() {
		xDestination = INITX;
		yDestination = INITY;
		command = Command.LeaveRestaurant;
	}
	
	public void GoToCashier()
	{
		moving = true;
		xDestination = WINDOWX;
		yDestination = WINDOWY - WIDTH;
	}
	
	public void setWaitingOrder(String choice)
	{
		order = choice.substring(0,1) + choice.substring(1,2) + "?";
	}
	
	public void setEatingOrder(String choice)
	{
		order = choice.substring(0,1) + choice.substring(1,2);
	}
	
	public void clearOrder()
	{
		order = "";
	}
}
