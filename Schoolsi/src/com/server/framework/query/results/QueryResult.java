package com.server.framework.query.results;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryResult {
	
	int mCount;
	Map<String, Object> mResultEntityProperties = new HashMap<String, Object>();
	List<Map<String,Object>> mResults = new ArrayList<Map<String, Object>>();
	List<Map<String, Map<String, Object>>> mJoinQueryResults = new ArrayList<>();
	
	
	/**
	 * gets the result count
	 * @return
	 */
	public int getCount() {
		return mCount;
	}
	/**
	 * Sets the count
	 * @param count
	 */
	public void setCount(int count) {
		mCount = count;
	}
	
	/**
	 * Returns the single entity properties
	 * @return
	 */
	public Map<String, Object> getEntityProperties() {
		return mResultEntityProperties;
	}
	
	/**
	 * sets the single entity result properties
	 * @param properties
	 */
	public void setEntityProperties(Map<String ,Object> properties) {
		mResultEntityProperties = properties;
	}
	
	/**
	 * Returns the results
	 * @return
	 */
	public List<Map<String, Object>> getResults() {
		return mResults;
	}
	
	/**
	 * Sets the results
	 * @param results
	 */
	public void setResults(List<Map<String, Object>> results) {
		mResults = results;
	}
	
	
	/**
	 * Returns the results
	 * @return
	 */
	public List<Map<String, Map<String, Object>>> getJoinQueryResults() {
		return mJoinQueryResults;
	}
	
	/**
	 * Sets the results
	 * @param results
	 */
	public void setJoinQueryResults(List<Map<String, Map<String, Object>>> joinQueryResults) {
		mJoinQueryResults = joinQueryResults;
	}
	
}
