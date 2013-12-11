package restaurantSM.gui;



import restaurantSM.SMCustomerRole;
import restaurantSM.SMHostRole;
import restaurantSM.SMWaiterRole;
import restaurantSM.utils.*;

import java.awt.*;
import java.util.ArrayList;

public class WaiterGui implements Gui {

    private SMWaiterRole agent = null;

    private int xPos = -20, yPos = -20;//default waiter position
    private int xDestination = -20, yDestination = -20;//default start position

    public static final int posChange = 20;
    private int xCook = 400;
    private int yCook = 100;
    private int xHome;
    private int yHome;
    public boolean moving = false;
    private String statusText = "";

    public WaiterGui(SMWaiterRole agent, int loc) {
        this.agent = agent;
        yHome = 20;
        xHome = 50 + (loc*20) + (10 * loc);
        DoGoToFront();
    }

    public void updatePosition() {
        if (xPos < xDestination)
            xPos+= 2;
        else if (xPos > xDestination)
            xPos-= 2;

        if (yPos < yDestination)
            yPos+= 2;
        else if (yPos > yDestination)
            yPos-= 2;

        if (xPos == xDestination && yPos == yDestination && moving == true) {
        	moving = false;
        	agent.msgDoneMoving();
        }
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, posChange, posChange);
        g.drawString(statusText, xPos, yPos);
    }
    
    public void setStatusText(String s){
    	statusText = s;
    }

    public boolean isPresent() {
        return true;
    }
    
    public boolean isOnBreak() {
    	return agent.onBreak;
    }

    public void breakPlease() {
    	agent.askForBreak();
    }
    
    public void DoGoToLine(int i) {
    	moving = true;
    	xDestination = 40;
    	yDestination = 40 + (i*20) + (10 * i);
    	
    }
    
    public void DoBringToTable(Table table) {
    	
    	moving = true;
    	switch (table.tableNumber) {
    	case 1:
    		xDestination = 150 + posChange;
	        yDestination = 200 - posChange;
    		break;
    	case 2:
    		xDestination = 150 + posChange;
	        yDestination = 300 - posChange;
    		break;
    	case 3: 
    		xDestination = 150 + posChange;
	        yDestination = 400 - posChange;
    		break;
    	}
    	
    }

    public void DoLeaveCustomer() {
        xDestination = xHome;
        yDestination = yHome;
    }
    
    public void DoGoToFront() {
    	moving = true;
    	xDestination = xHome;
        yDestination = yHome;
    }
    
    public void DoGoToCook() {
    	moving = true;
    	xDestination = xCook;
    	yDestination = yCook + posChange;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
}
