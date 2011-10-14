import java.util.ArrayList;


public class StockHistoryCreator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ArrayList<CompanyObject> companies = CompanyDownloader.DownloadCompanyTables();
		for(CompanyObject company:companies)
		{
			ArrayList<StockObject> stockHistory = StockDownloader.DownloadEntireHistory(company.symbol);
			//INSERT COMPANY
			for(StockObject stockObject:stockHistory)
			{
				System.out.println(stockObject);
				System.out.println(company);
				//INSERT HISTORY
			}
		}
	}

}
