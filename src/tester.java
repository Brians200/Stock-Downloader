import java.util.ArrayList;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * This is used as a testing class for the stock downloader.
 * @author brian
 *
 */
public class tester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy/MM/dd");
		
		DateTime beginDate = DateTime.parse("2011/09/28",dateTimeFormatter);
		DateTime endDate = DateTime.now();
		ArrayList<StockObject> i = StockDownloader.Download("GOOG",beginDate,endDate);
		
		System.out.println("Downloaded " + i.size() + " lines");
		for(StockObject a:i)
		{
//TODO: ADD TO DATABASE
			System.out.println(a);
		}
	}

}
