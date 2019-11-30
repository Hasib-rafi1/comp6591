package app;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

import org.json.JSONArray;

import dbConnect.DbConnectionManager;

public class Start {
	public static DbConnectionManager dm =null;
	public static JsonConverter jc=null;
	static ArrayList<ResultSet> resultsSet = new ArrayList<ResultSet>();
	
	
	public static void main(String[] args) throws SQLException {
		
		Scanner in = new Scanner(System.in);
		dm = new DbConnectionManager();
		jc = new JsonConverter();
		DataImport di = new DataImport(dm);
		
		System.out.println("Create an Anotated Database: Enter I/i \nCreate a Database with automatic anotation generator: Enter A/a \n");		
		String selectedDB = "";
		while(true){
			selectedDB = in. nextLine();
			if(selectedDB.equalsIgnoreCase("I") || selectedDB.equalsIgnoreCase("A")){
				break;
			}
			else{
				System.out.println("Please only choose I/i or A/a");
			}
		}
		
		System.out.println("Select one of the semantics:\n(1) Bag Semantic\n(2) Probability Semantic\n(3) Certainty Semantic\n(4) Provenance Polynomial Semantic\n(5) Standard Semantic (0 or 1)\n");
		int semantic = -1;
		while(true){
			semantic = Integer.parseInt(in. nextLine());
			if(1 <= semantic && semantic <= 5){
				break;
			}
			else{
				System.out.println("Please only choose a number between 1-5");
			}
		}
	
		di.readFolder(semantic,selectedDB);
		
		while(true) {
			System.out.println("Enter your query\n-Enter <DONE> when it has been finished\n-You wanna quit? Enter Q/q");
			String query1 = in. nextLine();
			if(query1.equalsIgnoreCase("Q")) {
				boolean check = di.dropAll();
				if(check) {
					dm.closeConnection();
					break;
				}
			}
			else{
				ArrayList<String> queryInputs = new ArrayList<String>();
				queryInputs.add(query1);
				while(true){
					String query = in. nextLine();
					if(query.equalsIgnoreCase("DONE")){
						break;
					}
					else{
						queryInputs.add(query);
					}
				}
				System.out.println(queryInputs);
				switch (semantic) {
				case 1:
					Bag bag = new Bag();
					runSemantic(queryInputs, bag);
					break;
				case 2:
				
					
					break;
				case 3:
					
					break;
				case 4:
					
					break;
				case 5:
					
					break;
				}
			}
			
			dm.closeConnection();
			
			/*
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
				e.printStackTrace();
			}
		*/
		}
		
//		ResultSet result = dm.executeStatement("SELECT * FROM emp;");
//		while(result.next())  
//			System.out.println(result.getInt(1)+"  "+result.getString(2)); 
//		
//		dm.closeConnection();
	}

	private static void runSemantic(ArrayList<String> queryInputs, Object semantic) throws SQLException {
		
		for(int i=0; i<queryInputs.size(); i++){
			String Q = queryInputs.get(i);
			//System.out.println(Q);
			StringTokenizer st = new StringTokenizer(Q);
			String firstElemnt = st.nextToken();
			switch (firstElemnt) {
			case "select":
				ResultSet result1 = dm.executeStatement(Q);
				resultsSet.add(result1);
				break;
			case "project":
				if(Q.contains("from")){
					String modifiedQ = "select " + Q.substring(8, Q.length());
					ResultSet result2 = dm.executeStatement(modifiedQ);
					resultsSet.add(result2);
				}
				else{
					ResultSet r1 = resultsSet.get(resultsSet.size()-1);
					ArrayList<String> col = new ArrayList<String>();
					String modifiedQ = Q.substring(8, Q.length());
					StringTokenizer st1 = new StringTokenizer(modifiedQ, ",");
					while(st1.hasMoreTokens()){
						col.add(st1.nextToken());
					}
					//ResultSet projectR = semantic.projection(r1,col);
				}
				
				break;
			case "join":
				ResultSet r2 = resultsSet.get(resultsSet.size()-1);
				ResultSet r3 = resultsSet.get(resultsSet.size()-2);
				String joinOn = Q.substring(5, Q.length());
				//resultsSet joinR =  semantic.join(jc.convertToJSON(r2), jc.convertToJSON(r3), joinOn, joinOn);
				resultsSet.remove(resultsSet.size()-1);
				resultsSet.remove(resultsSet.size()-2);
				//resultsSet.add(joinR);
				break;
			case "union":
				ResultSet r4 = resultsSet.get(resultsSet.size()-1);
				ResultSet r5 = resultsSet.get(resultsSet.size()-2);
				//ResultSet unionR = semantic.union(r4, r5);
				resultsSet.remove(resultsSet.size()-1);
				resultsSet.remove(resultsSet.size()-2);
				//resultsSet.add(unionR);
				break;
			}
		}
		
		
	}
	
	

}
