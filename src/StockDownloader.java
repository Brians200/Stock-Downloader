import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	 * @return an ArrayList of the StockObjects
	 */
	public static ArrayList<StockObject> Download(String Symbol)
	{
//TODO: add a date range to params
		ArrayList<StockObject> retern = new ArrayList<StockObject>();
		int year = Calendar.getInstance().get(Calendar.YEAR);
		int month = Calendar.getInstance().get(Calendar.MONTH);
		int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		
		String nextLine;
		URL url = null;
		URLConnection urlConnection = null;
		InputStreamReader inputStream = null;
		BufferedReader bufferedReader = null;
		
		try{
			url = new URL("http://ichart.yahoo.com/table.csv?s=GOOG&a=0&b=1&c=2000&d=" + Integer.toString(month) + "&e=" + Integer.toString(day) + "&f=" + Integer.toString(year) + "&g=d&ignore=.csv");
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
						stockObject = new StockObject(currentLine.get(0), currentLine.get(1), currentLine.get(2), currentLine.get(3), currentLine.get(4), currentLine.get(5), currentLine.get(6));
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
			System.out.println("CAN'T READ");
			System.out.println(e);
		}
		
		return retern;
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
