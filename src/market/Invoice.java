package market;

public class Invoice {

	String type;
	int amount;
	double price;
	public int marketNum;

	public Invoice(String type, int amount, double price, int marketNum) {
		this.type = type;
		this.amount = amount;
		this.price = price;
		this.marketNum = marketNum;
	}

}

