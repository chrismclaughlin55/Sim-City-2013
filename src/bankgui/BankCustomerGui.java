package bankgui;

import java.awt.Color;
import java.awt.Graphics2D;

import javax.swing.ImageIcon;

import bank.CustomerRole;
import bank.utilities.GuiPositions;
import Gui.*;

public class BankCustomerGui implements GuiPositions, Gui {
	Boolean isPresent = true;
	CustomerRole c;
	private int xPos = doorx, yPos = doory;
	private int xDestination = 50, yDestination = 50;
	public BankCustomerGui(CustomerRole c) {
		this.c = c;
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
		//	c.msgGuiIsAtDest();
		}
	}

	@Override
	public void draw(Graphics2D g) {
		Graphics2D g2 = (Graphics2D)g;
		ImageIcon person = new ImageIcon("res/person.png");
		g2.drawImage(person.getImage(), xPos, yPos, null);
		
	}

	@Override
	public boolean isPresent() {
		return isPresent;
	}

	@Override
	public void setPresent(boolean b) {
		isPresent = b;
		
	}

}
