package market.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import market.MarketEmployeeRole;
import market.MarketOrder;
import Gui.Gui;

public class EmployeeGui implements Gui {

	private int xPos = 0, yPos = 0;
	private int xDestination = 0, yDestination = 0;
	private int deskNum;
	
	private String label = "";
	private boolean itemsVisible = false;

	private enum Command {noCommand, goToDesk, acquireItems, deliverItems};
	private Command command=Command.noCommand;

	private MarketEmployeeRole role = null;
	public List<MarketOrder> items = Collections.synchronizedList(new ArrayList<MarketOrder>());	
	
	public EmployeeGui(MarketEmployeeRole role) {
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
				command = Command.noCommand;
				return;
			}
			
			else if (command == Command.acquireItems) {
				ProcureItems();
				DeliverItems();
				command = Command.deliverItems;
				return;
			}
			
			else if (command == Command.deliverItems) {
				role.msgAtStorage();
				command = Command.noCommand;
				return;
			}
			
			command = Command.noCommand;
		}

	}

	@Override
	public void draw(Graphics2D g) {
		Color employeeColor = new Color (52, 152, 219);
		g.setColor(employeeColor);
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

	public int getXPos() {
		return xPos;
	}

	public int getYPos() {
		return yPos;
	}

	public void LeaveMarket() {
		// TODO Auto-generated method stub

	}

	public void MoveToDesk(int deskNum) {
		this.deskNum = deskNum;
		role.print("Going to desk " + deskNum);
		command = Command.goToDesk;
		xDestination = 70+90*deskNum;
		yDestination = 205;
	}

	public void AcquireItems(List<MarketOrder> items) {
		this.items = items;
		command = Command.acquireItems;
		xDestination = 0 + (int)(Math.random() * ((240 - 0) + 1));
		yDestination = 355 + (int)(Math.random() * ((475 - 355) + 1));
		
		for (MarketOrder m : items) {
			label = label + m.type.substring(0,2) + " ";
		}
	}
	
	private void ProcureItems() {
		itemsVisible = true;
	}
	
	public void GiveItems() {
		itemsVisible = false;
	}
	
	private void DeliverItems() {
		xDestination = 70+90*deskNum;
		yDestination = 205;
	}


}
