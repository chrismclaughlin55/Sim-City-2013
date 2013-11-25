package city.gui;

import java.awt.Graphics2D;

import javax.swing.ImageIcon;

import Gui.Gui;
import city.BusAgent;

public class BusGui implements Gui {
	private BusAgent agent = null;
	int xPos=0;
	int yPos=0;
	boolean xmove = false;
	boolean xfirst = false;
	private int xDestination = -20, yDestination = -20;//default start position
    boolean betweenStops = false;
	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		if(xfirst) {
			if(xmove) {
				if(xPos==xDestination) {
					xmove = false;
				}
				if (xPos < xDestination)
		            xPos++;
		        else if (xPos > xDestination)
		            xPos--;
			}
			else {
				if (yPos < yDestination)
		            yPos++;
		        else if (yPos > yDestination)
		            yPos--;
			}
		}
        if (moving && xPos == xDestination && yPos == yDestination
        		&& (xDestination >= 0) && (yDestination >= 0)) {
           agent.msgAnimationDone();
        }
		
	}


	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		Graphics2D g2 = (Graphics2D)g;
		ImageIcon person = new ImageIcon("res/bus.png");
		g2.drawImage(person.getImage(), xPos, yPos, null);
	}


	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void DoGoToNextStop(int x, int y) {
		xDestination = x;
		yDestination = y;
		
	}


	@Override
	public void setPresent(boolean b) {
		// TODO Auto-generated method stub
		
	}
}
