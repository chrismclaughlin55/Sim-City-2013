package restaurantMQ.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Gui.Gui;
import restaurantMQ.HostAgent;
import restaurantMQ.WaiterAgent;
import restaurantMQ.interfaces.Customer;
import restaurantMQ.interfaces.Waiter;

public class WaiterGui implements Gui 
{

    private Waiter agent = null;

    private int xPos = -20, yPos = -20;//default waiter position
    private int xDestination = -20, yDestination = -20;//default start position
    boolean cooking = false;
    private int waiterNumber;

    private class OrderGui
    {
    	String name;
    	Customer customer;
    	boolean cooking;
    	
    	OrderGui(String name, Customer customer, boolean cooking)
    	{
    		this.name = name;
    		this.customer = customer;
    		this.cooking = cooking;
    	}
    	
    	boolean equalsOrder(OrderGui o)
    	{
    		return this.name.equals(o.name) && this.customer == o.customer;
    	}
    }
    private List<OrderGui> orders = Collections.synchronizedList(new ArrayList<OrderGui>());
    
    public static final int NTABLES = 4;
    public static final int NSPOTS = 4;
    public static final int xTable = 150;
    public static final int yTable = 200;
    public static final int WIDTH = 20;
    public static final int TABLEDIST = 100;
    public static final int NUMROWS = 2;
    private static final int WINDOWX = 350;
    private static final int WINDOWY = 350;
    public static final int cookx = WINDOWX;
    public static final int cooky = 10*WIDTH;
    
    private class Coordinates
	{
		int x, y;
		
		Coordinates(int x, int y)
		{
			this.x = x;
			this.y = y;
		}
	}
	
	private Map<Integer, Coordinates> tableMap = new HashMap<Integer, Coordinates>();
	private Map<Integer, Coordinates> waitingMap = new HashMap<Integer, Coordinates>();
    
    private boolean moving = false;

    public WaiterGui(Waiter agent) 
    {
        this.agent = agent;
        
        for(int i = 1; i <= NTABLES; ++i)
		{
			tableMap.put(i, new Coordinates(xTable + (((i -1) / NUMROWS)*TABLEDIST), (yTable + ((i-1) % NUMROWS)*TABLEDIST - WIDTH)));
		}
        
        for(int i = 1; i <= NSPOTS; ++i)
		{
			waitingMap.put(i, new Coordinates(WIDTH + 2*(((i-1) / NUMROWS)*WIDTH), (WIDTH + 2*((i - 1) % NUMROWS)*WIDTH - WIDTH)));
		}
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

        if (moving && xPos == xDestination && yPos == yDestination
        		&& (xDestination >= 0) && (yDestination >= 0)) {
           agent.msgAnimationDone();
        }
        if(moving && xPos == -1*WIDTH && yPos == -1*WIDTH)
        {
        	moving = false;
        	agent.msgAnimationDone();
        }
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, WIDTH, WIDTH);
        synchronized(orders)
        {
        	for(OrderGui o : orders)
        	{
	        	if(o.cooking)
	        	{
	        		if(o.name.endsWith("?"))
	        		{
		        		g.setColor(Color.BLACK);
		        		g.drawRect(WINDOWX + 5*WIDTH, WIDTH + waiterNumber*WIDTH, WIDTH, WIDTH);
		        		g.drawString(o.name, WINDOWX + 5*WIDTH, 2*WIDTH + waiterNumber*WIDTH);
	        		}
	        		else
	        		{
	        			g.setColor(Color.BLACK);
		        		g.drawRect(WINDOWX + 3*WIDTH, WIDTH + waiterNumber*WIDTH, WIDTH, WIDTH);
		        		g.drawString(o.name, WINDOWX + 3*WIDTH, 2*WIDTH + waiterNumber*WIDTH);
	        		}
	        	}
	        	else
	        	{
	        		g.setColor(Color.BLACK);
	        		g.drawRect(xPos + WIDTH, yPos, WIDTH, WIDTH);
	        		g.drawString(o.name, xPos + WIDTH, yPos+WIDTH);
	        	}
        	}
        }
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
    	xDestination = tableMap.get(tableNum).x;
		yDestination = tableMap.get(tableNum).y;
        moving = true;
    }
    
    public void DoGoToSpot(int number)
    {
    	xDestination = waitingMap.get(number).x;
    	yDestination = waitingMap.get(number).y;
    	moving = true;
    }
    
    public void DoGoToCook()
    {
    	xDestination = WINDOWX + 2*WIDTH;
        yDestination =  WIDTH + waiterNumber*WIDTH;
        moving = true;
    }

    public void DoLeaveCustomer() {
    	xDestination = WINDOWX;
        yDestination =  WIDTH + waiterNumber*WIDTH;
        moving = true;
    }
    
    public void setOrder(String choice, Customer cust)
    {
    	cooking = false;
    	String name = choice.substring(0, 1) + choice.substring(1,2);
    	OrderGui order = new OrderGui(name, cust, false);
    	synchronized(orders)
    	{
    		for(OrderGui o : orders)
    		{
    			if(o.equalsOrder(order))
    			{
    				o.cooking = false;
    				return;
    			}
    		}
    	}
    }
    
    public void setCooking(String choice, Customer cust)
    {
    	cooking = true;
    	orders.add(new OrderGui((choice.substring(0, 1) + choice.substring(1,2) + "?"), cust, true)); //First two characters of the choice
    }
    
    public void setCookingDone(String choice, Customer cust)
    {
    	cooking = true;
    	String name = choice.substring(0, 1) + choice.substring(1,2) + "?";
    	OrderGui order = new OrderGui(name, cust, false);
    	synchronized(orders)
    	{
    		for(OrderGui o : orders)
    		{
    			if(o.equalsOrder(order))
    			{
    				o.name = choice.substring(0, 1) + choice.substring(1,2);
    				return;
    			}
    		}
    	} //First two characters of the choice
    }

    public void clearOrder(String choice, Customer cust)
    {
    	String name = choice.substring(0, 1) + choice.substring(1,2);
    	OrderGui order = new OrderGui(name, cust, false);
    	synchronized(orders)
    	{
    		for(OrderGui o : orders)
    		{
    			if(o.equalsOrder(order))
    			{
    				orders.remove(o);
    				return;
    			}
    		}
    	}
    }
    
    public void DefaultAction()
    {
    	xDestination = WINDOWX;
        yDestination =  WIDTH + waiterNumber*WIDTH;
        moving = false;
    }
    
    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
    
    public void setNumber(int n)
    {
    	waiterNumber = n;
    }
}