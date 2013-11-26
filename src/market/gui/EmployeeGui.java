package market.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import market.MarketCustomerRole;
import market.MarketEmployeeRole;
import Gui.Gui;

public class EmployeeGui implements Gui {

	private int xPos = 0, yPos = 0;
	private int xDestination = 0, yDestination = 0;
	
	private enum Command {noCommand, goToDesk};
    private Command command=Command.noCommand;

    private MarketEmployeeRole role = null;
    
    public EmployeeGui(MarketEmployeeRole role, int x, int y) {
    	this.role = role;
    	xDestination = x;
    	yDestination = y;
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
		Color employeeColor = new Color (52, 152, 219);
		g.setColor(employeeColor);
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

	public int getXPos() {
		return xPos;
	}

	public int getYPos() {
		return yPos;
	}

	public void LeaveMarket() {
		// TODO Auto-generated method stub
		
	}

}
