package market.gui;

import java.awt.Graphics2D;

import javax.swing.ImageIcon;

import market.MarketManagerRole;
import Gui.Gui;

public class ManagerGui implements Gui{

	private MarketManagerRole role = null;

	private enum Command {noCommand, enter, leave};
	private Command command=Command.noCommand;

	private int xPos = 0, yPos = 0;//default waiter position
	private int xDestination = 0, yDestination = 0;


	public ManagerGui(MarketManagerRole role) {
		this.role = role;
	}

	public void draw(Graphics2D g) {
		ImageIcon icon = new ImageIcon("res/Rami.png");
		g.drawImage(icon.getImage(), xPos, yPos, null);
	}

	public boolean isPresent() {
		return true;
	}

	public int getXPos() {
		return xPos;
	}

	public int getYPos() {
		return yPos;
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
			
			if (command == Command.enter) {
				role.msgEntered();
			}
			command = Command.noCommand;
		}

	}

	@Override
	public void setPresent(boolean b) {
		// TODO Auto-generated method stub

	}

	public void GoToRoom() {
		System.out.println("go to room function called");
		xDestination = 325;
		yDestination = 410;
		command = Command.enter;
	}

}
