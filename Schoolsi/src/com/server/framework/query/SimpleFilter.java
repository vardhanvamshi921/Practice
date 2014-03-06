package com.server.framework.query;

public class SimpleFilter extends Filter {

	protected String mKey;
	protected Object mValue;

	public SimpleFilter(String key, Object value) {
		this(key, value, Filter.Operator.EQUAL);
	}

	public SimpleFilter(String key, Object value, Filter.Operator operator) {
		super(operator);

		mKey = key;
		mValue = value;
	}

	public String getKey() {
		return mKey;
	}

	public Object getValue() {
		return mValue;
	}
}
