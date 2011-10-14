
public class CompanyObject {
	String symbol;
	String name;
	String lastSale;
	String marketCap;
	String adrTSO;
	String ipoYear;
	String sector;
	String industry;
	String summaryQuote;
	String exchange;
	
	public CompanyObject(String[] a)
	{
		symbol = a[0];
		name = a[1];
		lastSale = a[2];
		marketCap = a[3];
		adrTSO = a[4];
		ipoYear = a[5];
		sector = a[6];
		industry = a[7];
		summaryQuote = a[8];
		exchange = a[9];
	}

}
