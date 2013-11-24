package city.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import Gui.Gui;
import city.PersonAgent;
import restaurantMQ.gui.RestaurantGui;

public class PersonGui implements Gui {
	
	private PersonAgent agent = null;
	private boolean isPresent = true;
	
	
	private static final int INITX = 40;
	private static final int INITY = 40;
	public static final int WIDTH = 20;

	private int xPos, yPos;
	private int xDestination, yDestination;
	

	public PersonGui(PersonAgent p){ //HostAgent m) {
		agent = p;
		xPos = INITX;
		yPos = INITY;
		xDestination = INITX;
		yDestination = INITY;
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

	}
	

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, WIDTH, WIDTH);
		
	}
	
	public void DoGoToEntrance() {
		xDestination = 0;
		yDestination = 340;
	}
	
	public void DoGoToRefridgerator() {
		xDestination = 30;
		yDestination = 30;
	}
	
	public void DoGoToBed() {
		xDestination = 360;
		yDestination = 20;
	}
	
	public void DoGoToStove() {
		xDestination = 105;
		yDestination = 25;
	}
	
	public void DoGoToWall() {
		xDestination = 190;
		yDestination = 340;
	}
	
	public void DoGoToCouch() {
		xDestination = 170;
		yDestination = 190;
	}

	@Override
	public boolean isPresent() {
		return isPresent;
	}

}
