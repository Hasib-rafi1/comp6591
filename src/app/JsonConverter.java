package app;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mysql.cj.jdbc.result.ResultSetMetaData;

import java.awt.List;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
/**
 * Utility for converting ResultSets into some Output formats
 * @author marlonlom
 */
public class JsonConverter {
	/**
	 * Convert a result set into a JSON Array
	 * @param resultSet
	 * @return a JSONArray
	 * @throws Exception
	 */

	public JSONArray convertToJSON(ResultSet resultSet)
			throws Exception {
		JSONArray jsonArray = new JSONArray();
		while (resultSet.next()) {
			int total_rows = resultSet.getMetaData().getColumnCount();
			JSONObject obj = new JSONObject();
			for (int i = 0; i < total_rows; i++) {
				obj.put(resultSet.getMetaData().getColumnLabel(i + 1), resultSet.getObject(i + 1));
			}
			jsonArray.put(obj);
		}
		return jsonArray;
	}
	
	private ArrayList<HashMap<String, Object>> resultSetToList(ResultSet rs) throws SQLException {
	    ResultSetMetaData md = (ResultSetMetaData) rs.getMetaData();
	    int columns = md.getColumnCount();
	    ArrayList<HashMap<String, Object>> rows = new ArrayList<HashMap<String, Object>>();
	    while (rs.next()){
	        HashMap<String, Object> row = new HashMap<String, Object>(columns);
	        for(int i = 1; i <= columns; ++i){
	            row.put(md.getColumnName(i), rs.getObject(i));
	        }
	        rows.add(row);
	    }
	    return rows;
	}

}