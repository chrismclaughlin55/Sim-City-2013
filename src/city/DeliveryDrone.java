package city;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.ImageIcon;

import restaurantKC.KCCookRole;
import market.Invoice;
import market.MarketManagerRole;
import Gui.Gui;

public class DeliveryDrone implements Gui{
	
	
	int homeX, homeY;
	public int xDestination, yDestination, xPos, yPos, origX, origY; 
	CityData cd;
	KCCookRole cook;
	public boolean isPresent = false;
	public List<Invoice> invoice = Collections.synchronizedList(new ArrayList<Invoice>());
	
	public enum Command {noCommand, goToBuilding, goBack};
	public Command command=Command.noCommand;
	
	MarketManagerRole m = null;
	
	public DeliveryDrone(List<Invoice> invoice, CityData cd, int x, int y, KCCookRole cook, int homeX, int homeY, MarketManagerRole m) {
		this.homeX = homeX;
		this.homeY = homeY;
		xPos = homeX;
		yPos = homeY;
		isPresent = true;
		this.cd = cd;
		this.cook = cook;
		this.invoice = invoice;
		this.m = m;
		
		cd.guis.add(this);
		
		xDestination = x;
		yDestination = y;
		origX = x;
		origY = y;
		
		command = Command.goToBuilding;
		
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
					if (cd.restaurants.get(2).isOpen)
						cook.msgOrdersFulfilled(invoice);
					else
						m.msgDeliveryFailed(this);
						
					xDestination = homeX;
					yDestination = homeY;
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
