import java.math.BigDecimal;


public class StockObject {
	String date;
	BigDecimal open;
	BigDecimal high;
	BigDecimal low;
	BigDecimal close;
	BigDecimal volume;
	BigDecimal adjClose;

	public StockObject(String date,String open, String high, String low, String close, String volume, String adjClose)
	{
		this.date = date;
		this.open = new BigDecimal(open);
		this.high = new BigDecimal(high);
		this.low = new BigDecimal(low);
		this.close = new BigDecimal(close);
		this.volume = new BigDecimal(volume);
		this.adjClose = new BigDecimal(adjClose);
	}
	
	public String toString()
	{
		return "Date: "+date+
				"\t Open: "+open+
				"\t High: "+high+
				"\t Low: "+low+
				"\t Close: "+close+
				"\t Volume: "+volume+
				"\t Adj Close:"+adjClose;
	}
}
