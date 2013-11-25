package city.gui;

import java.awt.Graphics2D;

import javax.swing.ImageIcon;

import Gui.Gui;
import city.BusAgent;
import city.CityData;

public class BusGui implements Gui {
	private BusAgent agent = null;
	int xPos=0;
	int yPos=0;
	boolean xmove = false;
	boolean xFirst = false;
	boolean yFirst = false;
	private int xDestination = -20, yDestination = -20;//default start position
    boolean betweenStops = false;
    boolean moving = false;
    int stop;
    CityData cd;
    public BusGui(BusAgent ba) {
    	agent = ba;
    }
	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		if(xFirst) {
			if (xPos < xDestination)
		       xPos++;
		    if (xPos > xDestination)
		       xPos--;
			if (xPos == xDestination) {
				xFirst = false;
			}			
		}	
		if(yFirst) {
			if (yPos < yDestination)
			   yPos++;
			if (yPos > yDestination)
			   yPos--;
			if (yPos == yDestination) {
				yFirst = false;
			}
		}
		if(!xFirst&&!yFirst) {
			if (xPos < xDestination)
			   xPos++;
			if (xPos > xDestination)
			   xPos--;
			if (xPos == xDestination) {
				xFirst = false;
			}	
			if (yPos < yDestination)
				yPos++;
			if (yPos > yDestination)
				yPos--;
			if (yPos == yDestination) {
				yFirst = false;
			}
		}
        if (moving && xPos == xDestination && yPos == yDestination
        		&& (xDestination >= 0) && (yDestination >= 0)) {
        	moving = false;
           agent.msgAtDestination();
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
		
		for(int i=0; i<11; i++) {
			if(cd.busStops.get(i).getX()==x&&cd.busStops.get(i).getY()==y) {
				stop = i;
			}
				
		}
		if(stop==0||stop==6) {
			xFirst = true;
		}
		else if(stop==4||stop==10) {
			yFirst=true;
		}
		moving = true;
		xDestination = x;
		yDestination = y;
		
	}


	@Override
	public void setPresent(boolean b) {
		// TODO Auto-generated method stub
		
	}
}
