package market;

public class CookMarketOrder {
	public String type;
	public int amount;
	public enum MarketOrderState {pending, ordering, done, ordered};

	public MarketOrderState state;

	public CookMarketOrder(String t, int a) {
		type = t;
		amount = a;
		state = MarketOrderState.pending;
	}
}