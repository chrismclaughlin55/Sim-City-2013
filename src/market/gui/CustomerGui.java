package market.gui;

import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;

import Gui.Gui;

public class CustomerGui implements Gui{
	
	int xPos = 0;
	int yPos = 0;

	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Graphics2D g) {
		ImageIcon icon = new ImageIcon("res/marketcust_normal.png");
        g.drawImage(icon.getImage(), xPos, yPos, null);
	}

	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return false;
	}

}
