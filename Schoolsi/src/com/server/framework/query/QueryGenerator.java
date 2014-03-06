package com.server.framework.query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.basic.schoolsi.util.Parameters;
import com.basic.schoolsi.util.Util;
import com.schoolsi.model.pojos.EntityUtils;
import com.server.framework.exception.BasicException;
import com.server.framework.pojo.IPojo;

public class QueryGenerator {
	
	public static Query geneateQuery(Map<String, Object> reqParams) throws JSONException, BasicException{
		String filters = Util.getPropertyAsString(reqParams.get(Parameters.FILTERS));
		Long limit = Util.getPropertyAsLong(reqParams.get(Parameters.LIMIT));
		Long offset = Util.getPropertyAsLong(reqParams.get(Parameters.OFFSET));
		String entityName = Util.getPropertyAsString(reqParams.get(Parameters.ENTITY));
		String joins = Util.getPropertyAsString(reqParams.get(Parameters.JOINS));
		String order = Util.getPropertyAsString(reqParams.get(Parameters.ORDER));
		Filter filter = FilterParser.getFilter(filters);
		IPojo entity = getEntity(entityName);
		Query query =new Query(entity.getTable(), entity.getDatabase());
		if (joins != null && !joins.equals("null") && !joins.equals("")) {
			query.select(EntityUtils.getColumns(entity.getClass()));
			addJoins(query, joins);
		}
		if (filter != null) {
			query.where(filter);
		}
		addOrder(query, order);
		if (limit != null) {
			query.limit(limit.longValue());
		}
		if (offset != null) {
			query.offset(offset.longValue());
		}
		return query;
	}
	
	public static boolean isJoinQuery(Map<String,Object> reqParams) {
		String joins = Util.getPropertyAsString(reqParams.get(Parameters.JOINS));
		if (joins != null && !joins.equals("null") && !joins.equals("")) {
			return true;
		}
		return false;
	}
	
	private static IPojo getEntity(String className) throws BasicException {
		try {
			String BASIC_PATH_FOR_POJO = "com.cloudpact.mowbly.pojos.";
			String classWithPath = null;
			if (className.equals("MowblyUser")) {
				classWithPath = "com.cloudpact.mowbly.mowblyusermanager.MowblyUser";
			} else {
				classWithPath = BASIC_PATH_FOR_POJO+ className;
			}
			IPojo entityObj = (IPojo)Class.forName(classWithPath).newInstance();	
			return entityObj;
		} catch (Exception e) {
			throw new BasicException("Class not found with this name");
		}
	}
	
	private static void addJoins(Query query, String joins) throws JSONException, BasicException {
		JSONArray jJoins = new JSONArray(joins);
		for (int i=0, length=jJoins.length(); i<length; i++) {
			JSONObject jJoin = jJoins.getJSONObject(i);
			String entityName = jJoin.getString("entity");
			String leftColumn = jJoin.getString("leftColumn"); // Column name in global table.
			String rightColumn = jJoin.getString("rightColumn");// Column name in join table.
			IPojo joinEntity = getEntity(entityName);
			query.join(joinEntity.getTable(), leftColumn, rightColumn)
			.select(EntityUtils.getColumns(joinEntity.getClass()))
			.end();
		}
	}
	
	private static void addOrder(Query query, String order) throws JSONException {
		if (order !=null && !order.equals("null") && !order.equals("")) {
			JSONArray jOrder = new JSONArray(order);
			for (int i=0, length=jOrder.length(); i<length; i++) {
				JSONObject jOrderObj = jOrder.getJSONObject(i);
				query.order(jOrderObj.getString("column"), Query.getStringAsSortOrder(jOrderObj.getString("order")));
			}
		}
	}
	
	public static List<Query> generateCreateQueries(Map<String, Object> reqParams) throws JSONException, BasicException {
		List<Query> queries = new ArrayList<Query>();
		
		String entitiesToCreate = Util.getPropertyAsString(Parameters.ENTITIESTOCREATE);
		JSONArray jArray = new JSONArray(entitiesToCreate);
		for (int i=0, length=jArray.length(); i<length; i++) {
			JSONObject entityToCreate = jArray.getJSONObject(i);
			JSONObject jFilter = entityToCreate.optJSONObject(Parameters.FILTERS);
			JSONObject propertiesToCreate = entityToCreate.optJSONObject(Parameters.PROPERTIESTOCREATE);
			Filter filter = FilterParser.getFilter(jFilter);
			String entityClass = entityToCreate.optString(Parameters.ENTITY);
			IPojo pojo = getEntity(entityClass);
			/** TODO: some checks may required for create mode.
			 * this is required because how to handle the dependent entity creations like 
			 *  User address creation is dependent on user id. How to handle those while 
			 *  creating only custom generated ids should use for the mapping of the table. 
			 *  Custom generation should be done properly.
			 * **/
			pojo.setProperties(Util.getJSONObjectASmap(propertiesToCreate));
			Query query = new Query(pojo.getTable(), pojo.getDatabase()).insert()
						.values(pojo.getProperties());
			if (filter != null) {
				query.where(filter);
			}
			queries.add(query);
		}
		return queries;
	}
	
	public static List<Query> generateUpdateQueries(Map<String, Object> reqParams) throws JSONException, BasicException {
		List<Query> queries = new ArrayList<Query>();
		
		String entitiesToUpdate = Util.getPropertyAsString(Parameters.ENTITIESTOUPATE);
		JSONArray jArray = new JSONArray(entitiesToUpdate);
		for (int i=0, length=jArray.length(); i<length; i++) {
			JSONObject entityToUpdate = jArray.getJSONObject(i);
			JSONObject jFilter = entityToUpdate.optJSONObject(Parameters.FILTERS);
			JSONObject propertiesToUpdate = entityToUpdate.optJSONObject(Parameters.PROPERTIESTOUPDATE);
			Filter filter = FilterParser.getFilter(jFilter);
			String entityClass = entityToUpdate.optString(Parameters.ENTITY);
			IPojo pojo = getEntity(entityClass);
			Query query = new Query(pojo.getTable(), pojo.getDatabase()).update()
						.values(Util.getJSONObjectASmap(propertiesToUpdate));
			if (filter != null) {
				query.where(filter);
			}
			queries.add(query);
		}
		return queries;
	}
	
	public static List<Query> generateDeleteQueries(Map<String, Object> reqParams) throws JSONException, BasicException {
		String entitiesToDelete = Util.getPropertyAsString(Parameters.ENTITIESTODELETE);
		JSONArray jArray = new JSONArray(entitiesToDelete);
		List<Query> queries = new ArrayList<Query>();
		for (int i=0, length=jArray.length(); i<length; i++) {
			JSONObject entityToDelete = jArray.getJSONObject(i);
			JSONObject jFilter = entityToDelete.optJSONObject(Parameters.FILTERS);
			Filter filter = FilterParser.getFilter(jFilter);
			String entityClass = entityToDelete.optString(Parameters.ENTITY);
			IPojo pojo = getEntity(entityClass);
			Query query = new Query(pojo.getTable(), pojo.getDatabase()).delete();
			if (filter != null) {
				query.where(filter);
			}
			queries.add(query);
		}
		return queries;
	}
}
