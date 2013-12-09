package restaurantLY.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import restaurantLY.LYCookRole;
import restaurantLY.interfaces.Cook;
import Gui.Gui;

public class CookGui implements Gui {
	private Cook agent = null;
	
	private int xPos, yPos;//default waiter position
    private int xDestination, yDestination;//default start position
	
	private String order = null;
	private boolean cooking = false;
	
	private int foodX, foodY;
	
    RestaurantGui gui;
	private boolean isPresent = false;
    
    public CookGui(LYCookRole c) {
        agent = c;
        xPos = 145;
        yPos = 450;
        xDestination = 145;
        yDestination = 450;
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
        
        if (xPos == xDestination && yPos == yDestination
    			& (xDestination == 145) & (yDestination == 450)) {
    		agent.msgAtCookingArea();
    	}
        
        if (xPos == xDestination && yPos == yDestination
    			& (xDestination == 55) & (yDestination == 450)) {
    		agent.msgAtPlacingArea();
    	}
        
        if (xPos == xDestination && yPos == yDestination
    			& (xDestination == 235) & (yDestination == 450)) {
    		agent.msgAtRefrigerator();
    	}
        
        if (xPos == xDestination && yPos == yDestination
    			& (xDestination == -20) & (yDestination == -20)) {
    		agent.msgLeft();
    	}
	}
	
	public void draw(Graphics2D g) {
		g.setColor(Color.BLUE);
        g.fillRect(xPos, yPos, 20, 20);
		g.setColor(Color.WHITE);
		if (order != null) {
			g.drawString(order, xPos+5, yPos+30);
		}
		if (order != null && !cooking) {
			g.drawString(order, foodX+5, foodY+30);
		}
		/*if (order != null && cooking) {
    		g.drawString(order, foodX+5, foodY+30);
    	}
    	if (order != null && !cooking) {
    		g.drawString(order, foodX+5, foodY+30);
    	}*/
	}
	
	public void DoGoToCookingArea() {
        xDestination = 145;
        yDestination = 450;
    }
	
	public void DoGoToPlacingArea() {
        xDestination = 55;
        yDestination = 450;
    }
	
	public void DoGoToRefrigerator() {
        xDestination = 235;
        yDestination = 450;
    }
	
	public boolean isPresent() {
		return true;
	}
	
	public void placeFood(String order, boolean cooking) {
		this.order = order;
		this.cooking = cooking;
		if (cooking) {
			foodX = 145;
			foodY = 450;
		}
		else {
			foodX = 55;
			foodY = 450;
		}
	}
	
	public void removeFood() {
		this.order = null;
	}
	
	public void setPresent(boolean p) {
		isPresent = p;
	}
	
	public void DoLeaveRestaurant()
    {
    	xDestination = -20;
    	yDestination = -20;
    }
}
