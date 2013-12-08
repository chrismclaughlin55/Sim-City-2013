package restaurantBK.gui;


import restaurantBK.interfaces.Customer;
import restaurantBK.interfaces.Waiter;

import java.awt.*;

public class WaiterGui implements Gui {

    private Waiter agent = null;

    private int xPos = -20, yPos = -20;//default waiter position
    private int xDestination = -20, yDestination = -20;//default start position
    RestaurantGui gui;
    public int xTable1 = 200;
    public int xTable2 = 326;
    public int xTable3 = 450;
    public int yTable = 250;
    private int cookx = 520;
    private int cooky = 226;
    private int cashierx = 16;
    private int cashiery = 400;
    private int restx;
    private int resty=40;
    private int waiterNumber;
    private String currentorder;
    private boolean order = false;
    private boolean moving = false;
    public WaiterGui(Waiter agent, RestaurantGui gui, int num) {
        this.agent = agent;
        this.restx=agent.getRestX();
        this.gui = gui;
        this.waiterNumber = num;
    }

    public void updatePosition() {
    	//System.out.println(xPos + " " + yPos);
    	//System.out.println(xDestination + " yolo " + yDestination);
        if (xPos < xDestination)
            xPos+=2;
        else if (xPos > xDestination)
            xPos-=2;

        if (yPos < yDestination)
            yPos+=2;
        else if (yPos > yDestination)
            yPos-=2;

        if (xPos == xDestination && yPos == yDestination && moving) {
        	moving = false;
        	agent.msgAtDestination();
        	
        }
        if(xPos==-20 && yPos==-20)
        {
        	
        }
    }

    public void flipOrder(String o) {
    	currentorder=o;
    	if(order == false) {
			order = true;
		}
		else {
			order = false;
		}
    }
    public void draw(Graphics2D g) {
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, 20, 20);
        if(order) {
        	g.drawString(currentorder.substring(0, 2),xPos+15,yPos-5);
        }
        g.drawString("W"+waiterNumber, restx, resty);
        
    }

    public boolean isPresent() {
        return true;
    }
    public void DoGoRest(int x, int y) {
    	moving = true;
    	xDestination = x;
    	yDestination = y;
    	
    }
    public void DoBringToTable(Customer customer, int tn) {
    	//System.out.println("here?");
    	moving = true;
    	if(tn==1) {
    		xDestination = xTable1 + 20;           
    	}
    	if(tn==2) {
    		xDestination = xTable2 + 20;
    	}
    	if(tn==3) {
    		xDestination = xTable3 + 20;
    	}
    	yDestination = yTable - 20;
        //System.out.println(xDestination+ 		c.c.msgWhatWouldYouLike();"!!!!");
    }
    public void DoGoToCook() {
    	//agent.atDestination.acquire();
    	
    	moving=true;
    	xDestination = cookx;
    	yDestination = cooky;
    }		
    public void setBreakEnabledUnselect() {
    	gui.setWaiterEnabled(agent,false);
    	//gui.setWaiterEnabled(agent,false);
    }
    public void setBreakEnabledSelect() {
    	gui.setWaiterEnabled(agent,true);
    }
    
    public void DoGoToCashier() {
    	moving = true;
    	xDestination = cashierx;
    	yDestination = cashiery;
    }

    public void DoLeaveCustomer(int pos) {
    	
    	moving=true;
        xDestination = 30*pos;
        yDestination = 24;	
    }
    

    public void setWantsBreak() {
    	agent.msgChangeWantsBreak();
    }
    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
}
