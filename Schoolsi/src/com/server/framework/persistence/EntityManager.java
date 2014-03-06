package com.server.framework.persistence;
import java.util.List;
import java.util.Map;

import com.server.framework.exception.DBException;
import com.server.framework.query.Query;
import com.server.framework.query.QueryBatch;

public interface EntityManager {
	
	public void  put(List<Query> queries) throws DBException;
	
	public void update(List<Query> quires)  throws DBException;
	
	public List<Map<String, Object>> query(Query query)  throws DBException;
	
	public List<Map<String, Map<String, Object>>> joinQuery(Query query)  throws DBException;
	
	public void delete(Query query)  throws DBException;

	public void executeBatch(QueryBatch queryBatch, boolean inTransaction)  throws DBException;
	
	public long count(Query query) throws DBException;
}
