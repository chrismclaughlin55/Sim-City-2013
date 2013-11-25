package city;

import javax.swing.*;

import city.gui.PersonGui;


import Gui.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.awt.Graphics;


public class HomePanel extends JPanel implements ActionListener {

	private Image bufferImage;
	private Dimension bufferSize;
	private int frameDisplay = 2;
	private int width = 400;
	private int height = 400;
	

	private List<Gui> guis = Collections.synchronizedList(new ArrayList<Gui>());

	public HomePanel() {
		setBackground(Color.WHITE);
		setVisible(true);
		bufferSize = this.getSize();

		Timer timer = new Timer(frameDisplay, this);
		timer.start();
		
		/**
		 * Used to test the gui
		 * guis.add(new PersonGui(new PersonAgent("Dave")));
		 * PersonGui g1 = (PersonGui) guis.get(0);
		 * g1.DoGoToBed();
		 */
		
	}

	public void actionPerformed(ActionEvent e) {
		synchronized(guis){
			for(Gui gui : guis) {
				if (gui.isPresent()) {
					gui.updatePosition();
				}
			}
		}
		repaint();  //Will have paintComponent called
	}
	
	public List<Gui> getGuis() {
		return guis;
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		//Clear the screen by painting a rectangle the size of the frame
		g2.setColor(getBackground());
		g2.fillRect(0, 0, width, height);
		g2.setColor(Color.BLACK);
		g2.drawLine(width/2, 0, width/2, height - 80);
		g2.fillRect((width/4 - 10), 0, 50, 25);
		g2.setColor(Color.CYAN);
		g2.fillRect(0, height - 70, 30, 40);
		g2.setColor(Color.BLUE);
		g2.fillRect(0, 20, 30, 40);
		g2.setColor(Color.PINK);
		g2.fillRect(width - 60, 0, 60, 100);
		g2.setColor(Color.GRAY);
		g2.fillRect(width/2 - 40, height/2 - 30, 40, 60);
		

		synchronized(guis){
			for(Gui gui : guis) {
				if (gui.isPresent()) {
					gui.draw(g2);
				}
			}
		}
	}

	public void addGui(PersonGui gui) {
		guis.add(gui);
		gui.setXPos(0);
		gui.setYPos(340);
		gui.setPresent(true);
	}


}


