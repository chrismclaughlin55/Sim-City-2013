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

	private List<HomeGui> homeGuis;
	HomePanel aptPanel;
	
	public ApartmentGui() {
		setTitle("Apartment");
    	setVisible(false);
    	setResizable(false);
    	setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(0,0, 600, 400);
		setLocation((screenSize.width/2-this.getSize().width/2), (screenSize.height/2-this.getSize().height/2));
		setLayout(new BorderLayout());
		
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
		infoPanel.setPreferredSize(new Dimension(200, 400));
		infoPanel.add(new JLabel("Apartment"));
		infoPanel.add(new JLabel("this is an apt"));
		this.add(infoPanel, BorderLayout.WEST);
		
		aptPanel = new HomePanel();
		this.add(aptPanel, BorderLayout.CENTER);
	}
	
	public void actionPerformed(ActionEvent arg0) {
		
	}

}
