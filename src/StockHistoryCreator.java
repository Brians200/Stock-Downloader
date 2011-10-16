/**
 * This class is serves two functions.  
 * If you want to populate a new database, it will 
 * download a list of a companies and then their "entire" histories.
 * The word entire is determined by the date set in the DownloadEntireHistory method of
 * StockDownloader.
 * !!!!!WARNING!!!!! DownloadCompaniesAndHistories() will delete all data in the Stock and History tables
 * 
 * 
 * UpdateToCurrentData is used to fill in the missing data as time goes on.
 * So if you create a table and wait a week, it will pull the data from the last week
 * and add it to the tables.
 * @author brian
 *
 */
import java.util.ArrayList;
import java.sql.*;

public class StockHistoryCreator {

	/**
	 * @param args
	 */
	private static Connection connect = null;
	private static PreparedStatement preparedStatement = null;
	private static ResultSet resultSet = null;
	/**
	 * This is a temporary starter point. Will be removed when I add it to the
	 * website.
	 */
	public static void main(String[] args) {
//TODO: Remove this method
		UpdateToCurrentData();
	}
	
	/**
	 *UpdateToCurrentData is used to fill in the missing data as time goes on.
	 * So if you create a table and wait a week, it will pull the data from the last week
	 * and add it to the tables.
	 */
	public static void UpdateToCurrentData()
	{
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
		
		try {
			int done=0;
			preparedStatement = connect.prepareStatement("Select max(date) from History");
			resultSet=preparedStatement.executeQuery();
			resultSet.next();
			String previousDate = resultSet.getString(1);
			previousDate = previousDate.replace('-', '/');
			preparedStatement.close();
			resultSet.close();
			
			preparedStatement = connect.prepareStatement("Select symbol,ename from Stock");
			resultSet=preparedStatement.executeQuery();
			
			PreparedStatement prep2 = null;
			while(resultSet.next())
			{
				System.out.println(++done);
				ArrayList<StockObject> stockhistory = StockDownloader.UpdateData(resultSet.getString("symbol"), previousDate);
				for(StockObject stockObject: stockhistory)
				{
					String sqlStatement = "INSERT into History VALUES('"+resultSet.getString("ename")+"','"+stockObject.symbol+"','"+stockObject.date+"',"+stockObject.high+","+stockObject.low+","+stockObject.close+","+stockObject.volume+","+stockObject.adjClose+")";
					prep2 = connect.prepareStatement(sqlStatement);
					prep2.executeUpdate();
					prep2.close();
				}
				
			}
			System.out.println("Downloaded Data from: " + previousDate);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	
	}
	
	/**
	 *If you want to populate a new database, it will 
	 * download a list of a companies and then their "entire" histories.
	 * The word entire is determined by the date set in the DownloadEntireHistory method of
	 * StockDownloader.
	 * !!!!!WARNING!!!!! DownloadCompaniesAndHistories() will delete all data in the Stock and History tables
	 */
	public static void DownloadCompaniesAndHistories()
	{
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection("jdbc:mysql://mysql.cis.ksu.edu/bsweeney","bsweeney", "a1b2c3d4e5");
			preparedStatement = connect.prepareStatement("TRUNCATE Stock");
			preparedStatement.executeUpdate();
			preparedStatement.close();
			preparedStatement = connect.prepareStatement("TRUNCATE History");
			preparedStatement.executeUpdate();
			preparedStatement.close();
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
