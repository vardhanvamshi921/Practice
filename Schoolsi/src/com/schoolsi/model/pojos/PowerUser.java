package com.schoolsi.model.pojos;

import java.util.Map;

import com.basic.schoolsi.annotations.Column;
import com.basic.schoolsi.util.Parameters;

public class PowerUser extends BasePojo{
	public static String TABLE = "powerusers";
	public static String DATABASE = Parameters.GLOBALDB;
	
	@Column
	public String userid; 
	@Column
	public String password;
	
	public PowerUser(Map<String, Object> properties) {
		super(TABLE, DATABASE);
		this.setProperties(properties);
	}
	
	@Override
	public void setProperties(Map<String, Object> properties) {
		super.setProperties(properties);
		if (properties == null) {
			return;
		}
		setUserid(getPropertyAsString(properties, Parameters.USERID));
		setPassword(getPropertyAsString(properties, Parameters.PASSWORD));
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
