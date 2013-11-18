package city.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import city.PersonAgent;
import restaurantMQ.CustomerAgent;
import restaurantMQ.gui.RestaurantGui;

public class PersonGui implements Gui{
	
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

	@Override
	public boolean isPresent() {
		return isPresent;
	}

}
