package city;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ApartmentGui extends JFrame implements ActionListener {

	ApartmentPanel aptPanel;
	List<Room> rooms;
	
	public ApartmentGui(List<Room> r) {
		setTitle("Apartment");
    	setVisible(false);
    	setResizable(false);
    	setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(0,0, 600, 400);
		setLocation((screenSize.width/2-this.getSize().width/2), (screenSize.height/2-this.getSize().height/2));
		setLayout(new BorderLayout());
		rooms = r;
		
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
		infoPanel.setPreferredSize(new Dimension(200, 400));
		infoPanel.add(new JLabel("Tenants", JLabel.CENTER));
		infoPanel.add(new JLabel("Landlord apt in gray"));
		infoPanel.add(new JLabel("More coming in v2"));
		this.add(infoPanel, BorderLayout.WEST);
		
		aptPanel = new ApartmentPanel();
		aptPanel.setPreferredSize(new Dimension(400, 400));
		this.add(aptPanel, BorderLayout.CENTER);
	}
	
	public void addName(PersonAgent p) {
		
	}
	
	public void actionPerformed(ActionEvent arg0) {
		
	}

	public ApartmentPanel getAptPanel() {
		return aptPanel;
	}

}
