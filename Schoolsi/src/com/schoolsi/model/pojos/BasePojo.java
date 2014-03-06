package com.schoolsi.model.pojos;

import java.util.List;
import java.util.Map;

import com.basic.schoolsi.annotations.Column;
import com.basic.schoolsi.util.Parameters;
import com.server.framework.pojo.IPojo;

public class BasePojo implements IPojo{
	
	protected String TABLENAME = "";
	protected String DATABASENAME = "";
	
	@Column
	protected long _eid;
	@Column
	protected long createdon;
	@Column
	protected long modifiedon;
	@Column
	protected String modifiedby;
	@Column
	protected String createdby;

	public BasePojo(String tableName, String dbtype) {
		this.TABLENAME = tableName;
		this.DATABASENAME = dbtype;
	}
	
	public void setProperties(Map<String, Object> properties) {
		Long eid = getPropertyAsLong(properties ,Parameters.EID);
		Long createdOn = getPropertyAsLong(properties ,Parameters.CREATEDON);
		Long modifiedOn = getPropertyAsLong(properties ,Parameters.MODIFIEDON);
		String createdBy = getPropertyAsString(properties ,Parameters.CREATEDBY);
		String modifiedBy = getPropertyAsString(properties ,Parameters.MODIFIEDBY);
		
		if (eid != null && eid != 0) {
			setId(eid);
		}
		if (createdOn != null) {
			setCreatedOn(createdOn);
		} 
		if (createdBy != null) {
			setCreatedBy(createdBy);
		}
		if (modifiedOn !=null) {
			setModifiedOn(modifiedOn); 
		}
		if (modifiedBy != null) {
			setModifiedBy(modifiedBy);
		} 
	}

	protected static String getPropertyAsString(Map<String, Object> properties, String name) {
		if(properties !=null){
			Object value = properties.get(name);
			if (value != null) {
				return String.valueOf(value);
			}
		}
		
		return null;
	}

	protected static Long getPropertyAsLong(Map<String, Object> properties, String name) {
		
		if(properties !=null){
			String value = getPropertyAsString(properties, name);
			if (value != null) {
				return Long.valueOf(value);
			}
		}
		
		return null;
	}

	protected static Integer getPropertyAsInteger(Map<String, Object> properties, String name) {
		
		if(properties != null) {
			String value = getPropertyAsString(properties, name);
			if (value != null) {
				return Integer.valueOf(value);
			}
		}
		
		return null;
	}

	@Override
	public Map<String, Object> getProperties() {
		try {
			return EntityUtils.getProperties(this);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<String> getCoumnNames() {
		return null;
	}

	@Override
	public String getCreatedBy() {
		return null;
	}

	@Override
	public long getCreatedOn() {
		return 0;
	}

	@Override
	public Long getId() {
		return _eid;
	}

	@Override
	public String getModifiedBy() {
		return null;
	}

	@Override
	public long getModifiedOn() {
		return 0;
	}

	@Override
	public String getTable() {
		return this.TABLENAME;
	}

	@Override
	public List<String> getTextFieldKeys() {
		return null;
	}

	@Override
	public List<String> getUniqueKeys() {
		return null;
	}

	@Override
	public void setCreatedBy(String createdBy) {
		
	}

	@Override
	public void setCreatedOn(long createdOn) {
		
	}

	@Override
	public void setDatabase(String database) {
		
	}

	@Override
	public void setModifiedBy(String modifiedBy) {
		
	}

	@Override
	public void setModifiedOn(long modifiedOn) {
		
	}

	@Override
	public void setTable(String table) {
		
	}

	@Override
	public void setId(long id) {
		this._eid = id;
		
	}

	@Override
	public String getDatabase() {
		return this.DATABASENAME;
	}

}
