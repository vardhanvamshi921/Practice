package com.server.framework.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CompositeFilter extends Filter {

	protected List<Filter> mFilters;

	public CompositeFilter(Filter.Operator operator, Filter... filters) {
		this(operator, Arrays.asList(filters));
	}
	public CompositeFilter(Filter.Operator operator, List<Filter> filters) {
		super(operator);
		mFilters = filters;
	}
	
	public CompositeFilter(Filter.Operator operator) {
		super(operator);
	}
	
	
	
	public CompositeFilter addFilter(Filter filter) {
		if (mFilters == null) {
			mFilters = new ArrayList<Filter>();
		}
		mFilters.add(filter);
		return this;
	}
	
	public List<Filter> getFilters() {
		return mFilters;
	}
}
