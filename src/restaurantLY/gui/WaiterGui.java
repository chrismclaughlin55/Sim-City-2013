package restaurantLY.gui;

import restaurantLY.WaiterAgent;

import java.awt.*;

public class WaiterGui implements Gui {

    private WaiterAgent agent = null;
    private boolean isOnBreak = false;

    //private int xPos = 5, yPos = 30;//default waiter position
    //private int xDestination = 5, yDestination = 30;//default start position
    private int xPos, yPos;//default waiter position
    private int xDestination, yDestination;//default start position
    private int xOrigin, yOrigin;
    
    private String order = null;

    public static final int xTable[] = {55, 145, 235, 325};
    public static final int yTable[] = {230, 230, 230, 230};
    public static final int xCust[] = {30, 55, 80, 105, 130, 155, 180, 205, 230, 255, 280, 305, 330, 355, 380, 405};
    public static final int yCust[] = {5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5};
    
    public WaiterGui(WaiterAgent agent) {
        this.agent = agent;
        xPos = 5;
        yPos = 30;
        xDestination = 5;
        yDestination = 30;
    }
    
    public WaiterGui(WaiterAgent agent, int x, int y) {
        this.agent = agent;
        xPos = x;
        yPos = y;
        xDestination = x;
        yDestination = y;
        xOrigin = x;
        yOrigin = y;
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
        
        for (int i = 0; i < 4; i++) {
        	if (xPos == xDestination && yPos == yDestination
        			& (xDestination == xTable[i] + 20) & (yDestination == yTable[i] - 20)) {
        		agent.msgAtTable();
        	}
        }
        
        if (xPos == xDestination && yPos == yDestination
    			& (xDestination == 35) & (yDestination == 430)) {
    		agent.msgAtCook();
    	}
        
        if (xPos == xDestination && yPos == yDestination
    			& (xDestination == xOrigin) & (yDestination == yOrigin)) {
    		agent.msgAtDoor();
    	}
        
        for (int i = 0; i < 16; i++) {
        	if (xPos == xDestination && yPos == yDestination
        			& (xDestination == xCust[i]) & (yDestination == yCust[i] + 20)) {
        		agent.msgAtCust();
        	}
        }
    }

    
    public void draw(Graphics2D g) {
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, 20, 20);
        g.setColor(Color.BLACK);
        if (order != null) {
        	g.drawString(order, xPos, yPos);
        }
    }

    public boolean isPresent() {
        return true;
    }
    
    public void DoGoToTable(int tableNum) {
    	xDestination = xTable[tableNum] + 20;
    	yDestination = yTable[tableNum] - 20;
    }

    public void DoGoToOrigin() {
        xDestination = xOrigin;
        yDestination = yOrigin;
    }
    
    public void DoGoToCook() {
    	xDestination = 35;
    	yDestination = 430;
    }
    
    public void DoGoToCust(int custNum) {
    	xDestination = xCust[custNum];
    	yDestination = yCust[custNum] + 20;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
    
    public void setOnBreak() {
		isOnBreak = true;
		agent.msgOnBreak();
	}
    public boolean isOnBreak() {
		return isOnBreak;
	}
    
    public void placeFood(String order) {
    	this.order = order;
    }
    
    public void removeFood() {
    	this.order = null;
    }
}
