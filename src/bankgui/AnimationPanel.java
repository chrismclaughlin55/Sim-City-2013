package bankgui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.*;

import market.gui.CustomerGui;
import market.gui.EmployeeGui;
import Gui.*;
import market.gui.ManagerGui;

public class AnimationPanel extends JPanel implements ActionListener{
	private List<Gui> guis = Collections.synchronizedList(new ArrayList<Gui>());
	private Image bufferImage;
	private Dimension bufferSize;
	private int frameDisplay = 2;

	public AnimationPanel() {
		setBackground(Color.WHITE);
		setVisible(true);

		bufferSize = this.getSize();

		Timer timer = new Timer(frameDisplay, this );
		timer.start();

		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		Color tableColor = new Color(149, 165, 166);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		//Clear the screen by painting a rectangle the size of the frame
		g2.setColor(getBackground());
		g2.fillRect(0, 0, screenSize.width/4, screenSize.height/2 );

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

	public void addGui(CustomerGui gui) {
		guis.add(gui);
	}

	public void removeGui(EmployeeGui gui) {
		guis.remove(gui);
	}

	public void addGui(ManagerGui gui) {
		guis.add(gui);
	}

}
