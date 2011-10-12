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
		// TODO Auto-generated method stub
		ArrayList<ArrayList<String>> i = StockDownloader.Download("GOOG");
		for(ArrayList<String> a:i)
		{
			System.out.println(a);
		}
	}

}
