package com.basic.schoolsi.token;

import java.util.List;

import com.server.framework.Token.IToken;
import com.server.framework.role.IRole;

public class Token implements IToken{

	@Override
	public void expireToken() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getEncryptedPassword() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLoginId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPlainPassword() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IRole> getRoles() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getTokenExpiryTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getTokenId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isLoggedIn() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isValid() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setEncryptedPassword(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLoggedIn(boolean arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLoginId(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPlainPassword(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRoles(List<IRole> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTokeId(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTokenExpiryTime(long arg0) {
		// TODO Auto-generated method stub
		
	}

}
