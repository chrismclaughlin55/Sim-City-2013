package city.gui;

import java.awt.Graphics2D;

import javax.swing.ImageIcon;

import mainGUI.MainGui;
import Gui.Gui;
import city.BusAgent;
import city.CityData;

public class BusGui implements Gui {
	CityData cd;
	private BusAgent agent = null;
	int xPos;
	int yPos;
	boolean xmove = false;
	boolean xFirst = false;
	boolean yFirst = false;
	private int xDestination, yDestination;//default start position
    boolean betweenStops = false;
    boolean moving = false;
    boolean isPresent = true;
    int stop;
    MainGui m;

    public BusGui(BusAgent ba, MainGui main, CityData cd) {
    	this.cd = cd;
    	agent = ba;
    	xPos = cd.busStops.get(0).getX();
    	yPos = cd.busStops.get(0).getY();
    	m = main;
    }
	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		if(xFirst) {
			if (xPos < xDestination)
		       xPos+=5;
		    if (xPos > xDestination)
		       xPos-=5;
			if (xPos == xDestination) {
				xFirst = false;
			}			
		}	
		if(yFirst) {
			if (yPos < yDestination)
			   yPos+=5;
			if (yPos > yDestination)
			   yPos-=5;
			if (yPos == yDestination) {
				yFirst = false;
			}
		}
		if(!(xFirst||yFirst)) {
			if (xPos < xDestination)
			   xPos+=5;
			if (xPos > xDestination)
			   xPos-=5;
			if (xPos == xDestination) {
				xFirst = false;
			}	
			if (yPos < yDestination)
				yPos+=5;
			if (yPos > yDestination)
				yPos-=5;
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
		return isPresent;
	}
	
	public void DoGoToNextStop(int x, int y) {
		//System.out.println("What");
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
