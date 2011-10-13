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
 * This actually performs the stock downloading
 * @author brian
 *
 */
public class StockDownloader {
	
	public static ArrayList<StockObject> Download(String Symbol)
	{
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
				//Rather than printing, we will actually have to add it to the appropriate spot in the database
				retern.add(toKeep);
			}
		}
		return retern;
	}
	
	private static String removeSpacesAndQuotes(String dirtyString) {
		String trimmedString = dirtyString.trim();
		String noQuotes = trimmedString.replace("\"", "");
		return noQuotes;
	}

}
