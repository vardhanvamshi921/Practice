package com.basic.schoolsi.model.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.basic.schoolsi.GAE.GaeEntityManager;
import com.basic.schoolsi.util.Parameters;
import com.server.framework.Token.IToken;
import com.server.framework.exception.DBException;
import com.server.framework.persistence.EntityManager;
import com.server.framework.pojo.IPojo;
import com.server.framework.query.Filter;
import com.server.framework.query.Query;
import com.server.framework.query.QueryBatch;

public class BaseModelController {
	
		public EntityManager entityManager = null;
		protected IToken token = null;
		
		public BaseModelController(){
			entityManager = GaeEntityManager.getInstance();
		}
		
		public BaseModelController(IToken token){
			entityManager = GaeEntityManager.getInstance();
			this.token = token;
		}

		protected Query getUpdateQuery(String tablename, String dbAlias, long id, Map<String, Object> updatePropertiesMap){
			Query query = new Query(tablename, dbAlias)
									.update()
									.values(updatePropertiesMap)
									.where(Parameters.EID, id);
			return query;
		}
		
		public void updateById(String tablename, String dbAlias, long id, Map<String, Object> updatePropertiesMap) throws DBException{
			update(getUpdateQuery(tablename, dbAlias, id, updatePropertiesMap));
		}
		
		public void updateById(IPojo iEntity, Map<String, Object> updatePropertiesMap) throws DBException{
			update(getUpdateQuery(iEntity.getTable(), iEntity.getDatabase(), iEntity.getId(), updatePropertiesMap).setReturnPojo(iEntity));
		}

		public void update(Query... queries) throws DBException {
			update(Arrays.asList(queries));
		}

		public void update(List<Query> queries) throws DBException {
			entityManager.update(addCreatedByAndModifiedByAndCreatedOnAndModifedOn(queries));
		}
		
		public void put(Query... queries) throws DBException {
			entityManager.put(Arrays.asList(queries));
		}
		
		public void put(List<Query> queries) throws DBException{
			entityManager.put(addCreatedByAndModifiedByAndCreatedOnAndModifedOn(queries));
		}
		
		public List<Map<String, Object>> query(Query query) throws DBException{
			return entityManager.query(query);
		}
		
		public Map<String, Object> get(Query query) throws DBException{
			List<Map<String, Object>> results =  query(query);
			if(results!= null && results.size()==0) {
				throw new DBException("123");
			}
			return results.get(0);
		}
		public Map<String, Object> get(String tableName, String dbAlias) throws DBException {
			Query query = new Query(tableName, dbAlias);
			return get(query);
		}
		
		public Map<String, Object> getById(String tableName, String dbAlias, long id) throws DBException {
			return getByKey(tableName, dbAlias, Parameters.EID, id);
		}

		public Map<String, Object> getByKey(String tableName, String dbAlias, String key, Object value) throws DBException {
			Query query = new Query(tableName, dbAlias);
			query.where(key, value);
			return get(query);
		}
		
		public List<Map<String, Object>> queryByKey(String tableName, String dbAlias, String key, Object value) throws DBException {
			Query query = new Query(tableName, dbAlias);
			query.where(key, value);
			return query(query);
		}
		
		protected List<Filter> getFiltersAsList(Filter... filters){
			return (Arrays.asList(filters));
		}
		
		public void executeBatchInTransaction(Query... queries) throws DBException  {
			QueryBatch queryBatch = new QueryBatch(queries);
			executeBatchInTransaction(queryBatch);
		}
		
		public void executeBatch(Query... queries) throws DBException  {
			QueryBatch queryBatch = new QueryBatch(queries);
			executeBatch(queryBatch);
		}
		
		public void executeBatch(List<Query> queries) throws DBException {
			QueryBatch queryBatch = new QueryBatch(queries);
			executeBatch(queryBatch);
		}
		
		protected void executeBatchInTransaction(List<Query> queries) throws DBException {
			QueryBatch queryBatch = new QueryBatch(queries);
			executeBatchInTransaction(queryBatch);
		}
		
		protected void executeBatch(QueryBatch queryBatch) throws DBException {
			executeBatch(queryBatch, false);
		}

		protected void executeBatchInTransaction(QueryBatch queryBatch) throws DBException {
			executeBatch(queryBatch, true);
		}

		public void executeBatch(QueryBatch queryBatch, boolean inTransaction) throws DBException {
			entityManager.executeBatch(addMetaDataToQuery(queryBatch), inTransaction);
		}
		
		public void delete(Query query) throws DBException{
			entityManager.delete(query);
		}
		
		public List<Map<String, Map<String, Object>>> joinQuery(Query query) throws DBException {
			return entityManager.joinQuery(query);
		}
		public long count(String tableName, String dbAlias) throws DBException {
			return count(tableName, dbAlias,  null, "COUNT(*)");
		}
		
		public long count(String tableName, String dbAlias,  Filter filter, String countString) throws DBException {
			Query query = new Query(tableName, dbAlias)
								.count(countString);
			if (filter != null) {
				query.where(filter);
			}
								
			return entityManager.count(query);	
		}
		
		public void incrementOrDecrementProperty(String tableName, String dbAlias, String key, long value) throws DBException {
			Query query = new Query(tableName, dbAlias).incrementOrDecrement(key, value);
			executeBatch(query);
		}
		/**
		 * Adds created by and modified by and created on modified on to query values
		 * @param queryBatch
		 * @return
		 */
		public QueryBatch addMetaDataToQuery(QueryBatch queryBatch) {
			return new QueryBatch(addCreatedByAndModifiedByAndCreatedOnAndModifedOn(queryBatch.getQueries()));
		}
		public List<Query> addCreatedByAndModifiedByAndCreatedOnAndModifedOn(List<Query> queries) {
			List<Query> modifiedQueries = new ArrayList<Query>();
			Iterator<Query> iterator = queries.iterator();
			while(iterator.hasNext()) {
				Query query = iterator.next();
				query = modifyDbAlias(query);
				modifiedQueries.add(query);
			}
			return modifiedQueries;
		}
		
		public void createEntity(IPojo pojo, String database) throws DBException {
			Query query = new Query(pojo.getTable(), database).insert()
					.values(pojo.getProperties()).setReturnPojo(pojo);
			
			this.put(query);
		}
	
		public Query modifyDbAlias(Query query) {
			if (query.dbAlias().equals(Parameters.SCHOOLDB)) {
				if (token != null) {
					query.dbAlias("");
				}
			}
			return query;
		}
	}


