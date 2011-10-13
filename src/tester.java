import java.util.ArrayList;

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
		ArrayList<StockObject> i = StockDownloader.Download("GOOG");
		for(StockObject a:i)
		{
//TODO: ADD TO DATABASE
			System.out.println(a);
		}
	}

}
