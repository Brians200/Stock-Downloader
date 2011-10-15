import java.util.ArrayList;
import java.lang.ref.WeakReference;
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
			preparedStatement = connect.prepareStatement("TRUNCATE Stock");
			preparedStatement.executeUpdate();
			preparedStatement = connect.prepareStatement("TRUNCATE History");
			preparedStatement.executeUpdate();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int done =0;
		
		ArrayList<CompanyObject> companies = CompanyDownloader.DownloadCompanyTables();
		System.out.println(companies.size());
		
		
		for(;companies.size()>0;)
		{
			CompanyObject company = companies.get(0);
			ArrayList<StockObject> stockHistory = StockDownloader.DownloadEntireHistory(company.symbol);
			//INSERT COMPANY
			if(company.ipoYear.equals("n/a"))
			{
				company.ipoYear = "NULL";
			}
			System.out.println(done++);
			String temp = new String("INSERT into Stock VALUES('"+company.exchange+"','"+company.symbol+"',"+"?"+","+company.ipoYear+",'"+company.industry+"',"+company.marketCap+",'"+company.sector+"')");
			try {
				preparedStatement = connect.prepareStatement(temp);
				preparedStatement.setString(1,company.name);
				preparedStatement.executeUpdate();
				preparedStatement.close();
				temp=null;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for(;stockHistory.size()>0;)
			{
				//System.out.println(stockObject);
				//System.out.println(company);
				StockObject stockObject = stockHistory.get(0);
				try {
					String temp2 = new String("INSERT into History VALUES('"+company.exchange+"','"+stockObject.symbol+"','"+stockObject.date+"',"+stockObject.high+","+stockObject.low+","+stockObject.close+","+stockObject.volume+","+stockObject.adjClose+")");
					preparedStatement = connect.prepareStatement(temp2);
					preparedStatement.executeUpdate();
					preparedStatement.close();
					temp2=null;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				stockObject = null;
				stockHistory.remove(0);
			}
			companies.remove(0);
			company = null;	
		}
		
		System.out.println("DONE");
	}

}
