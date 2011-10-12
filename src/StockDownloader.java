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
	
	public static ArrayList<ArrayList<String>> Download(String Symbol)
	{
		ArrayList<ArrayList<String>> retern = new ArrayList<ArrayList<String>>();
		int year = Calendar.getInstance().get(Calendar.YEAR);
		int month = Calendar.getInstance().get(Calendar.MONTH);
		int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
		
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
					retern.add(split(nextLine));
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
