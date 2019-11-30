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
	static ArrayList<JSONArray> resultsSet = new ArrayList<JSONArray>();
	
	
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
					runBagSemantic(queryInputs);
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
			
			System.out.println();
			for(int i=0; i< resultsSet.size(); i++){
				System.out.println(resultsSet.get(i));
			}
			System.out.println();
			
			dm.closeConnection();
		}
	}

	
	public static void runBagSemantic(ArrayList<String> queryInputs) throws SQLException {
		Bag bag = new Bag();
		for(int i=0; i<queryInputs.size(); i++){
			String Q = queryInputs.get(i);
			System.out.println("Query: " + Q);
			StringTokenizer st = new StringTokenizer(Q);
			String firstElemnt = st.nextToken();
			switch (firstElemnt) {
			case "select":
				System.out.println("Select-before" + resultsSet);
				ResultSet result1 = dm.executeStatement(Q);
				try {
					resultsSet.add(jc.convertToJSON(result1));
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				System.out.println("Select-after" + resultsSet);
				break;
			case "project":
				try {
				if(Q.contains("from")){
					System.out.println("Project-from-before" + resultsSet);
					String modifiedQ = "select " + Q.substring(8, Q.length());
					System.out.println(modifiedQ);
					ResultSet result2 = dm.executeStatement(modifiedQ);
					
					resultsSet.add(jc.convertToJSON(result2));
					
					System.out.println("Project-from-after" + resultsSet);
				}
				else{
					System.out.println("Project-before" + resultsSet);
					JSONArray r1 = resultsSet.get(resultsSet.size()-1);
					ArrayList<String> col = new ArrayList<String>();
					String modifiedQ = Q.substring(8, Q.length());
					StringTokenizer st1 = new StringTokenizer(modifiedQ, ",");
					while(st1.hasMoreTokens()){
						col.add(st1.nextToken());
					}
					
					JSONArray projectR = bag.projection(r1,col);
					resultsSet.remove(resultsSet.size()-1);
					resultsSet.add(projectR);
					System.out.println("Project-after" + resultsSet);
				}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case "join":
				System.out.println("Join-before" + resultsSet);
				JSONArray r2 = resultsSet.get(resultsSet.size()-1);
				JSONArray r3 = resultsSet.get(resultsSet.size()-2);
				String joinOn = Q.substring(5, Q.length());
				JSONArray joinR =  bag.join(r2, r3, joinOn, joinOn);
				
				resultsSet.remove(resultsSet.size()-1);
				resultsSet.remove(resultsSet.size()-1);
				resultsSet.add(joinR);
				System.out.println("Join-after" + resultsSet);
				break;
			case "union":
				System.out.println("Union-before" + resultsSet);
				JSONArray r4 = resultsSet.get(resultsSet.size()-1);
				JSONArray r5 = resultsSet.get(resultsSet.size()-2);
				JSONArray unionR = bag.union(r4, r5);
				resultsSet.remove(resultsSet.size()-1);
				resultsSet.remove(resultsSet.size()-1);
				resultsSet.add(unionR);
				
				System.out.println("Union-after" + resultsSet);
				break;
			}
		}	
	}
}
