package restaurant;

//Hack - market orders are shared but the methods provided make them read-only
public class MarketOrder 
{
	private String name;
	private int amount;
	
	public MarketOrder(String name, int amount)
	{
		this.name = name;
		this.amount = amount;
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getAmount()
	{
		return amount;
	}
}
