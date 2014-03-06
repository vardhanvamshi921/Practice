package com.basic.schoolsi.GAE;

import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.server.framework.query.Filter;
import com.server.framework.query.Query;


public class GaeFilterOperators {
	
	public  static FilterOperator EQUAL = FilterOperator.EQUAL;
	public static FilterOperator NOTEQUAL = FilterOperator.NOT_EQUAL;
	
	public static FilterOperator GREATERTHAN = FilterOperator.GREATER_THAN;
	public static FilterOperator GREATERTHANOREQUAL = FilterOperator.GREATER_THAN_OR_EQUAL;
	
	public static FilterOperator LESSTHAN = FilterOperator.LESS_THAN;
	public static FilterOperator LESSTHANOREQUAL = FilterOperator.LESS_THAN_OR_EQUAL;
	
	public static FilterOperator IN = FilterOperator.IN;
	
	public static CompositeFilterOperator AND = CompositeFilterOperator.AND;
	public static CompositeFilterOperator OR = CompositeFilterOperator.OR;
	
	public static SortDirection ASC = SortDirection.ASCENDING;
	public static SortDirection DESC = SortDirection.DESCENDING;
	
	
	public static FilterOperator getOperator(Filter.Operator operator) { 
		FilterOperator op = null;
		switch(operator) {
			case IN:
				op = IN;
				break;
			case LESS_THAN:
				op = LESSTHAN;
				break;
			case LESS_THAN_OR_EQUAL:
				op = LESSTHANOREQUAL;
				break;
			case GREATER_THAN:
				op = GREATERTHAN;
				break;
			case GREATER_THAN_OR_EQUAL:
				op = GREATERTHANOREQUAL;
				break;
			case EQUAL:
				op = EQUAL;
				break;
			case NOT_EQUAL:
				op = NOTEQUAL;
				break;
			default :
				op = EQUAL;
				break;
		}
		
		return op;
	}

	public static CompositeFilterOperator getCompositeFilterOperator(Filter.Operator compositeFilterOperator) {
		CompositeFilterOperator op = null;
		switch(compositeFilterOperator){
			case AND:
				op = AND;
				break;
			case OR:
				op = OR;
				break;
			default :
				op = AND;
				break;
		}
		return op;
	}
	
	public static SortDirection getOrder(Query.SortOrder order) {
		SortDirection op = null;
		switch(order){
			case ASC:
				op = ASC;
				break;
			case DESC:
				op = DESC;
				break;
			default :
				op = ASC;
				break;
		}
		return op;
	}
	
}