package city;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.ImageIcon;

import restaurantKC.KCCookRole;
import market.Invoice;
import Gui.Gui;

public class DeliveryDrone implements Gui{
	
	int xDestination = 340, yDestination = 420;
	int xPos = 340, yPos = 420; 
	CityData cd;
	KCCookRole cook;
	boolean isPresent = false;
	public List<Invoice> invoice = Collections.synchronizedList(new ArrayList<Invoice>());
	
	private enum Command {noCommand, goToBuilding, goBack};
	private Command command=Command.noCommand;
	
	public DeliveryDrone(List<Invoice> invoice, CityData cd, int x, int y, KCCookRole cook) {
		isPresent = true;
		this.cd = cd;
		this.cook = cook;
		this.invoice = invoice;
		
		cd.guis.add(this);
		
		xDestination = x;
		yDestination = y;
		
		command = Command.goToBuilding;
		
		/*if (b instanceof SMRestaurantBuilding) {
			xDestination = 200;
			yDestination = 140;
		}
		if (b instanceof KCRestaurantBuilding) {
			xDestination = 200;
			yDestination = 260;
		}
		if (b instanceof BKRestaurantBuilding) {
			xDestination = 340;
			yDestination = 260;
		}
		if (b instanceof MQRestaurantBuilding) {
			xDestination = 200;
			yDestination = 540;
		}
		if (b instanceof LYRestaurantBuilding) {
			xDestination = 340;
			yDestination = 540;
		}*/
		
	}

	@Override
	public void updatePosition() {
		if (xPos < xDestination)
			xPos++;
		else if (xPos > xDestination)
			xPos--;

		if (yPos < yDestination)
			yPos++;
		else if (yPos > yDestination)
			yPos--;
		
		if ((xPos == xDestination) && (yPos == yDestination)) {
			
			if (command == Command.goToBuilding) {
				if ((xDestination == 200) && (yDestination == 260)) {
					cook.msgOrdersFulfilled(invoice);
					
					xDestination = 340;
					yDestination = 420;
					command = command.goBack;
					
					return;
				}
			}
			
			if (command == Command.goBack) {
				isPresent = false;
			}
			command = Command.noCommand;
		}
	}

	@Override
	public void draw(Graphics2D g) {
		ImageIcon icon = new ImageIcon("res/drone.png");
		g.drawImage(icon.getImage(), xPos, yPos, null);
	}

	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return isPresent;
	}

	@Override
	public void setPresent(boolean b) {
		// TODO Auto-generated method stub
		
	}

}
