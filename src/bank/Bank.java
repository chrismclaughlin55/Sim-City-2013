package bank;

import java.util.HashMap;
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
		existingCustRoles = new HashMap<PersonAgent, CustomerRole>();
		existingManagerRoles = new HashMap<PersonAgent, BankManagerRole>();
		existingTellerRoles = new HashMap<PersonAgent, TellerRole>();
	}
	@Override
	public void EnterBuilding(PersonAgent p, String roleRequest){
		p.print(roleRequest);
		if(roleRequest.equals("BankManager")){
			p.print(roleRequest);
			if(manager != null){
				manager = p;
			}
			if(p.equals(manager)){
				if(existingManagerRoles.get(p) != null){
				setOpen(p);
				p.msgAssignRole(existingManagerRoles.get(p));
				currentManager = existingManagerRoles.get(p);
				}
				else {
				existingManagerRoles.put(p, new BankManagerRole(p));
				setOpen(p);
				p.print("bank is open? "+ isOpen);
				p.msgAssignRole(existingManagerRoles.get(p));
				currentManager = existingManagerRoles.get(p);
				cityData.buildings.get(18).manager = currentManager.getPerson();
				p.print("current manager is " + currentManager.getName());
				}
			}
		}
		if(roleRequest.equals("Customer")){
			if(isOpen()){
				if(existingCustRoles.get(p) != null){
					CustomerRole role = existingCustRoles.get(p);
					BankCustomerGui custGui = new BankCustomerGui(role);
					role.msgAddGui(custGui);
					p.msgAssignRole(role);
					bankGui.animationPanel.addGui(custGui);
					p.print("current manager is " + currentManager.getName());
					currentManager.msgINeedService(role);
				}
				else{
					p.print("assigned cust gui");
					CustomerRole newRole = new CustomerRole(p);
					existingCustRoles.put(p, newRole);
					BankCustomerGui custGui = new BankCustomerGui(newRole);
					p.msgAssignRole(newRole);
					bankGui.animationPanel.addGui(custGui);
					p.print("current manager is " + currentManager.getName());
					currentManager.msgINeedService(newRole);
				}
			}
		}
		if(roleRequest.equals("BankTeller")){
			if(isOpen()){
				if(existingTellerRoles.get(p) != null){
					p.print("teller role existed for me");
					TellerRole role = existingTellerRoles.get(p);
					TellerGui tellerGui = new TellerGui(existingTellerRoles.get(p));
					role.msgAddGui(tellerGui);
					p.msgAssignRole(role);
					role.msgAddGui(tellerGui);
					bankGui.animationPanel.addGui(tellerGui);
					currentManager.msgAddTeller(role);
					role.msgAddManager(currentManager);
				}
				else{
					p.print("assigned teller gui");
					TellerRole newRole = new TellerRole(p);
					existingTellerRoles.put(p, newRole);
					TellerGui tellerGui = new TellerGui(newRole);
					p.msgAssignRole(newRole);
					bankGui.animationPanel.addGui(tellerGui);
					p.print("current manager is " + currentManager.getName());
					currentManager.msgAddTeller(newRole);
					newRole.msgAddManager(currentManager);
				}
			}
		}
	}
}
