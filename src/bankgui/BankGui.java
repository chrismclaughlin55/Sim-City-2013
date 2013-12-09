package bankgui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import bank.Bank;
import bank.utilities.GuiPositions;



public class BankGui extends JFrame implements GuiPositions, ActionListener {

		public AnimationPanel animationPanel;
		private BankPanel bankPanel;
		public Bank bank;
		public BankGui(Bank bank) {
			this.bank = bank;
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			setBounds(0,0,600, 400);
			setLocation(screenSize.width/2-this.getSize().width/2, screenSize.height/2-this.getSize().height/2);
			setLayout(new GridLayout(0,2));
			
			
			
			animationPanel = new AnimationPanel();
			animationPanel.setVisible(true);
			bankPanel = new BankPanel(bank);
			bankPanel.setVisible(true);


			add (bankPanel);
			add (animationPanel);
			

}
		public void setBank(Bank b){
			this.bank = b;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		
}
