package restaurantBK.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import restaurantBK.interfaces.Cook;
import restaurantBK.interfaces.Waiter;

public class CookGui implements Gui{
	
	private Cook agent = null;
	private int xPos = 500, yPos = 226;
	private int home = 160;
	private int xDestination = 540, yDestination = 160;//starting/plating area
	RestaurantGui gui;
	class Plate {
		String name;
		PlateState ps;
		int table;
		public Plate(String x, int table) {
			ps=PlateState.cooking;
			this.table=table;
			name=x;
		}
	}
	public enum PlateState {cooking,plated, done};
	List<Plate> plates = Collections.synchronizedList(new ArrayList<Plate>());
	private int cooking = 192;
	private int plating=250;
	private boolean moving=false;
	public CookGui(Cook agent, RestaurantGui gui) {
        this.agent = agent;
        this.gui = gui;
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
    }
    public void draw(Graphics2D g) {
        g.setColor(Color.BLUE);
        g.fillRect(xPos, yPos, 20, 20);
       	for(Plate p:plates) {
       		if(p.ps==PlateState.plated) {
       			//System.out.println("what");
       			g.drawString(p.name.substring(0,2),510,plating);
       		}
       		/*if(p.ps==PlateState.done) {
       			//plates.remove(p);
       		}*/
        }
        //g.drawString(cookingNow.substring(0,2),xPos-5,yPos);
        /*if(order) {
        	g.drawString(currentorder.substring(0, 2),xPos+15,yPos-5);
        }*/
        //NEED TO DRAW PLATING AREA
        //NEED TO DRAW COOKING AREA
        //NEED TO DRAW FRIDGE
    }
    public void flipPlated(int tn) {
    	synchronized(plates) {	
    		//System.out.println("what3");
    		for(Plate p:plates) {
    			//System.out.println("What4");
    			if(p.table==tn) {
    				if(p.ps==PlateState.cooking) {
    					p.ps=PlateState.plated;
    				}
    				else {
    					p.ps=PlateState.done;
    				}
    			}
    		}
    	}
    }
    public void DoGoToHome() {
    	moving = true;
    	yDestination = home;
    }
    public void DoMoveToCook(String name,int tn) {
    	moving = true;
    	yDestination = cooking;
    	Plate p = new Plate(name,tn);
    	plates.add(p);
    }
    public void DoMoveToPlating() {
    	moving = true;
    	yDestination = plating;
    	//MAKE THE STRING STAY AT THE PLATING AREA FOR WAITER TO PICKUP
    	//MESSAGING SYSTEM TO MAKE STRING GO AWAY AS HE TAKES IT, JUST LIKE CUSTOMER
    	//AND WAITER HANDOFF OF FOOD, MIRROR THAT INTERACTION
    }
    public boolean isPresent() {
        return true;
    }
    
    public void DoGetFoodFromFridge() {
    	moving = true;
    }
}
