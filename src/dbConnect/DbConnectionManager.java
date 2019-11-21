package dbConnect;

import java.sql.*; 

public class DbConnectionManager {
	Connection conn = null;
	
	public DbConnectionManager() throws SQLException {
		// TODO Auto-generated constructor stub
		connect();
	}
	private void connect() throws SQLException {
		try{  
			Class.forName("com.mysql.cj.jdbc.Driver");  
			conn=DriverManager.getConnection(  
			"jdbc:mysql://localhost:3306/kbs_data","root","723400");  
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
	
	public void closeConnectio() {
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
	  

