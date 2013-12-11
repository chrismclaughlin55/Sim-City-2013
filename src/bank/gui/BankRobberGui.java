package bank.gui;

import java.awt.Graphics2D;

import javax.swing.ImageIcon;

import bank.interfaces.DaBankRobber;
import bank.utilities.GuiPositions;
import Gui.Gui;

public class BankRobberGui implements Gui, GuiPositions {

	private int xPos, yPos;
	private int xDestination, yDestination;
	private boolean moving = false;
	private DaBankRobber bankRobber;
	private boolean isPresent = true;
	
	public BankRobberGui(DaBankRobber br) {
		bankRobber = br;
		xPos = doorx;
		yPos = doory;
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
		
		if (xPos == xDestination && yPos == yDestination && moving == true) {
            moving = false;
            bankRobber.msgDoneMoving();
		}
	}
	
	public void DoGoToTeller() {
		moving = true;
		xDestination = tellerx - 20;
		yDestination = teller1y;
	}
	
	public void DoGoToDoorway() {
		moving = true;
		xDestination = doorx;
		yDestination = doory;
	}

	
	public void draw(Graphics2D g) {
		Graphics2D g2 = (Graphics2D)g;
		ImageIcon person = new ImageIcon("res/bankrobber.png");
		g2.drawImage(person.getImage(), xPos, yPos, null);
	}

	
	public boolean isPresent() {
		return isPresent;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}
	
	public void DoLeaveBuilding() {
		isPresent = false;
	}

}
