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
		System.out.println("To Import data without anotation genarator into Database enter I/i and A/a for with anotation genarator.");
		dm = new DbConnectionManager();
		DataImport di = new DataImport(dm);
		Scanner in = new Scanner(System.in);
		String s = in. nextLine();
		System.out.println("Select the symantics: (1) the multiplicity of t (this means \"R\" is a bag), (2) the probability of t, (3) the certainty of t,, (4) provenance polynomial, or (5) 0 or 1 (for representing the standard case)");
		int anotation = in. nextInt();
		if(s.equals("I") ||s.equals("i") || s.equals("A") ||s.equals("a")) {
			di.readFolder(anotation,s);
		}
		while(true) {
			System.out.println("Write query or for quit write q");
			String query = in. next();
			if(query.equals("Q")||query.equals("q")) {
				boolean check = di.dropAll();
				if(check) {
					dm.closeConnection();
					break;
				}
			}

		}
		
//		ResultSet result = dm.executeStatement("SELECT * FROM emp;");
//		while(result.next())  
//			System.out.println(result.getInt(1)+"  "+result.getString(2)); 
//		
//		dm.closeConnection();
	}
	
	

}
