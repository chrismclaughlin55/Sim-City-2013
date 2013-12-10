package bankgui;
import Gui.*;

import java.awt.Color;
import java.awt.Graphics2D;

import javax.swing.ImageIcon;

import bank.TellerRole;
import bank.utilities.GuiPositions;

public class TellerGui implements GuiPositions, Gui {
	private TellerRole teller;
	private int xPos;
	private int xDestination;
	private int yPos;
	private int yDestination;
	private boolean isPresent = true;
	private boolean moving = false;


	public TellerGui(TellerRole tellerRole, int place) {
		this.teller = tellerRole;
		xPos = doorx;
		yPos = doory;
		goTo(place);
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

			if(xPos == xDestination && yPos == yDestination){
					teller.msgGuiIsAtDest();
					moving = false;
			}

	}




	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.red);
		g.fillRect(xPos, yPos, 10, 10);
	}


	@Override
	public boolean isPresent() {
		return isPresent;
	}


	@Override
	public void setPresent(boolean b) {
		isPresent = b;
	}


	public void goTo(int place) {
		moving = true;
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
	}

}
