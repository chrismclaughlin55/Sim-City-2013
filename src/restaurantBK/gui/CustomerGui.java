package restaurantBK.gui;

import restaurantBK.BKHostRole;
import restaurantBK.interfaces.Customer;

import java.awt.*;

public class CustomerGui implements Gui{

	private Customer agent = null;
	private boolean isPresent = false;
	private boolean isHungry = false;

	//private HostAgent host;
	RestaurantGui gui;

	private int xPos, yPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, GoToSeat, GoToCashier, LeaveRestaurant};
	private Command command=Command.noCommand;
	private boolean choice = false;
	private boolean eat = false;
	private String selection;
	public int xTable = 200;
	public int yTable = 250;
	private int cashierx = 16;
	private int cashiery = 400;

	public CustomerGui(Customer c, RestaurantGui gui){ //HostAgent m) {
		agent = c;
		xPos = -40;
		yPos = -40;
		xDestination = -40;
		yDestination = -40;
		//maitreD = m;
		this.gui = gui;
	}

	public void flipEat() {
		if(eat == false) {
			eat = true;
		}
		else {
			eat = false;
		}
	}
	public void flipChoice(String s) {
		if(choice == false) {
			choice = true;
		}
		else {
			choice = false;
		}
		this.selection = s;
	}
	public void updatePosition() {
		if (xPos < xDestination)
			xPos+=2;
		else if (xPos > xDestination)
			xPos-=2;

		if (yPos < yDestination)
			yPos+=2;
		else if (yPos > yDestination)
			yPos-=2;

		if (xPos == xDestination && yPos == yDestination) {
			if (command==Command.GoToSeat) agent.msgAnimationFinishedGoToSeat();
			else if (command==Command.LeaveRestaurant) {
				agent.msgAnimationFinishedLeaveRestaurant();
				//System.out.println("about to call gui.setCustomerEnabled(agent);");
				isHungry = false;
				gui.setCustomerEnabled(agent);
			}
			else if (command==Command.GoToCashier) {
				agent.msgAnimationFinishedGoToCashier();
				//agent.
			}
			command=Command.noCommand;
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, 20, 20);
		if(choice) {
			g.drawString(selection.substring(0,2)+"?",xPos+5,yPos);
		}
		if(eat) {
			g.drawString(selection.substring(0,2),xPos+10, yPos-10);
		}
	}
	
	public void DoGoToWait(int x, int y) {
		xDestination=x;
		yDestination=y;
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

	public void choiceBox(String c) {
		
		//Graphics2d g = new Graphics();
		//g.drawString(c,20,20);
		//Graphics2D g = new Graphics();
		//make a stupid text box that has c+"?" right above coordinates
		//xpos+5, ypos + 5
	}
	public void DoGoToSeat(int seatnumber,int xcord, int ycord) {//later you will map seatnumber to table coordinates.
		xDestination = xcord;
		yDestination = ycord;
		xTable=xcord;
		yTable=ycord;
		command = Command.GoToSeat;
	}
	public void DoGoToCashier() {
		xDestination = cashierx;
		yDestination = cashiery;
		command = Command.GoToCashier;
	}
	public void DoExitRestaurant() {
		xDestination = -40;
		yDestination = -40;
		command = Command.LeaveRestaurant;
	}
}
