package com.schoolsi.model.pojos;

import java.util.Map;

import com.basic.schoolsi.annotations.Column;
import com.basic.schoolsi.util.Parameters;

public class User extends BasePojo {

	public static String TABLE = "users";
	public static String DATABASE = Parameters.SCHOOLDB;
	
	@Column
	public String userid;
	@Column
	public String username; 
	@Column
	public String firstname;
	@Column
	public String lastname;
	@Column
	public String password;
	@Column
	public String email;
	@Column
	public long phonenumber;
	@Column
	public String usertype;
	@Column
	public SchoolFile profilepic;
	@Column
	public String institueid;
	@Column
	public String branchid;
	
	
	public User(Map<String, Object> properties) {
		super(TABLE, DATABASE);
		this.setProperties(properties);
	}
	
	@Override
	public void setProperties(Map<String, Object> properties) {
		
	}
	
}
