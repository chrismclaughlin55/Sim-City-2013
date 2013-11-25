package city.gui;
import java.awt.Graphics2D;

import javax.swing.ImageIcon;

import Gui.Gui;

public class BusStopGui implements Gui {
	
	int xPos;
	int yPos;
	public BusStopGui(int xcord, int ycord) {
		// TODO Auto-generated constructor stub
		xPos = xcord;
		yPos = ycord;
 	}

	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		Graphics2D g2 = (Graphics2D)g;
		ImageIcon person = new ImageIcon("res/busstop.png");
		g2.drawImage(person.getImage(), xPos, yPos, null);
	}

	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setPresent(boolean b) {
		// TODO Auto-generated method stub
		
	}

}
