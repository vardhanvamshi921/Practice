package com.basic.schoolsi.GAE;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.QueryResultList;

public class GaeDataUtil {
	
	public static List<Entity> convertPreparedQueryToIEntities(PreparedQuery pq) {
		List<Entity> iEntites = new ArrayList<Entity>();
		for (Entity entity : pq.asIterable()) {
			iEntites.add(entity);
		}
		return iEntites;
	}
	
	public static List<Entity> convertQueryResultListToIEntities(QueryResultList<Entity> queryResults) {
		List<Entity> iEntites = new ArrayList<Entity>();
		for (Entity entity : queryResults) {
			iEntites.add(entity);
		}
		return iEntites;
	}
}
