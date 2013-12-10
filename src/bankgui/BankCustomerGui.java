package bankgui;

import java.awt.Color;
import java.awt.Graphics2D;

import javax.swing.ImageIcon;

import bank.CustomerRole;
import bank.utilities.GuiPositions;
import Gui.*;

public class BankCustomerGui implements GuiPositions, Gui {
	boolean isPresent = true;
	boolean atDest = false;
	CustomerRole c;
	private int xPos = doorx, yPos = doory;
	private int xDestination = lineSx, yDestination = liney;
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
			if(!atDest){
			c.msgGuiIsAtDest();
			atDest = true;
			}
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

	public void goTo(int place){
		System.out.println("going to place #"+place);
		if(place == 0){
			xDestination = lineSx;
			yDestination = liney;
		}
		if(place == 1){
			xDestination = line1x;
			yDestination = liney;
		}
		if(place == 2){
			xDestination = line2x;
			yDestination = liney;
		}
		if(place == 3){
			xDestination = line3x;
			yDestination = liney;
		}
		if(place == 4){
			xDestination = line4x;
			yDestination = liney;
		}
		if(place == 5){
			xDestination = tellerx;
			yDestination = teller1y;
		}
		if(place == 6){
			xDestination = tellerx;
			yDestination = teller2y;
		}
		if(place == 7){
			xDestination = tellerx;
			yDestination = teller3y;
		}
		if(place == 8){
			xDestination = tellerx;
			yDestination = teller4y;
		}	
		if(place == 9){
			xDestination = doorx;
			yDestination = doory;
			
		}
		atDest = false;
	}

}
