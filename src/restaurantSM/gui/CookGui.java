package restaurantSM.gui;

import restaurantSM.CookAgent;
import restaurantSM.HostAgent;

import java.awt.*;

public class CookGui implements Gui{

	private CookAgent agent = null;
	private boolean isPresent = false;
	private String statusText = "";

	//private HostAgent host;
	RestaurantGui gui;

	private int xPos, yPos;
	private int xDestination, yDestination;
	public boolean moving = false;

	public static final int xTable = 200;
	public static final int yTable = 250;
	public static final int posChange = 20;
	public static final int grillXPos = 350;
	public static final int grillYPos = 50;
	public static final int platingXPos = 400;
	public static final int platingYPos = 100;
	public static final int fridgeXPos = 450;
	public static final int fridgeYPos = 50;
	public static final int xHome = 400;
	public static final int yHome = 50;
	public boolean food = false;

	public CookGui(CookAgent c, RestaurantGui gui){ //HostAgent m) {
		agent = c;
		xPos = xHome;
		yPos = yHome;
		xDestination = xPos;
		yDestination = yPos;
		this.gui = gui;
		isPresent = true;
	}

	public void addFood(){
		food = true;
	}
	public void removeFood(){
		food = false;
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
	
	public void DoGoToGrills(){
		moving = true;
		xDestination = grillXPos;
		yDestination = grillYPos;
	}
	
	public void DoGoToPlating(){
		moving = true;
		xDestination = platingXPos;
		yDestination = platingYPos;
	}
	
	public void DoGoToFridge(){
		moving = true;
		xDestination = fridgeXPos;
		yDestination = fridgeYPos;
	}
	
	public void DoGoToHome(){
		moving = true;
		xDestination = xHome;
		yDestination = yHome;
	}
	
	public void setStatusText(String s){
		statusText = s;
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.fillRect(xPos, yPos, posChange, posChange);
		if (food){
			g.setColor(Color.RED);
			g.fillRect(xPos+5, yPos+20, 10, 10);
		}
		g.drawString(statusText, xPos, yPos);
	}

	public boolean isPresent() {
		return isPresent;
	}
	

	public void setPresent(boolean p) {
		isPresent = p;
	}

	
	
}
