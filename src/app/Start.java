package app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

import dbConnect.DbConnectionManager;

public class Start {
	public static DbConnectionManager dm =null;
	
	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		System.out.println("a");
		dm = new DbConnectionManager();
		readFolder();
//		ResultSet result = dm.executeStatement("SELECT * FROM emp;");
//		while(result.next())  
//			System.out.println(result.getInt(1)+"  "+result.getString(2)); 
//		
//		dm.closeConnection();
	}
	
	private static void readFolder() {
		final File folder = new File("dbFiles/");
		for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isFile() && fileEntry.getName().endsWith(".txt")) {
	        	String table_name = fileEntry.getName().replaceAll(".txt", "").replace('.', '_');
	        	//System.out.println(table_name);
	        	//System.out.println(fileEntry.getName());
	        	createTable(table_name,fileEntry.getName());
	        }
	    }
	}
	
	private static void createTable(String table_name, String table_path) {
		FileInputStream fstream;
		try {
			fstream = new FileInputStream("dbFiles/"+table_path);
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

			String strLine1;
			String strLine2;
			ArrayList<String>  strLine1_arr = new ArrayList<String>();
			ArrayList<String>  strLine2_arr = new ArrayList<String>();
			String queryString = "CREATE TABLE "+table_name+"(";

			//Read File Line By Line
			try {
				if((strLine1 = br.readLine()) != null)   {
				  // Print the content on the console
					Collections.addAll(strLine1_arr, strLine1.split(", "));
				  
				}
				if((strLine2 = br.readLine()) != null)   {
					  // Print the content on the console
					Collections.addAll(strLine2_arr, strLine2.split(","));
				}
				fstream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Iterator<String> first = strLine1_arr.iterator();
			Iterator<String> second = strLine2_arr.iterator();
			int check_first = 0;
			while(second.hasNext()) {
				String type = second.next();
				if(strLine1_arr.size()==strLine2_arr.size()) {
				
				String key_name = first.next();
				if(checkInt(type)) {
					if(check_first==0) {
						queryString = queryString+key_name+" INT(10)";
					}else {
						queryString = queryString+","+key_name+" INT(10)";
					}
				}else if(checkFloat(type)) {
					if(check_first==0) {
						queryString = queryString+key_name+" DOUBLE(10,2)";
					}else {
						queryString = queryString+","+key_name+" DOUBLE(10,2)";
					}
				}else {
					if(check_first==0) {
						queryString = queryString+key_name+" VARCHAR(30)";
					}else {
						queryString = queryString+","+key_name+" VARCHAR(30)";
					}
				}
				check_first++;}else {
					System.out.println(table_name+" Value Doesn't have any col");
					System.out.println(strLine1_arr.size());
					System.out.println(strLine2_arr.size());
				}
			}
			queryString = queryString +");";
			try {
				dm.executeStatementUpdate(queryString);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static boolean checkInt(String a) {
		try

		{
			Integer.parseInt(a); //use your variable or object in place of obj
			return true;
		}

		catch (NumberFormatException e)
		{
			return false;
		}
	}
	
	public static boolean checkFloat(String a) {
		try

		{
			Float.parseFloat(a); //use your variable or object in place of obj
			return true;
		}

		catch (NumberFormatException e)
		{
			return false;
		}
	}

}
