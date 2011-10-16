/**
 * This class is used to hold information about a specific company
 * @author brian
 *
 */
import java.util.ArrayList;


public class CompanyObject{
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
	
	public CompanyObject(ArrayList<String> a)
	{
		symbol = a.get(0);
		name =a.get(1);
		lastSale = a.get(2);
		marketCap = a.get(3);
		adrTSO = a.get(4);
		ipoYear = a.get(5);
		sector = a.get(6);
		industry = a.get(7);
		summaryQuote = a.get(8);
		exchange = a.get(9);
	}
	
	public String toString()
	{
		return "Symbol: "+ symbol+
				"\tName: " +name+
				"\tLast Sale: " + lastSale +
				"\tMarket Cap: " + marketCap +
				"\tADR TSO: " + adrTSO +
				"\tIPO Year: "+ipoYear+
				"\tSector: " +sector+
				"\tIndustry: " + industry+
				"\tSummary Quote: "+ summaryQuote +
				"\tExchange: "+exchange;
	}
}
