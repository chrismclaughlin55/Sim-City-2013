package city;

public abstract class Role 
{
	//DATA MEMBERS
	private PersonAgent person;
	private boolean isActive = false;
	
	//CONSTRUCTORS
	public Role(PersonAgent person)
	{
		this.person = person;
	}
	
	//GETTERS/SETTERS
	public boolean isActive()
	{
		return isActive;
	}
	
	protected void setActive()
	{
		isActive = true;
	}
	
	protected void setInactive()
	{
		isActive = false;
	}
	
	protected void stateChanged()
	{
		person.stateChanged();
	}
	
	public abstract boolean pickAndExecuteAnAction();
}
