package com.basic.schoolsi.GAE;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.server.framework.query.Query;
import com.server.framework.query.Query.SortOrder;
import com.server.framework.query.SimpleFilter;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;


public class GaeQueryGenerator {

	
	public static Filter getFilter(String tableName, com.server.framework.query.Filter filter) {
		
		Filter gaeFilter = null;
		
		if (filter instanceof SimpleFilter) {
			String key = ((SimpleFilter) filter).getKey();
			FilterOperator operator = GaeFilterOperators.getOperator(filter.getOperator());
			Object value = ((SimpleFilter) filter).getValue();
			
			gaeFilter = getSimpleFilter(tableName, key, value, operator);
			
		} else if (filter instanceof com.server.framework.query.CompositeFilter){
			List<com.server.framework.query.Filter> filters = ((com.server.framework.query.CompositeFilter) filter).getFilters();
			
			Iterator<com.server.framework.query.Filter> iterator = filters.iterator();
			CompositeFilterOperator operator = GaeFilterOperators
					.getCompositeFilterOperator(filter.getOperator());
			
			List<Filter> subFilters = new ArrayList<Filter>();
			
			while (iterator.hasNext()) {
				com.server.framework.query.Filter f = iterator.next();
				subFilters.add(getFilter(tableName, f));
			}
			if (subFilters.size() >1) {
				gaeFilter = new CompositeFilter(operator, subFilters);
			} else if (subFilters.size() == 1){
				gaeFilter = subFilters.get(0);
			}
			
		}
		return gaeFilter;
	}
	
	
	public static com.google.appengine.api.datastore.Query getSelectQuery(Query query) {
		String table = query.table();
		Key k = KeyFactory.createKey("mowblydatastore", "mowblydatastore");
		com.google.appengine.api.datastore.Query gaeQuery = 
				new com.google.appengine.api.datastore.Query(table).setAncestor(k);
		
		com.server.framework.query.Filter filter = query.where();
		
		if (filter != null) {
			Filter f = getFilter(table, filter);
			gaeQuery.setFilter(f);
		}
		Map<String, SortOrder> sortOrder = query.order();
		
		if (sortOrder != null && sortOrder.size() >0) {
			for (Map.Entry<String, SortOrder> entry: sortOrder.entrySet()) {
				gaeQuery.addSort(entry.getKey(), GaeFilterOperators.getOrder(entry.getValue()));
			}
		}
		
		return gaeQuery;
	}
	
	public static Filter getSimpleFilter(String tableName, String key, Object value, FilterOperator operator) {
		Filter gaeFilter = null;
		if (operator == null) {
			operator = FilterOperator.EQUAL;
		}
		if (key.equals("_eid")) {
			//Util.getPropertyAsLong(value)
			Key k = KeyFactory.createKey(KeyFactory.createKey("mowblydatastore", "mowblydatastore"),tableName, null);
			gaeFilter = new FilterPredicate(Entity.KEY_RESERVED_PROPERTY, operator, k);
		} else {
			gaeFilter = new FilterPredicate(key, operator, value);
		}
		return gaeFilter;
	}
}
