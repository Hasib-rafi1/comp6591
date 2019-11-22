package dbConnect;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties; 

public class DbConnectionManager {
	Connection conn = null;
	Properties prop = new Properties();
	
	public DbConnectionManager() throws SQLException {
		// TODO Auto-generated constructor stub
		
		String filename = "config.properties";

		InputStream input = null;
		input = DbConnectionManager.class.getClassLoader()
				.getResourceAsStream(filename);
		
		//Trying to establish connection with default settings
		//in case no file is found or input is null
		if (input == null) {
			System.out.println("Sorry, unable to find " + filename);
			System.exit(0);
			
		} else {
			try {
				prop.load(input);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			connect();
		}
		
	}
	
	
	private void connect() throws SQLException {
		try{  
			Class.forName("com.mysql.cj.jdbc.Driver");  
			conn=DriverManager.getConnection(  
			"jdbc:mysql://"+prop.getProperty("ipport")+"/"+ prop.getProperty("database"),prop.getProperty("user"),prop.getProperty("password"));  
			//here sonoo is database name, root is username and password  
			  
			}catch(Exception e){ System.out.println(e);}  
		}
	
	private Connection getConnection() {
		if (conn == null)
			try {
				connect();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return conn;
	}
	
	public ResultSet executeStatement(String sql) throws SQLException {
		Statement stmt=getConnection().createStatement();  
		ResultSet rs=stmt.executeQuery(sql);  
		return rs;
	}	
	
	public void executeStatementUpdate(String sql) throws SQLException {
		Statement stmt=getConnection().createStatement();  
		stmt.executeUpdate(sql);  
		
	}
	
	public void closeConnection() {
		if (conn != null) {
			try {
				conn.close();
				conn = null;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}
	  

