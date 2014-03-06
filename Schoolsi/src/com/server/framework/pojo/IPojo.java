package com.server.framework.pojo;

import java.util.List;
import java.util.Map;

public interface IPojo {
	
	/**
	 * Returns the id
	 * @return
	 */
	Long getId();
	/**
	 * Sets the id
	 */
	void setId(long id);
	
	/**
	 * Returns the created on
	 * @return
	 */
	long getCreatedOn();
	
	/**
	 * Sets the createdon
	 * @param createdOn
	 */
	void setCreatedOn(long createdOn);
	
	/**
	 * Returns the createdby
	 * @return
	 */
	String getCreatedBy();
	
	/**
	 * Sets the createdby
	 * @param createdBy
	 */
	void setCreatedBy(String createdBy);
	
	/**
	 * Returns the modified on
	 * @return
	 */
	long getModifiedOn();
	
	/**
	 * Sets the modifiedon
	 * @param modifiedOn
	 */
	void setModifiedOn(long modifiedOn);
	
	/**
	 * Returns the modifiedBy
	 * @return
	 */
	String getModifiedBy();
	
	/**
	 * Sets the modifiedBy
	 * @param modifiedBy
	 */
	void setModifiedBy(String modifiedBy);
	
	/**
	 * Returns the table of the pojo
	 * @return
	 */
	String getTable();
	
	/**
	 * Sets the table of the pojo
	 * @param table
	 */
	void setTable(String table);
	
	String getDatabase();
	
	/**
	 * Sets the database of the table
	 * @param database
	 */
	void setDatabase(String database);
	
	/**
	 * Returns the properties
	 * @return
	 */
	Map<String, Object> getProperties();
	/**
	 * Returns the properties
	 * @return
	 */
	void setProperties(Map<String, Object> properites);
	
	/**
	 * Get column names
	 * @return
	 */
	List<String> getCoumnNames();
	
	/**
	 * 
	 * @return
	 */
	List<String> getUniqueKeys();
	
	/**
	 * Returns the text field keys if any. Specific to appengine
	 * @return
	 */
	List<String> getTextFieldKeys();
	
	//TODO: Composite unique keys and individual unique keys. And foregin key references. And Error messages
	
}
