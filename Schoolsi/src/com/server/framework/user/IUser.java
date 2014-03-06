package com.server.framework.user;

import java.util.List;

import com.server.framework.role.IRole;

/**
 * 
 * @author Vamshavardhan G
 *
 */
public interface IUser {
	
	/**
	 * Returns the login id. Unique column in the user table. 
	 * Which is using a foreign key for other tables
	 * @return
	 */
	Object getLoginId();
	
	/**
	 * Sets the logginId
	 * @param logginId
	 */
	void setLoginId(Object logginId);
	
	/**
	 * Returns the username 
	 * @return
	 */
	String getUserName();
	
	/**
	 * Sets the username
	 * @param userName
	 */
	void setUserName(String userName);
	
	/**
	 * Returns the firstname
	 * @return
	 */
	String getFirstName();
	
	/**
	 * Sets firstName
	 * @param firstName
	 */
	void setFirstName(String firstName);
	
	/**
	 * Gets the lastname
	 * @return
	 */
	String getLastName();
	/**
	 * Sets the last name
	 * @param lastName
	 */
	void setLastName(String lastName);
	/**
	 * Gets the password
	 * @return
	 */
	String getPassword();
	/**
	 * Sets the password
	 * @param password
	 */
	void setPassword(String password);
	
	/**
	 * Gets the roles
	 * @return
	 */
	List<IRole> getRoles();
	
	/**
	 * Sets the roles
	 * @param role
	 */
	void setRoles(List<IRole> role);
	
}
