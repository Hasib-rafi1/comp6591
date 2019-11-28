package app;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.JSONArray;

import dbConnect.DbConnectionManager;

public class Start {
	public static DbConnectionManager dm =null;
	public static JsonConverter jc=null;
	
	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		System.out.println("To Import data without anotation genarator into Database enter I/i and A/a for with anotation genarator.");
		dm = new DbConnectionManager();
		jc = new JsonConverter();
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
			ResultSet result1 = dm.executeStatement("Select A,B,anotation from r;");
			ResultSet result2 = dm.executeStatement("Select B,C,anotation from r;");
			ResultSet result3 = dm.executeStatement("Select A,C,anotation from r;");
			ResultSet result4 = dm.executeStatement("Select B,C,anotation from r;");
			try {
				Bag bag = new Bag();
				JSONArray joing1 =  bag.join(jc.convertToJSON(result1), jc.convertToJSON(result2), "B", "B");
				JSONArray joing2 =  bag.join(jc.convertToJSON(result3), jc.convertToJSON(result4), "C", "C");
				ArrayList<String> col = new ArrayList<>();
				col.add("A");
				col.add("C");
				JSONArray project1 = bag.projection(joing1,col);
				JSONArray project2 = bag.projection(joing2,col);
				
				JSONArray union = bag.union(project1, project2);
				
				System.out.println(union);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		
//		ResultSet result = dm.executeStatement("SELECT * FROM emp;");
//		while(result.next())  
//			System.out.println(result.getInt(1)+"  "+result.getString(2)); 
//		
//		dm.closeConnection();
	}
	
	

}
