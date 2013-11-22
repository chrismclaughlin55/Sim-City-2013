package market;

public class MarketData {
	String type; 
	public int amount;
	double price;
	String custType;

	public MarketData(String type, int amount, double price){
		this.type = type;
		this.amount = amount;
		this.price = price;
	}
}

