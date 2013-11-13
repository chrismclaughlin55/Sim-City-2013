package restaurantMQ.gui;


import restaurantMQ.interfaces.Customer;
import restaurantMQ.interfaces.Host;

import java.awt.*;

public class HostGui implements Gui {

    private Host agent = null;

    private int xPos = -20, yPos = -20;//default waiter position
    private int xDestination = -20, yDestination = -20;//default start position

    public static final int xTable = 150;
    public static final int yTable = 200;
    public static final int WIDTH = 20;
    public static final int TABLEDIST = 100;
    public static final int NUMROWS = 2;
    
    private boolean moving = false;

    public HostGui(Host agent) {
        this.agent = agent;
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
        		&& (xDestination > 0) && (yDestination > 0)) {
           agent.msgAtTable();
        }
        if(moving && xPos == -1*WIDTH && yPos == -1*WIDTH)
        {
        	moving = false;
        	agent.msgReadyForCust();
        }
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, WIDTH, WIDTH);
    }

    public boolean isPresent() {
        return true;
    }

    public void DoBringToTable(Customer customer) {
        xDestination = xTable + WIDTH;
        yDestination = yTable - WIDTH;
        moving = true;
    }
    
    public void DoBringToTable(Customer customer, int tableNum) {
    	//The -1 term is to account for off-by-1 math
        xDestination = xTable + ((tableNum-1) / NUMROWS)*TABLEDIST + WIDTH;
        yDestination = yTable + ((tableNum-1) % NUMROWS)*TABLEDIST - WIDTH;
        moving = true;
    }
    
    public void GoToTable(int tableNum) {
    	//The -1 term is to account for off-by-1 math
        xDestination = xTable + ((tableNum-1) / NUMROWS)*TABLEDIST + WIDTH;
        yDestination = yTable + ((tableNum-1) % NUMROWS)*TABLEDIST - WIDTH;
        moving = true;
    }

    public void DoLeaveCustomer() {
        xDestination = -1*WIDTH;
        yDestination = -1*WIDTH;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
}
