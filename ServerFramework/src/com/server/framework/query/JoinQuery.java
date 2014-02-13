package com.server.framework.query;


public class JoinQuery extends Query {

	protected Query mBase;
	protected String mLeft;
	protected String mRight;
	protected String mDbAlias;
	
	public JoinQuery(Query base, String tableName, String dbAlias, String left, String right) {
		super(tableName, dbAlias);
		mBase = base;
		mLeft = left;
		mRight = right;
	}

	public String left() {
		return mLeft;
	}

	public String right() {
		return mRight;
	}

	public Query end() {
		mBase.join(this);
		return mBase;
	}
}
