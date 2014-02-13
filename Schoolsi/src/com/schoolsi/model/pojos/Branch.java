package com.schoolsi.model.pojos;

import java.util.Map;

import com.basic.schoolsi.annotations.Column;
import com.basic.schoolsi.util.Parameters;

public class Branch extends BasePojo{
	
	public static final String BRANCH = "branch";
	public static final String FRANCHAISE = "franchaise";
	
	public static String TABLE = "branches";
	public static String DATABASE = Parameters.SCHOOLDB;
	
	@Column
	public String branchid;// B01 or F01 need to check it once.
	@Column
	public String name;
	@Column
	public String type;
	
	public Branch(Map<String, Object> properties) {
		super(TABLE, DATABASE);
		this.setProperties(properties);
	}
	
	@Override
	public void setProperties(Map<String, Object> properties) {
		
	}
}
