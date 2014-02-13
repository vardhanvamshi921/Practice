package com.server.framework.role;
/**
 * 
 * @author Vamshavardhan G
 * vamshi@cloudpact.com
 */
public interface IRole {
	
	/**
	 * Returns the id of the role
	 * @return
	 */
	Object getId();
	
	/**
	 * Sets the id of the role
	 * @param id
	 */
	void setId(Object id);
	
	/**
	 * Returns the name of the role
	 * @return
	 */
	String getName();
	
	/**
	 * Sets the name of the role
	 * @param name
	 */
	void setName(String name);
	
	
}
