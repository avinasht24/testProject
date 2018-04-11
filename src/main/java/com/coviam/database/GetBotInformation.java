package com.coviam.database;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
/**
 * Created by avinash.t
 */
public class GetBotInformation {
	
    public static Connection apiconn =null;
	public String getApiInformation(String environment, String source_type, String bot_keyword)
	{
		connect();
		String sql = "select url from bot_config where environment='"+environment+"' and source_type='"+source_type+"' and bot_keyword='"+bot_keyword+"' ";
		String outputUrl=null;

        try {
            Statement  stmt= apiconn.createStatement();
            System.out.println("Statement:"+stmt);

            System.out.println("SQL:"+sql);

            ResultSet rs    = stmt.executeQuery(sql);

            
//            if(rs.wasNull())
//            {
//            	outputUrl="No Results found for the given API details. Please check if configuration details are added.";
//            }
            // loop through the result set
            while (rs.next()) {
	                //System.out.println(rs.getString("url"));
	                outputUrl=rs.getString("url");
	                String temp1="<a href=\"";
	                String temp2="\">Click Here for "+bot_keyword+" API URL<a/>";
	                outputUrl=temp1.concat(outputUrl).concat(temp2);
	                System.out.println(outputUrl);
            }
            rs.close();
            stmt.close();
            apiconn.close();
            
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            outputUrl=e.getMessage();
        }
		
		
		return outputUrl;
	}
	
	public String getCenterInformation(String environment, String source_type, String bot_keyword)
	{
		connect();
		String sql = "select url from bot_config where environment='"+environment+"' and source_type='"+source_type+"' and bot_keyword='"+bot_keyword+"' ";
		String outputUrl=null;

        try {
            Statement  stmt= apiconn.createStatement();
            System.out.println("Statement:"+stmt);

            System.out.println("SQL:"+sql);

            ResultSet rs    = stmt.executeQuery(sql);

            
//            if(rs.wasNull())
//            {
//            	outputUrl="No Results found for the given API details. Please check if configuration details are added.";
//            }
            // loop through the result set
            while (rs.next()) {
	                //System.out.println(rs.getString("url"));
	                outputUrl=rs.getString("url");
	                String temp1="<a href=\"";
	                String temp2="\">Click Here for "+bot_keyword+" Center URL<a/>";
	                outputUrl=temp1.concat(outputUrl).concat(temp2);
	                System.out.println(outputUrl);
            }
            rs.close();
            stmt.close();
            apiconn.close();
            
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            outputUrl=e.getMessage();
        }
		
		
		return outputUrl;
	}
	
	public String getProductInformation(String environment, String source_type, String bot_keyword)
	{
		connect();
		String sql = "select url from bot_config where environment='"+environment+"' and source_type='"+source_type+"' and bot_keyword='"+bot_keyword+"' ";
		String outputUrl=null;

        try {
            Statement  stmt= apiconn.createStatement();
            System.out.println("Statement:"+stmt);

            System.out.println("SQL:"+sql);

            ResultSet rs    = stmt.executeQuery(sql);

            
//            if(rs.wasNull())
//            {
//            	outputUrl="No Results found for the given API details. Please check if configuration details are added.";
//            }
            // loop through the result set
            while (rs.next()) {
	                //System.out.println(rs.getString("url"));
	                outputUrl=rs.getString("url");
	                String temp1="<a href=\"";
	                String temp2="\">Click Here for "+bot_keyword+" Product URL<a/>";
	                outputUrl=temp1.concat(outputUrl).concat(temp2);
	                System.out.println(outputUrl);
            }
            rs.close();
            stmt.close();
            apiconn.close();
            
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            outputUrl=e.getMessage();
        }
		
		
		return outputUrl;
	}
	
	public String getUserInformation(String environment, String source_type, String bot_keyword)
	{
		connect();
		String sql = "select url from bot_config where environment='"+environment+"' and source_type='"+source_type+"' and bot_keyword='"+bot_keyword+"' ";
		String outputUrl=null;

        try {
            Statement  stmt= apiconn.createStatement();
            System.out.println("Statement:"+stmt);

            System.out.println("SQL:"+sql);

            ResultSet rs    = stmt.executeQuery(sql);

            
//            if(rs.wasNull())
//            {
//            	outputUrl="No Results found for the given API details. Please check if configuration details are added.";
//            }
            // loop through the result set
            while (rs.next()) {
	                //System.out.println(rs.getString("url"));
	                outputUrl=rs.getString("url");
//	                String temp1="<a href=\"";
	                String temp2="Username & Password details for "+bot_keyword+" is :";
	                outputUrl=temp2.concat(outputUrl);
	                System.out.println(outputUrl);
            }
            rs.close();
            stmt.close();
            apiconn.close();
            
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            outputUrl=e.getMessage();
        }
		
		
		return outputUrl;
	}
	
	public String getPromoInformation(String environment, String source_type, String bot_keyword)
	{
		connect();
		String sql = "select url from bot_config where environment='"+environment+"' and source_type='"+source_type+"' and bot_keyword='"+bot_keyword+"' ";
		String outputUrl=null;

        try {
            Statement  stmt= apiconn.createStatement();
            System.out.println("Statement:"+stmt);

            System.out.println("SQL:"+sql);

            ResultSet rs    = stmt.executeQuery(sql);

            
//            if(rs.wasNull())
//            {
//            	outputUrl="No Results found for the given API details. Please check if configuration details are added.";
//            }
            // loop through the result set
            while (rs.next()) {
	                //System.out.println(rs.getString("url"));
	                outputUrl=rs.getString("url");
//	                String temp1="<a href=\"";
	                String temp2="Public Voucher Code :";
	                outputUrl=temp2.concat(outputUrl);
	                System.out.println(outputUrl);
            }
            rs.close();
            stmt.close();
            apiconn.close();
            
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            outputUrl=e.getMessage();
        }
		
		
		return outputUrl;
	}
	
	 public  void connect() {
	        try {
	            // db parameters
	            try {
	                Class.forName("org.sqlite.JDBC");
	            } catch (ClassNotFoundException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }
	            String url = "jdbc:sqlite:/Users/avinash.t/Avinash/botdb/qabot.db";
	            // create a connection to the database
	            apiconn= DriverManager.getConnection(url);
	            System.out.println("Connection: " + apiconn);
	            if (apiconn!= null) {
	                DatabaseMetaData meta = apiconn.getMetaData();
	                System.out.println("The driver name is " + meta.getDriverName());
	            }
	            System.out.println("Connection to SQLite has been established.");
	        } catch (SQLException e) {
	            System.out.println(e.getMessage());
	        }
	    }
}
