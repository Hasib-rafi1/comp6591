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
import java.util.Scanner;

import dbConnect.DbConnectionManager;

public class Start {
	public static DbConnectionManager dm =null;
	
	
	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		System.out.println("To Import data into Database enter I/i and to skip enter any other value");
		dm = new DbConnectionManager();
		DataImport di = new DataImport(dm);
		Scanner in = new Scanner(System.in);
		String s = in. nextLine();
		if(s.equals("I") ||s.equals("i")) {
			di.readFolder();
		}
		System.out.println("Select the symantics");
//		ResultSet result = dm.executeStatement("SELECT * FROM emp;");
//		while(result.next())  
//			System.out.println(result.getInt(1)+"  "+result.getString(2)); 
//		
//		dm.closeConnection();
	}
	
	

}
