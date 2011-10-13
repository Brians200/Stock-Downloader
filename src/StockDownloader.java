import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * This actually performs the stock downloading and parsing
 * @author brian
 *
 */
public class StockDownloader {
	
	/**
	 * This returns an ArrayList of the StockObjects
	 * for the date range and symbol given
	 * @param Symbol - Symbol of the company
	 * @param beginDate - The first date you wish to include "yyyy/MM/dd" format or "NOW"
	 * @param endDate - The last date you wish to include "yyyy/MM/dd" format or "NOW"
	 * @return an ArrayList of the StockObjects
	 */
	public static ArrayList<StockObject> Download(String Symbol, String BeginDate, String EndDate)
	{
		ArrayList<StockObject> retern = new ArrayList<StockObject>();
		
		DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy/MM/dd");
		
		DateTime beginDate;
		DateTime endDate;
		if(BeginDate.equals("NOW"))
		{
			beginDate= DateTime.now();
		}
		else
		{
			beginDate = DateTime.parse(BeginDate,dateTimeFormatter);
		}
		
		if(EndDate.equals("NOW"))
		{
			endDate = DateTime.now();
		}
		else
		{
			endDate = DateTime.parse(EndDate,dateTimeFormatter);
		}
		
		String nextLine;
		URL url = null;
		URLConnection urlConnection = null;
		InputStreamReader inputStream = null;
		BufferedReader bufferedReader = null;
		
		try{
			//http://greenido.wordpress.com/2009/12/22/yahoo-finance-hidden-api/
			//where the FROM date is: &a=01&b=10&c=2010
			//and the TO date is: &d=01&e=19&f=2010
			
			//yahoo api uses [0-11] for months
			url = new URL("http://ichart.yahoo.com/table.csv?s="+Symbol
																+"&a="+ Integer.toString(beginDate.getMonthOfYear()-1) 
																+"&b="+ Integer.toString(beginDate.getDayOfMonth())
																+"&c="+ Integer.toString(beginDate.getYear())
																+"&d=" + Integer.toString(endDate.getMonthOfYear()-1) 
																+ "&e=" + Integer.toString(endDate.getDayOfMonth()) 
																+ "&f=" + Integer.toString(endDate.getYear()) 
																+ "&g=d&ignore=.csv");
			urlConnection = url.openConnection();
			inputStream = new InputStreamReader(urlConnection.getInputStream());
			bufferedReader = new BufferedReader(inputStream);
			
			while(true)
			{
				nextLine = bufferedReader.readLine();
				if(nextLine !=null)
				{
					ArrayList<String> currentLine = split(nextLine);
					StockObject stockObject;
					try
					{
						stockObject = new StockObject(Symbol,currentLine.get(0), currentLine.get(1), currentLine.get(2), currentLine.get(3), currentLine.get(4), currentLine.get(5), currentLine.get(6));
					}catch (Exception e)
					{
						//The first line from the stream is 
						//Date,Open,High,Low,Close,Volume,Adj Close
						//which we don't care about
						continue;
					}
					retern.add(stockObject);
				}
				else
				{
					break;
				}
			}
		}
		catch(MalformedURLException e)
		{
			System.out.println("BAD URL");
			System.out.println(e);
		}
		catch(IOException e)
		{
			System.out.println("THERE IS NO DATA");
			System.out.println(e);
		}
		
		return retern;
	}
	
	
	public static ArrayList<StockObject> DownloadEntireHistory(String Symbol)
	{
		//Assuming that the entire History is jan 1, 1900  through NOW
		//Can easily change this later
		
		String beginDate ="1900/01/01";
		String endDate = "NOW";
		return StockDownloader.Download(Symbol,beginDate,endDate);
	}
	
	
	/**
	 * This will strip each value from the CSV line.
	 * "XOM","exxon, corporation", 234,21 
	 * will become
	 * XOM
	 * exxon, corporation
	 * 234
	 * 21
	 * 
	 * This allows names to have commas in them if that were to happen.
	 * This is just a pattern matching, so it will work for any number
	 * of values in the CSV line
	 * @param CSVline - line of values to be separated, comma separated
	 * @return ArrayList of Strings containing the different values
	 */
	private static ArrayList<String> split(String CSVline)
	{
		ArrayList<String> retern = new ArrayList<String>();
		final Pattern pattern = Pattern.compile("\"([^\"]*)\"|(?<=,|^)([^,]*)(?=,|$)");

		Matcher m = pattern.matcher(CSVline);
		
		while(m.find())
		{
			String toKeep = removeSpacesAndQuotes(m.group());
			if(!toKeep.equals(""))
			{
				retern.add(toKeep);
			}
		}
		return retern;
	}
	
	/**
	 * Cleans up the string.
	 * @param dirtyString - string to be cleaned
	 * @return dirtyString without quotes and spaces on the outsides
	 */
	private static String removeSpacesAndQuotes(String dirtyString) {
		String trimmedString = dirtyString.trim();
		String noQuotes = trimmedString.replace("\"", "");
		return noQuotes;
	}

}
