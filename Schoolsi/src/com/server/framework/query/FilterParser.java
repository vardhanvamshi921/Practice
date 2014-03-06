package com.server.framework.query;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.server.framework.query.Filter.Operator;


public class FilterParser {
	static JSONArray jFilterArray = new JSONArray();
	static JSONObject orginalFilters = new JSONObject();
	static JSONObject newFilter = new JSONObject();
	
	static {
		
		try {

			JSONObject f1 = new JSONObject();
			f1.put("key", "key");
			f1.put("operator", "OR");
			f1.put("value", "value1");
			f1.put("__type__", "simple");
			
			JSONObject f2 = new JSONObject();
			f2.put("key", "key");
			f2.put("operator", "OR");
			f2.put("value", "value1");
			f2.put("__type__", "simple");

			JSONObject f3 = new JSONObject();
			f3.put("key", "key");
			f3.put("operator", "OR");
			f3.put("value", "value1");
			f3.put("__type__", "simple");

			JSONObject f4 = new JSONObject();
			f4.put("key", "key");
			f4.put("operator", "OR");
			f4.put("value", "value1");
			f4.put("__type__", "simple");
			
			JSONObject f5 = new JSONObject();
			f5.put("key", "key");
			f5.put("operator", "OR");
			f5.put("value", "value1");
			f5.put("__type__", "simple");
			
			jFilterArray.put(f1);
			jFilterArray.put(f2);
			jFilterArray.put(f3);
			jFilterArray.put(f4);
			
			orginalFilters.put("compositeoperator", "AND");
			orginalFilters.put("filters", jFilterArray);
			orginalFilters.put("__type__", "composite");
			
			JSONArray newAfilter = new JSONArray();
			newAfilter.put(f5);
			newAfilter.put(orginalFilters);
			
			newFilter.put("compositeoperator", "OR");
			newFilter.put("filters", newAfilter);
			newFilter.put("__type__", "composite");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public static Filter getFilter(String jFilterStr) throws JSONException {
		if (jFilterStr == null || jFilterStr.equals("") || jFilterStr.equals("null")) {
			return null;
		}
		return getFilter(new JSONObject(jFilterStr));
	}
	
	public static Filter getFilter(JSONObject jFilter) throws JSONException {
		
		Filter f = null;
		String type = jFilter.optString("filtertype");
		if (type.equals("simple")) {
			f = getSimpleFilter(jFilter); 
		} else {
			Operator cop = Filter.getStringAsFilterOperator(jFilter.optString("compositeoperator")); 
			JSONArray filters = jFilter.optJSONArray("filters");
			List<Filter> subFilters = new ArrayList<Filter>();
			for (int i=0; i<filters.length(); i++) {
				subFilters.add(getFilter(filters.getJSONObject(i)));
			}
			if (subFilters.size() >1) {
				f = new CompositeFilter(cop, subFilters);
			} else {
				f = subFilters.get(0);
			}
		}
		return f;
	}
	
		
	public static Filter getSimpleFilter(JSONObject jFilter) throws JSONException {
		String key = jFilter.optString("key");
		Operator operator = Filter.getStringAsFilterOperator(jFilter.optString("operator"));
		Object value = jFilter.get("value");
		return new SimpleFilter(key, value, operator);
	}
	
	public static void main(String[] args) throws Exception {
		Filter f = getFilter(newFilter);
		System.out.println("Success");
	}
}
