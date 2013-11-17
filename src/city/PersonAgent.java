package city;

import java.util.ArrayList;
import java.util.List;

import agent.Agent;

public class PersonAgent extends Agent
{
	//DATA MEMBERS
	String name;
	
	private List<Role> roles = new ArrayList<Role>(); //hold all possible roles (even inactive roles)
	
	//CONSTRUCTORS
	public PersonAgent()
	{
		
	}
	
	public void assignRole(Role role)
	{
		for(Role r : roles)
		{
			if(r == role)
			{
				r.setActive();
			}
		}
	}
	
	protected void stateChanged()
	{
		super.stateChanged();
	}
	
	protected boolean pickAndExecuteAnAction() 
	{
		for(Role role : roles)
		{
			if(role.isActive())
			{
				if(role.pickAndExecuteAnAction())
				{
					return true;
				}
			}
		}
		return false;
	}
	
}
