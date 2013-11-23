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

import market.gui.AnimationPanel;

public class HomeGui extends JFrame implements ActionListener {

	public HomeGui() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(0,0,(int) (screenSize.width/1.5), (int) (screenSize.height/1.5));
		setLocation((screenSize.width/2-this.getSize().width/2), (screenSize.height/2-this.getSize().height/2));
		setLayout(new BorderLayout());
		
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
		infoPanel.add(new JLabel("Hello"));
		infoPanel.add(new JLabel("here is some home info    "));
		infoPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		this.add(infoPanel, BorderLayout.WEST);
		
		JPanel animationPanel = new HomePanel();
		animationPanel.add(new JLabel("animation goes here"));
		this.add(animationPanel, BorderLayout.CENTER);
	}
	
	public void actionPerformed(ActionEvent arg0) {
		
	}

}
