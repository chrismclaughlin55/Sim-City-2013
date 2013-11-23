package bankgui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;



public class BankGui extends JFrame implements ActionListener {

		public AnimationPanel animationPanel;
		private BankPanel bankPanel;

		public BankGui() {

			//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			setBounds(0,0,screenSize.width/2, screenSize.height/2);
			setLocation(screenSize.width/2-this.getSize().width/2, screenSize.height/2-this.getSize().height/2);
			setLayout(new GridLayout(0,2));
			animationPanel = new AnimationPanel();
			animationPanel.setVisible(true);
			
			bankPanel = new BankPanel();
			bankPanel.setVisible(true);


			add (bankPanel);
			add (animationPanel);

}

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		
}
