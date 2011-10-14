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
				connect = DriverManager.getConnection("jdbc:mysql://mysql.cis.ksu.edu/bsweeney","bsweeney", "a1b2c3d4e5");
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			ArrayList<CompanyObject> companies = CompanyDownloader.DownloadCompanyTables();
			for(CompanyObject company:companies)
			{
				ArrayList<StockObject> stockHistory = StockDownloader.DownloadEntireHistory(company.symbol);
				//INSERT COMPANY
				String temp = "INSERT into Stock VALUES('"+company.exchange+"','"+company.symbol+"','"+company.name+"',"+company.ipoYear+",'"+company.industry+"',"+company.marketCap+",'"+company.sector+"')";
				try {
					preparedStatement = connect.prepareStatement(temp);
					preparedStatement.executeUpdate();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				for(StockObject stockObject:stockHistory)
				{
					//System.out.println(stockObject);
					//System.out.println(company);
					try {
						String temp2 = "INSERT into History VALUES('"+company.exchange+"','"+stockObject.symbol+"','"+stockObject.date+"',"+stockObject.high+","+stockObject.low+","+stockObject.close+","+stockObject.volume+","+stockObject.adjClose+")";
						preparedStatement = connect.prepareStatement(temp2);
						preparedStatement.executeUpdate();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
	}

}
