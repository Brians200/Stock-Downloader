import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CompanyDownloader {

	public static ArrayList<CompanyObject> DownloadCompanyTables()
	{
		ArrayList<CompanyObject> companies  = new ArrayList<CompanyObject>();
		
		String[] urls = {
				"http://www.nasdaq.com/screening/companies-by-name.aspx?letter=0&exchange=nasdaq&render=download",
				"http://www.nasdaq.com/screening/companies-by-name.aspx?letter=0&exchange=nyse&render=download",
				"http://www.nasdaq.com/screening/companies-by-name.aspx?letter=0&exchange=amex&render=download"
		};
		String[] exchanges = {"NASDAQ","NYSE","AMEX"};
		
		for(int i=0;i<3;i++)
		{
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
				url = new URL(urls[i]);
				urlConnection = url.openConnection();
				inputStream = new InputStreamReader(urlConnection.getInputStream());
				bufferedReader = new BufferedReader(inputStream);
				
				while(true)
				{
					nextLine = bufferedReader.readLine();
					if(nextLine !=null)
					{
						ArrayList<String> currentLine = split(nextLine);
						CompanyObject companyObject;
						try
						{
							currentLine.add(exchanges[i]);
							companyObject = new CompanyObject(currentLine);
						}catch (Exception e)
						{
							//The first line from the stream is garbage
							//which we don't care about
							continue;
						}
						if(!companyObject.symbol.equals("Symbol"))
						{
							companies.add(companyObject);
						}
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
		}
		
		return companies;
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
