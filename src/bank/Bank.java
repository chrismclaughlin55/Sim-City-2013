package bank;

import java.util.Map;

import bankgui.BankCustomerGui;
import bankgui.BankGui;
import bankgui.TellerGui;
import mainGUI.MainGui;
import city.Building;
import city.CityData;
import city.PersonAgent;

public class Bank extends Building {
	public BankGui bankGui;
	BankManagerRole currentManager = null;
	Map<PersonAgent, CustomerRole> existingCustRoles;
	Map<PersonAgent, BankManagerRole> existingManagerRoles;
	Map<PersonAgent, TellerRole> existingTellerRoles;
	public Bank(int xPos, int yPos, int width, int height, String name, BuildingType bank, MainGui mainGui, CityData cd) {
		super(xPos, yPos, width, height, name, bank, mainGui);
		cityData = cd;
		this.bankGui = new BankGui();
		mainGui.bankGui = this.bankGui;
		mainGui.bankGui.bank = this;
	}
	@Override
	public void EnterBuilding(PersonAgent p, String roleRequest){
		p.print("going into bank");
		if(roleRequest.equals("bankManager")){
			if(p.equals(manager)){
				if(existingManagerRoles.get(p) != null){
				setOpen(p);
				p.msgAssignRole(existingManagerRoles.get(p));
				}
				else {
				existingManagerRoles.put(p, new BankManagerRole(p));
				setOpen(p);
				p.msgAssignRole(existingManagerRoles.get(p));
				}
			}
		}
		if(roleRequest.equals("bankCustomer")){
			if(isOpen()){
				if(existingCustRoles.get(p) != null){
					CustomerRole role = existingCustRoles.get(p);
					BankCustomerGui custGui = new BankCustomerGui(role);
					role.msgAddGui(custGui);
					p.msgAssignRole(role);
					bankGui.animationPanel.addGui(custGui);
				}
				else{
					CustomerRole newRole = new CustomerRole(p);
					existingCustRoles.put(p, newRole);
					BankCustomerGui custGui = new BankCustomerGui(newRole);
					p.msgAssignRole(newRole);
					bankGui.animationPanel.addGui(custGui);
				}
			}
		}
		if(roleRequest.equals("bankTeller")){
			if(isOpen()){
				if(existingTellerRoles.get(p) != null){
					TellerRole role = existingTellerRoles.get(p);
					TellerGui tellerGui = new TellerGui(existingTellerRoles.get(p));
					role.msgAddGui(tellerGui);
					p.msgAssignRole(role);
					role.msgAddGui(tellerGui);
					bankGui.animationPanel.addGui(tellerGui);
				}
				else{
					CustomerRole newRole = new CustomerRole(p);
					existingCustRoles.put(p, newRole);
					BankCustomerGui custGui = new BankCustomerGui(newRole);
					p.msgAssignRole(newRole);
					bankGui.animationPanel.addGui(custGui);
				}
			}
		}
	}
}
