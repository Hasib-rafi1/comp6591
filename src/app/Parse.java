package app;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.json.JSONArray;

import dbConnect.DbConnectionManager;

public class Parse {
	static ArrayList<JSONArray> resultsSet = new ArrayList<JSONArray>();
	public static JsonConverter jc=null;
	
	
	public static ArrayList<JSONArray> runBagSemantic(ArrayList<String> queryInputs, DbConnectionManager dm) throws SQLException {
		Bag bag = new Bag();
		for(int i=0; i<queryInputs.size(); i++){
			String Q = queryInputs.get(i);
			//System.out.println(Q);
			StringTokenizer st = new StringTokenizer(Q);
			String firstElemnt = st.nextToken();
			switch (firstElemnt) {
			case "select":
				ResultSet result1 = dm.executeStatement(Q);
				try {
					resultsSet.add(jc.convertToJSON(result1));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("Select" + resultsSet);
				break;
			case "project":
				if(Q.contains("from")){
					System.out.println("IT has from");
					String modifiedQ = "select " + Q.substring(8, Q.length());
					System.out.println(modifiedQ);
					ResultSet result2 = dm.executeStatement(modifiedQ);
					
					try {
						resultsSet.add(jc.convertToJSON(result2));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("Project" + resultsSet);
				}
				else{
					System.out.println("Project-from-before" + resultsSet);
					JSONArray r1 = resultsSet.get(resultsSet.size()-1);
					ArrayList<String> col = new ArrayList<String>();
					String modifiedQ = Q.substring(8, Q.length());
					StringTokenizer st1 = new StringTokenizer(modifiedQ, ",");
					while(st1.hasMoreTokens()){
						col.add(st1.nextToken());
					}
					
					JSONArray projectR = bag.projection(r1,col);
					resultsSet.add(projectR);
					System.out.println("Project-from-after" + resultsSet);
				}
				
				break;
			case "join":
				System.out.println("Join-before" + resultsSet);
				JSONArray r2 = resultsSet.get(resultsSet.size()-1);
				JSONArray r3 = resultsSet.get(resultsSet.size()-2);
				String joinOn = Q.substring(5, Q.length());
				JSONArray joinR =  bag.join(r2, r3, joinOn, joinOn);
				resultsSet.remove(resultsSet.size()-1);
				resultsSet.remove(resultsSet.size()-2);
				resultsSet.add(joinR);
				System.out.println("Join-after" + resultsSet);
				break;
			case "union":
				System.out.println("Union-before" + resultsSet);
				JSONArray r4 = resultsSet.get(resultsSet.size()-1);
				JSONArray r5 = resultsSet.get(resultsSet.size()-2);
				JSONArray unionR = bag.union(r4, r5);
				resultsSet.remove(resultsSet.size()-1);
				resultsSet.remove(resultsSet.size()-2);
				resultsSet.add(unionR);
				System.out.println("Union-after" + resultsSet);
				break;
			}
		}
		return resultsSet;
		
	}
}
