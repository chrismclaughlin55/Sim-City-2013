package market.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;

public class CustomerGui implements Gui{
	
	int xPos = 0;
	int yPos = 0;
	

	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Graphics2D g) {
		Color customerColor = new Color (46, 204, 113);
		g.setColor(customerColor);
		g.fillRect(xPos, yPos, 30, 30);
	}

	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return true;
	}

}
