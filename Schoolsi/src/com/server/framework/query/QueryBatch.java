package com.server.framework.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * @author Vamshi
 */
public class QueryBatch {

	private List<Query> mQueries = null;
	
	public QueryBatch() {
		this((Query[]) null);
	}

	public QueryBatch(Query... queries) {
		this((queries!=null)? Arrays.asList(queries): null);
	}

	public QueryBatch(List<Query> queries) {
		if (mQueries == null) {
			mQueries = new ArrayList<Query>();
		}
		if (queries !=  null) { 
			mQueries.addAll(queries);
		}
	}

	public QueryBatch addQuery(Query query) {
		if (mQueries == null) {
			mQueries = new ArrayList<Query>();
		}
		mQueries.add(query);
		return this;
	}
	
	public QueryBatch addQuery(QueryBatch queryBatch) {
		return addQuery(queryBatch.getQueries());
	}
	
	public QueryBatch addQuery(List<Query> queries) {
		if (mQueries == null) {
			mQueries = new ArrayList<Query>();
		}
		mQueries.addAll(queries);
		return this;
	}
	
	public List<Query> getQueries() {
		return mQueries;
	}
}
