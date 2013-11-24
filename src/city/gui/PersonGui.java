package city.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.*;

import javax.swing.ImageIcon;

import Gui.Gui;
import mainGUI.MainGui;
import city.PersonAgent;
import city.Building;

public class PersonGui implements Gui{
	
	private PersonAgent agent = null;
	private boolean isPresent = true;
	
	
	private static final int INITX = 40;
	private static final int INITY = 40;
	public static final int WIDTH = 20;

	private int xPos, yPos;
	private int xDestination, yDestination;
	
	public static final int xBuilding[] = {52, 52, 52, 52, 232, 362, 542, 542, 542, 542, 362, 232, 243, 373, 243, 373, 232, 360, 243, 373};
	public static final int yBuilding[] = {198, 328, 468, 598, 738, 738, 598, 468, 328, 198, 58, 58, 202, 202, 332, 332, 470, 470, 602, 602};
	
	MainGui gui;

	public PersonGui(PersonAgent p, MainGui g){ //HostAgent m) {
		agent = p;
		gui = g;
		xPos = INITX;
		yPos = INITY;
		xDestination = INITX;
		yDestination = INITY;
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
		
		for (int i = 0; i < 20; i++) {
        	if (xPos == xDestination && yPos == yDestination
        			& (xDestination == xBuilding[i]) & (yDestination == yBuilding[i])) {
        		agent.msgAtBuilding();
        	}
        }
	}
	

	@Override
	public void draw(Graphics2D g) {
		//g.setColor(Color.GREEN);
		//g.fillRect(xPos, yPos, WIDTH, WIDTH);
		Graphics2D g2 = (Graphics2D)g;
		ImageIcon person = new ImageIcon("res/person.png");
		g2.drawImage(person.getImage(), xPos, yPos, null);
	}
	
	public void DoGoToEntrance() {
		xDestination = 0;
		yDestination = 340;
	}
	
	public void DoGoToRefridgerator() {
		xDestination = 30;
		yDestination = 30;
	}
	
	public void DoGoToBed() {
		xDestination = 360;
		yDestination = 20;
	}
	
	public void DoGoToStove() {
		xDestination = 105;
		yDestination = 25;
	}
	
	public void DoGoToWall() {
		xDestination = 190;
		yDestination = 340;
	}
	
	public void DoGoToCouch() {
		xDestination = 170;
		yDestination = 190;
	}

	@Override
	public boolean isPresent() {
		return isPresent;
	}
	
	public void DoGoToBuilding(int buildingNumber) {
		xDestination = xBuilding[buildingNumber];
		yDestination = yBuilding[buildingNumber];
	}

	public void DoGoIntoBuilding() {
		isPresent = false;
	}
}
