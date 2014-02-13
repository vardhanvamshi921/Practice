package com.schoolsi.model.pojos;

import java.util.Map;

import com.basic.schoolsi.annotations.Column;
import com.basic.schoolsi.util.Parameters;

public class Address extends BasePojo{
	
	public static String TABLE = "address";
	public static String DATABASE = Parameters.SCHOOLDB;
	
	public String reference;
	@Column
	public String hno;
	@Column
	public String street;
	@Column
	public String landmark;
	@Column
	public String city;
	@Column
	public long pincode;
	

	public Address(Map<String, Object> properties) {
		super(TABLE, DATABASE);
		this.setProperties(properties);
	}
	
	@Override
	public void setProperties(Map<String, Object> properties) {
		
	}

	
}
