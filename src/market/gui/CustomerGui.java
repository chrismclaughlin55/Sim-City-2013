package market.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import market.MarketCustomerRole;
import Gui.Gui;

public class CustomerGui implements Gui{
	
	int xPos = 0, yPos = 0, xDestination = 0, yDestination = 0;
	private enum Command {noCommand, goToDesk};
    private Command command=Command.noCommand;
	
	private MarketCustomerRole role;

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
			command = Command.noCommand;
		}
		
	}

	@Override
	public void draw(Graphics2D g) {
		Color customerColor = new Color (46, 204, 113);
		g.setColor(customerColor);
		g.fillRect(xPos, yPos, 30, 30);
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
	
	public void DoGoToEmployee(int x, int y) {
		System.out.println ("here");
		xDestination = x;
		yDestination = y;
	}

}
