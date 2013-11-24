package city;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.*;

import city.gui.PersonGui;
import market.gui.AnimationPanel;

public class HomeGui extends JFrame implements ActionListener {

	private HomePanel homePanel;
	
	public HomeGui() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(0,0, 600, 400);
		setLocation((screenSize.width/2-this.getSize().width/2), (screenSize.height/2-this.getSize().height/2));
		setLayout(new BorderLayout());
		
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
		infoPanel.setPreferredSize(new Dimension(200, 400));
		infoPanel.add(new JLabel("Hello"));
		infoPanel.add(new JLabel("here is some home info this home is pretty great and stuff"));
		this.add(infoPanel, BorderLayout.WEST);
		
		homePanel = new HomePanel();
		this.add(homePanel, BorderLayout.CENTER);
	}
	
	public HomePanel getHomePanel(){
		return homePanel;
	}
	
	public void actionPerformed(ActionEvent arg0) {
		
	}

}
