package market.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import market.MarketCustomerRole;
import market.MarketEmployeeRole;
import market.MarketOrder;
import market.MyOrder;
import Gui.Gui;

public class CustomerGui implements Gui{
	
	int xPos = 0, yPos = 0, xDestination = 0, yDestination = 0;
	private enum Command {noCommand, goToDesk, leave};
    private Command command=Command.noCommand;
	
	private MarketCustomerRole role;
	private String label = "";
	private boolean itemsVisible = false;

	public CustomerGui(MarketCustomerRole role) {
		this.role = role;
	}
	@Override
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
			
			if (command == Command.goToDesk) {
				role.msgAtDesk();
			}
			
			if (command == Command.leave) {
				role.msgLeft();
			}
			command = Command.noCommand;
		}
		
	}

	@Override
	public void draw(Graphics2D g) {
		Color customerColor = new Color (46, 204, 113);
		g.setColor(customerColor);
		g.fillRect(xPos, yPos, 30, 30);
		
		if (itemsVisible) {
			g.setColor(new Color (231, 76, 60));
    		g.fillRect(xPos-20, yPos-55, 70, 55);
			g.setColor(new Color (255, 255, 255));
			if (label.length() < 13)
				g.drawString(label, xPos-17, yPos-30);
			else {
				g.drawString(label.substring(0,12), xPos-17, yPos-30);
				g.drawString(label.substring(12,label.length()), xPos-17, yPos-10);
			}
		}
	}

	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void setPresent(boolean b) {
		// TODO Auto-generated method stub
		
	}
	
	
	public void DoGoToEmployee(MarketEmployeeRole employee) {
		command = Command.goToDesk;
		xDestination = employee.gui.getXPos();
		yDestination = employee.gui.getYPos()-90;
	}
	
	public void DoLeaveMarket() {
		command = Command.leave;
		xDestination = -30;
		yDestination = -30;
	}
	
	public void TakeItems() {
		for (MyOrder m : role.orders) {
			label = label + m.type.substring(0,2) + " ";
		}
		itemsVisible = true;
	}

}
