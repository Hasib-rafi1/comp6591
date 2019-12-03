package app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Uncertain {

	public JSONArray projection(JSONArray result, ArrayList<String> columns) {
		JSONArray finalOutPut = new JSONArray();
		if(result!=null && result.length()>0){
			for (int i = 0; i < result.length(); i++) {
				JSONObject first_objects =result.optJSONObject(i);
				Iterator<String> keys = columns.iterator();
				JSONObject jo = new JSONObject();
				while(keys.hasNext()) {
					String key = keys.next();
					try {
						jo.put(key, first_objects.get(key));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				try {
					jo.put("anotation", first_objects.get("anotation"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				finalOutPut.put(jo);
			}
		}
		return finalOutPut;
	}
	public JSONArray join(JSONArray first,JSONArray second, String c, String d) {
		JSONArray finalOutPut = new JSONArray();
		HashMap<String, Double> anotationVal1 = new HashMap<>();
		HashMap<String, Double> anotationVal2 = new HashMap<>();
		if(first!=null && first.length()>0){
			for (int i = 0; i < first.length(); i++) {
				JSONObject first_objects =first.optJSONObject(i);
					try {
						if(anotationVal1.containsKey(first_objects.get(c))&&anotationVal2.containsKey(first_objects.get(c))) {
							anotationVal1.put(first_objects.getString(c), anotationVal1.get(first_objects.getString(c))+first_objects.getDouble("anotation"));
							anotationVal2.put(first_objects.getString(c), anotationVal2.get(first_objects.getString(c))*first_objects.getDouble("anotation"));
						}else {
							anotationVal1.put(first_objects.get(c).toString(), first_objects.getDouble("anotation"));
							anotationVal2.put(first_objects.get(c).toString(), first_objects.getDouble("anotation"));
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		if(first!=null && first.length()>0){
			for (int i = 0; i < first.length(); i++) {
				JSONObject first_objects =first.optJSONObject(i);
				if(second!=null && second.length()>0){
					for (int j = 0; j < second.length(); j++) {
						JSONObject second_objects =second.optJSONObject(j);
						try {
							if(first_objects.get(c)==second_objects.get(d)||first_objects.get(c).equals(second_objects.get(d))){
								JSONObject jo = new JSONObject();
								Iterator<String> first_key= first_objects.keys();
								Iterator<String> second_key= second_objects.keys();
								while(first_key.hasNext()) {
									String currentDynamicKey = (String)first_key.next();
									if(!currentDynamicKey.equals("anotation") || !currentDynamicKey.equals(c) ) {
										jo.put(currentDynamicKey, first_objects.get(currentDynamicKey));
									}
								}

								while(second_key.hasNext()) {
									String currentDynamicKey = (String)second_key.next();
									if(!currentDynamicKey.equals("anotation") || !currentDynamicKey.equals(d) ) {
										jo.put(currentDynamicKey, second_objects.get(currentDynamicKey));
									}
								}
								
								jo.put(c, first_objects.get(c));
								Double val = (anotationVal1.get(first_objects.get(c).toString())-anotationVal2.get(first_objects.get(c).toString()))*second_objects.getDouble("anotation");
								if(val==0) {
									val= anotationVal1.get(first_objects.get(c).toString()) * second_objects.getDouble("anotation");
								}
								val = Math.round(val* 100.0) / 100.0;
								jo.put("anotation", val);
								finalOutPut.put(jo);

							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}


		return finalOutPut;
	}

	public JSONArray union(JSONArray result, JSONArray result2) {
		JSONArray finalOutPut = new JSONArray();
		JSONArray processedFinalOutPut = new JSONArray();
		if(result2!=null && result2.length()>0){
			for (int i = 0; i < result2.length(); i++) {
				JSONObject first_objects =result2.optJSONObject(i);
				result.put(first_objects);
			}
		}

		try {

			for (int i = 0; i < result.length(); i++) {
				JSONObject first_objects =result.optJSONObject(i); 
				JSONObject jo = new JSONObject();
				Double ano = first_objects.getDouble("anotation");
				for (int j = i+1; j < result.length(); j++) {


					JSONObject second_objects =result.optJSONObject(j);

					Iterator<String> keys= second_objects.keys();
					boolean same = true;
					while(keys.hasNext()) {
						String currentDynamicKey = (String)keys.next();

						if(!currentDynamicKey.equals("anotation") && !first_objects.get(currentDynamicKey).equals(second_objects.get(currentDynamicKey))) {
							//jo.put(currentDynamicKey, first_objects.get(currentDynamicKey));
							same = false;
						}

					}
					if(same == true) {
						if(ano<second_objects.getDouble("anotation")) {
							ano = second_objects.getDouble("anotation");
						}
						
					}

				}
				Iterator<String> keys= first_objects.keys();

				while(keys.hasNext()) {
					String currentDynamicKey = (String)keys.next();

					if(!currentDynamicKey.equals("anotation")) {
						jo.put(currentDynamicKey, first_objects.get(currentDynamicKey));

					}

				}
				Double val = Math.round(ano* 100.0) / 100.0;
				jo.put("anotation", val);
				finalOutPut.put(jo);
				//				if(finalOutPut!=null && finalOutPut.length()>0){
				//					for (int k = 0; k < finalOutPut.length(); k++) {
				//						JSONObject final_objects =finalOutPut.optJSONObject(k);
				//						Iterator<String> final_keys= final_objects.keys();
				//						boolean same = true;
				//						while(keys.hasNext()) {
				//							String currentDynamicKey = (String)keys.next();
				//							
				//							if(!currentDynamicKey.equals("anotation") && !final_objects.get(currentDynamicKey).equals(jo.get(currentDynamicKey)) ) {
				//								//jo.put(currentDynamicKey, first_objects.get(currentDynamicKey));
				//								same = false;
				//							}
				//
				//						}
				//						System.out.println(same);
				//						if(same==true) {
				//							finalOutPut.put(jo);
				//						}
				//					}
				//				}else {
				//					finalOutPut.put(jo);
				//				}

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		processedFinalOutPut = removeDuplicates(finalOutPut);
		
		return processedFinalOutPut;
	}

	public JSONArray removeDuplicates(JSONArray result) {
		ArrayList <Integer> indices = new ArrayList();
		JSONArray finalOutput = new JSONArray();
		try {
			for (int i = 0; i < result.length(); i++) {
				JSONObject first_objects =result.optJSONObject(i); 
				JSONObject jo = new JSONObject();
				Double ano = first_objects.getDouble("anotation");
				for (int j = i+1; j < result.length(); j++) {


					JSONObject second_objects =result.optJSONObject(j);

					Iterator<String> keys= second_objects.keys();
					boolean same = true;
					while(keys.hasNext()) {
						String currentDynamicKey = (String)keys.next();

						if(!currentDynamicKey.equals("anotation") && !first_objects.get(currentDynamicKey).equals(second_objects.get(currentDynamicKey))) {
							//jo.put(currentDynamicKey, first_objects.get(currentDynamicKey));
							same = false;
						}

					}
					if(same == true) {
						indices.add(j);
					}

				}
			}
			for (int i = 0; i < result.length(); i++) {
				JSONObject first_objects =result.optJSONObject(i); 
				if(!indices.contains(i)) {
					finalOutput.put(first_objects);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return finalOutput;
	}

}
