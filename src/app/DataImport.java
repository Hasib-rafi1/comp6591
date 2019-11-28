package app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;

import dbConnect.DbConnectionManager;

public class DataImport {
	public  DbConnectionManager dm =null;
	public int anotation= 0;
	public String importType;
	public Random ran = new Random();
	public ArrayList<String> tables= new ArrayList();
	public DataImport(DbConnectionManager dms) {
		dm = dms;
	}
	
	public  void readFolder(int number, String a) {
		anotation = number;
		importType = a;
		final File folder = new File("dbFiles/");
		System.out.println("Reading Folder");
		for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isFile() && fileEntry.getName().endsWith(".txt")) {
	        	String table_name = fileEntry.getName().replaceAll(".txt", "").replace('.', '_');
	        	tables.add(table_name);
	        	//System.out.println(table_name);
	        	//System.out.println(fileEntry.getName());
	        	createTable(table_name,fileEntry.getName());
	        	insertValueInTable(table_name,fileEntry.getName());
	        }
	    }
	}
	
	private  void insertValueInTable(String table_name, String table_path) {
		FileInputStream fstream;
		
		String insertQuery = "INSERT INTO "+table_name+" (";
		
		int count =0;
		try {
			fstream = new FileInputStream("dbFiles/"+table_path);
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			
		    String line;
		    String randomString = "abcdefghijklmnopqrstuvwxyz";
		    char ranchar = randomString.charAt(ran.nextInt(26));
		    int row =0;
		    while ((line = br.readLine()) != null) {
		       // process the line.
		    	if(count==0) {
		    		if(importType.equals("A") ||importType.equals("a")) {
		    			insertQuery = insertQuery+ line+",anotation) VALUES (";
		    		}else {
		    			insertQuery = insertQuery+ line+") VALUES (";
		    		}
		    		
		    		count++;
		    	}else {
		    		
		    		
		    		String finalQuery="";
		    		if(importType.equals("A") ||importType.equals("a")) {
		    			if(anotation==1) {
		    				int x = ran.nextInt(9)+1;
		    				finalQuery = insertQuery+line+","+x+");";
						}else if (anotation==2) {
							float x = ran.nextFloat();
							finalQuery = insertQuery+line+","+x+");";
						}else if (anotation==3) {
							float x = ran.nextFloat();
							finalQuery = insertQuery+line+","+x+");";
						}else if (anotation==4) {
							row++;
							finalQuery = insertQuery+line+",'"+ranchar+row+"');";
						}else if (anotation==5) {
							finalQuery = insertQuery+line+","+1+");";
						}else {
							finalQuery = insertQuery+line+");";
						}
		    			
		    		}else {
		    			finalQuery = insertQuery+line+");";
		    		}
		    		
		    		//System.out.println(finalQuery);
		    		try {
		    			
						dm.executeStatementUpdate(finalQuery);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    	}
		    }
		    System.out.println(table_name+" loaded with data");
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private  void createTable(String table_name, String table_path) {
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
						queryString = queryString+","+key_name+" TEXT(65535)";
					}
				}
				check_first++;}else {
					System.out.println(table_name+" Value Doesn't have any col");
					System.out.println(strLine1_arr.size());
					System.out.println(strLine2_arr.size());
				}
			}
			if(importType.equals("I") ||importType.equals("i")) {
				queryString = queryString +");";
				
			}else if(importType.equals("A") ||importType.equals("a")){
				if(anotation==4) {
					queryString = queryString +",anotation VARCHAR(30));";
				}else if (anotation==2||anotation==3) {
					queryString = queryString +",anotation DOUBLE(10,2));";
				}
				else {
					queryString = queryString +",anotation INT(10));";
				}
				
			}else {
				queryString = queryString +");";
			}
			try {
				dm.executeStatementUpdate(queryString);
				System.out.println(table_name+" is Created");
			} catch (SQLException e) {
				System.out.println(queryString);
				// TODO Auto-generated catch block
				//e.printStackTrace();
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
	
	public boolean dropAll() {
		for (String table : tables) {
			try {
				dm.executeStatementUpdate("DROP TABLE IF EXISTS " +table+";");
				System.out.print(table+" Droped");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.print(table+" can't be Droped");
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
}
