package com.schoolsi.model.pojos;

import java.util.Map;

import com.basic.schoolsi.annotations.Column;
import com.basic.schoolsi.util.Parameters;

public class Institue extends BasePojo{
	
	public  final static String UNIVERSITY = "university";
	public final static String COLLEGE = "college";
	public final static String SCHOOL = "school";
	
	public final static String INTER = "intermediate";
	public final static String DEGREE = "degree";
	public final static String ENGINEERING = "engineering";
	public final static String POSTGRADUATE = "pg";
	
	public static String TABLE = "institues";
	public static String DATABASE = Parameters.GOBALDB;
	
	@Column
	public String name;
	@Column
	public String type;
	@Column 
	public String level;
	@Column
	public String email;
	@Column
	public long phonenuber;
	@Column
	public String instituteid;
	@Column
	public SchoolFile logo;
	@Column
	public String databasename; // 6letters random generated string + institute id
	
	public Institue(Map<String, Object> properties) {
		super(TABLE, DATABASE);
		this.setProperties(properties);
	}
	
	@Override
	public void setProperties(Map<String, Object> properties) {
		
	}
}
