package com.server.framework.Token;

import java.util.List;

import com.server.framework.role.IRole;

/**
 * 
 * @author Vamshavardhan G
 * vamshi@cloudpact.com
 * 
 * Basic Token is token it will have the default properties to authenticate
 */
public interface IToken {
	
	/**
	 * Returns the tokeid
	 * @return
	 */
	String getTokenId();
	
	/**
	 * Sets the tokeId
	 * @param tokenid
	 */
	void setTokeId(String tokenid);
	
	/**
	 * Returns the token expirytime
	 * @return
	 */
	long getTokenExpiryTime();
	
	/**
	 * Sets the token ExpiryTime
	 * @param tokeExpiryTime
	 */
	void setTokenExpiryTime(long tokeExpiryTime);
	
	/**
	 * Expires the token
	 */
	void expireToken();
	
	/**
	 * Checks weather the toke is valid or not
	 * @return
	 */
	boolean isValid();
	
	/**
	 * Checks weather token has logged in user or not
	 * @return
	 */
	boolean isLoggedIn();
	
	/**
	 * Sets the logged true or not
	 */
	void setLoggedIn(boolean isLoggedin);
	
	/**
	 * Returns the logind like username or employyed id. 
	 *  or Which is used to login and unique to identify the user
	 * @return
	 */
	String getLoginId();
	
	/**
	 * Sets the logInd
	 * @param loginid
	 */
	void setLoginId(String loginId);
	
	/**
	 * Returns the plain password which is entered by the user.
	 * @return
	 */
	String getPlainPassword();
	
	/**
	 * Sets the plain password
	 * @param password
	 */
	void setPlainPassword(String password);
	
	/**
	 * Returns the encrypted password
	 * @return
	 */
	String getEncryptedPassword();
	
	/**
	 * Sets the encrypted password
	 * @param encryptedPassword
	 */
	void setEncryptedPassword(String encryptedPassword);
	
	/**
	 * Gets the roles of the logged in user. If logged in else it should return null
	 * @return
	 */
	List<IRole> getRoles();
	
	/**
	 * Sets the roles
	 * @param role
	 */
	void setRoles(List<IRole> role);
}
