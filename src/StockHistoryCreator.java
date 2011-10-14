import java.util.ArrayList;
import java.sql.*;

public class StockHistoryCreator {

	/**
	 * @param args
	 */
	private static Connection connect = null;
	private static PreparedStatement preparedStatement = null;
	
	public static void main(String[] args) {
		
		
			try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			ArrayList<CompanyObject> companies = CompanyDownloader.DownloadCompanyTables();
			for(CompanyObject company:companies)
			{
				ArrayList<StockObject> stockHistory = StockDownloader.DownloadEntireHistory(company.symbol);
				//INSERT COMPANY
				try {
					preparedStatement = connect.prepareStatement("INSERT into TABLE Stock VALUES("+company.exchange+","+company.symbol+","+company.name+","+company.ipoYear+","+company.industry+","+company.marketCap+","+company.sector+")");
					preparedStatement.executeUpdate();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				for(StockObject stockObject:stockHistory)
				{
					System.out.println(stockObject);
					System.out.println(company);
					try {
						preparedStatement = connect.prepareStatement("INSERT into TABLE Stock VALUES("+company.exchange+","+stockObject.symbol+","+stockObject.date+","+stockObject.high+","+stockObject.low+","+stockObject.close+","+stockObject.volume+","+stockObject.adjClose+")");
						preparedStatement.executeUpdate();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
	}

}
