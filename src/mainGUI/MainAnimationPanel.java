package mainGUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

import city.Building;
import city.gui.Gui;
import city.gui.PersonGui;

public class MainAnimationPanel extends JPanel implements ActionListener {

	private int frameDisplay = 2;

	private List<Gui> guis = Collections.synchronizedList(new ArrayList<Gui>());
	private List<Building> buildings = Collections.synchronizedList(new ArrayList<Building>());

	public MainAnimationPanel() {
		//Add buildings
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				Building b = new Building(190+i*130, j*680, 50, 50);
				buildings.add(b);
			}
		}
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				Building b = new Building(190+i*130, 140+j*130, 50, 50);
				buildings.add(b);
			}
		}
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				Building b = new Building(190+i*130, 410+j*130, 50, 50);
				buildings.add(b);
			}
		}
		for (int i = 0; i < 2; i++) {
			Building b = new Building(10, 140+i*130, 50, 50);
			buildings.add(b);
		}
		for (int i = 0; i < 2; i++) {
			Building b = new Building(10, 410+i*130, 50, 50);
			buildings.add(b);
		}
		for (int i = 0; i < 2; i++) {
			Building b = new Building(500, 140+i*130, 50, 50);
			buildings.add(b);
		}
		for (int i = 0; i < 2; i++) {
			Building b = new Building(500, 410+i*130, 50, 50);
			buildings.add(b);
		}
		
		//setBackground(Color.WHITE);
		
		setVisible(true);
		Timer timer = new Timer(frameDisplay, this );
		timer.start();

	}

	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		//Clear the screen by painting a rectangle the size of the frame
		//g2.setColor(getBackground());
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, screenSize.width/2, screenSize.height );
		ImageIcon background = new ImageIcon("res/background.png");
		g2.drawImage(background.getImage(), 0, 0, null);
		
		//Draw houses
		ImageIcon house = new ImageIcon("res/house.png");
		for (int i = 0; i < 4; i++) {
			g2.drawImage(house.getImage(), (int) buildings.get(i).x, (int) buildings.get(i).y, null);
		}
		for (int i = 12; i < 20; i++) {
			g2.drawImage(house.getImage(), (int) buildings.get(i).x, (int) buildings.get(i).y, null);
		}
		
		//Draw restaurants
		ImageIcon rest = new ImageIcon("res/restaurant.png");
		for (int i = 4; i < 8; i++) {
			g2.drawImage(rest.getImage(), (int) buildings.get(i).x, (int) buildings.get(i).y, null);
		}
        g2.drawImage(rest.getImage(), (int) buildings.get(9).x, (int) buildings.get(9).y, null);
        g2.drawImage(rest.getImage(), (int) buildings.get(11).x, (int) buildings.get(11).y, null);
		
		//Draw bank
        ImageIcon bank = new ImageIcon("res/bank.png");
        g2.drawImage(bank.getImage(), (int) buildings.get(8).x, (int) buildings.get(8).y, null);
		
		//Draw market
        ImageIcon market = new ImageIcon("res/market.png");
        g2.drawImage(market.getImage(), (int) buildings.get(10).x, (int) buildings.get(10).y, null);
        
		//height 900, width 720
        //Draw road
        ImageIcon road1 = new ImageIcon("res/road1.png");
        for (int i = 0; i < screenSize.width/2; i++) {
        	g2.drawImage(road1.getImage(), i, 365, null);
        }
        for (int i = 130; i < 470; i++) {
        	g2.drawImage(road1.getImage(), i, 95, null);
        }
        for (int i = 130; i < 470; i++) {
        	g2.drawImage(road1.getImage(), i, 635, null);
        }
        ImageIcon road2 = new ImageIcon("res/road2.png");
        for (int i = 95; i < 660; i++) {
        	g2.drawImage(road2.getImage(), 130, i, null);
        }
        for (int i = 95; i < 660; i++) {
        	g2.drawImage(road2.getImage(), 440, i, null);
        }
        ImageIcon road3 = new ImageIcon("res/road3.png");
        g2.drawImage(road3.getImage(), 130, 365, null);
        g2.drawImage(road3.getImage(), 440, 365, null);
        g2.drawImage(road3.getImage(), 130, 95, null);
        g2.drawImage(road3.getImage(), 440, 95, null);
        g2.drawImage(road3.getImage(), 130, 635, null);
        g2.drawImage(road3.getImage(), 440, 635, null);

		synchronized(guis){
			for(Gui gui : guis) {
				if (gui.isPresent()) {
					gui.updatePosition();
				}
			}
		}
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
		System.out.println ("added gui!");
	}



}