	import java.sql.Connection;
	import java.sql.DriverManager;
	import java.sql.PreparedStatement;
	import java.sql.ResultSet;
	import java.sql.SQLException;
	import java.sql.Statement;
	import java.util.ArrayList;
	import java.util.Arrays;

public class ImageAnalyzer_hist {
		public static Connection conn;
		public static String experimentID;
		public static int localMin;
		
		/*define connection to database*/
		public static void connect()
		{
			try 
			{
	            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
	        } catch (Exception ex) {/* handle the error*/}
	        
	        try 
	        {
	            conn = DriverManager.getConnection("jdbc:mysql://localhost/test?serverTimezone=IST","root","Aa123456");
	            System.out.println("SQL connection succeed");        
	     	} catch (SQLException ex) 
	     	    {
	            System.out.println("SQLException: " + ex.getMessage());
	            }
	   	}
		
		/** define a set of parameters to be used	 */
		public static void setParameters(String ID)
		{
			experimentID=ID;
		}
		
			
		
		
		/**	find the threshold between "black" areas and the rest of image  */
		public static int getThreshold(Histogram hist)
		{
			int partitionNumber=hist.partitionNum;
			int partitionSize=255/partitionNumber;
			int defaultTh=0;
			int Threshold=0;
			try 
			{   
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT settings.defaultThreshold FROM settings WHERE experimentID= "+experimentID+";");
		 		rs.next();
		 		defaultTh=rs.getInt(1);
		 		rs.close();			
			} catch (SQLException e) {e.printStackTrace();}
			
			for (int i = 30/partitionSize; i < 100/partitionSize; i++) {
				if (hist.items[i+1]< hist.items[i])
					Threshold=i;
			}
			if (Threshold<defaultTh)
				return 0;
			else return Threshold*partitionSize;		
		}
		
		/**	find the first local minimum for the given histogram */
		public static boolean firstLocalMinimum(Histogram hist)
		{
			int partitionNumber=hist.partitionNum;
			int min=1;
						
			for (int i = 1; i < partitionNumber; i++) {
				if (hist.items[i+1]< hist.items[i])
					min=i;
			}
			localMin=min;	
			
			if (min==1) return false;
			else return true;
		}
	}		

		
		




