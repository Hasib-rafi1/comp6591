package app;

import java.sql.ResultSet;
import java.sql.SQLException;

import dbConnect.DbConnectionManager;

public class Start {

	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		System.out.print("a");
		DbConnectionManager dm = new DbConnectionManager();
		ResultSet result = dm.executeStatement("SELECT * FROM emp;");
		while(result.next())  
			System.out.println(result.getInt(1)+"  "+result.getString(2)); 
		
		dm.closeConnectio();
	}

}
