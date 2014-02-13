package com.server.framework.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.server.framework.pojo.IPojo;
import com.server.framework.query.results.QueryResult;



/**
 * Usage:
 *   - Select
 *   		Query query = new Query("users")
 * 								.select("fname, lname")
 * 								.order("fname", PQuery.SortOrder.DESC)
 * 								.order("lname", PQuery.SortOrder.ASC)
 * 								.limit(10)
 * 								.offset(20);
 * 
 * 			Query query = new Query("users")
 * 								.select("fname, lname")
 * 								.join("userprofile")
 * 									.on("_eid", "user_id") // Left, Right
 * 									.where(Filter)
 * 								.join("bandwidth")
 * 									.on("_eid", "user_id") // Left, Right
 * 									.where(Filter)
 * 
 * @author Vamshi
 */
public class Query {

	public static enum SortOrder {
		ASC,
		DESC
	};

	public static final int SELECT = 1;
	public static final int INSERT = 2;
	public static final int UPDATE = 3;
	public static final int DELETE = 4;
	public static final int CREATEBLOB = 5;
	public static final int UPDATEBLOB = 6;
	public static final int COUNT = 7;
	
	public static final int MAX = 8;
	public static final int ADDINDEX = 9;
	public static final int REMOVEINDEX = 10;
	public static final int SEARCH = 11;
	
	public static final int RESETINDEX = 12;

	protected String mTableName;
	protected String mDbAlias;
	protected int mAction = SELECT;
	protected List<String> mSelectColumns;
	protected List<IPojo> mInsertOrUpdatePojos;
	protected Map<String, Object> mInsertOrUpdateValues;
	protected Map<String, Long> mIncrementOrDecrementValues;
	protected Filter mFilter;
	protected long mLimit = 0;
	protected long mOffset = 0;
	protected String mCount = "COUNT(*)";
	protected String mMax; 
	protected String mCursor;
	protected String mSearchQueryString;
	
	protected Map<String, SortOrder> mSortOrders;
	
	protected IPojo mIEntity;
	
	protected List<JoinQuery> mJoinQueries;
	
	protected QueryResult mQueryResult;
	
	// This dbAlias is the level of the database eithe company level or global leve.. The possible values are "mowbly" or "company"
	public Query(String tableName, String dbAlias) {
		mTableName = tableName;
		mDbAlias = dbAlias;
	}
	
	public String table() {
		return mTableName;
	}
	
	// This is the proper Alias which is presented in database properties file to differntiate the different company databases.
	public void dbAlias(String dbAlias) {
		this.mDbAlias = dbAlias;
	}
	
	public String dbAlias() {
		return mDbAlias;
	}
	
	public int action() {
		return mAction;
	}

	private Query action(int action) {
		mAction = action;
		return this;
	}

	public Query insert() {
		return action(INSERT);
	}

	public Query update() {
		return action(UPDATE);
	}
	
	public Query addIndex() {
		return action(ADDINDEX);
	}
	public Query removeIndex() {
		return action(REMOVEINDEX);
	}
	public Query restIndex() {
		return action(RESETINDEX);
	}
	
	public Query search() {
		return action(SEARCH);
	}
	public Query delete() {
		return action(DELETE);
	}
	
	public Query createBlob() {
		return action(CREATEBLOB);
	}
	
	public Query updateBlob() {
		return action(UPDATEBLOB);
	}
	
	public Query select(String columns) {
		return select(columns.split(","));
	}

	public Query select(String[] columns) {
		return select(Arrays.asList(columns));
	}

	public Query select(List<String> columns) {
		mSelectColumns = columns;
		return action(SELECT);
	}

	public Query values(Map<String, Object> insertOrUpdateValues) {
		if (mInsertOrUpdateValues == null) {
			mInsertOrUpdateValues = new HashMap<String, Object>();
		}
		mInsertOrUpdateValues.putAll(insertOrUpdateValues);
		return this;
	}

	public Query insertOrUpdatePojos(IPojo ipojo) {
		if (mInsertOrUpdatePojos == null) {
			mInsertOrUpdatePojos = new ArrayList<IPojo>();
		}
		mInsertOrUpdatePojos.add(ipojo);
		return this;
	}
	
	public Query insertOrUpdatePojos(List<IPojo> ipojos) {
		mInsertOrUpdatePojos = ipojos;
		return this;
	}

	public Query values(String column, Object value) {
		if (mInsertOrUpdateValues == null) {
			mInsertOrUpdateValues = new HashMap<String, Object>();
		}
		mInsertOrUpdateValues.put(column, value);
		return this;
	}

	
	public Query increment(String key) {
		return increment(key, 1);
	}

	public Query increment(String key, int value) {
		return increment(key, new Long(value));
	}

	public Query increment(String key, long value) {
		return expression(key, value);
	}

	public Query decrement(String key) {
		return decrement(key, 1);
	}

	public Query decrement(String key, int value) {
		return decrement(key, new Long(value));
	}

	public Query decrement(String key, long value) {
		return expression(key, -value);
	}
	
	public Query incrementOrDecrement(String key, long value) {
		return expression(key, value);
	}
	
	public Query incrementOrDecrement(Map<String, Long> propertiesToIncrementOrDecrement) {
		mIncrementOrDecrementValues = propertiesToIncrementOrDecrement;
		return action(UPDATE);
	}
	
	protected Query expression(String key, long value) {
		if (mIncrementOrDecrementValues == null) {
			mIncrementOrDecrementValues = new HashMap<String, Long>();
		}
		mIncrementOrDecrementValues.put(key, value);
		return action(UPDATE);
	}

	public Map<String, Long> expressions() {
		return mIncrementOrDecrementValues;
	}

	public Map<String, Object> values() {
		return mInsertOrUpdateValues;
	}

	public List<String> select() {
		return mSelectColumns;
	}

	public Query order(String column, SortOrder order) {
		if (mSortOrders == null) {
			mSortOrders = new LinkedHashMap<String, SortOrder>();
		}
		mSortOrders.put(column, order);
		return this;
	}

	public Map<String, SortOrder> order() {
		return mSortOrders;
	}

	public Query where(String key, Object value) {
		return where(key, value, Filter.Operator.EQUAL);
	}

	public Query where(String key, Object value, Filter.Operator operator) {
		return where(new SimpleFilter(key, value, operator));
	}

	public Query where(Filter filter) {
		mFilter = filter;
		return this;
	}

	public Filter where() {
		return mFilter;
	}

	public Query limit(long limit) {
		mLimit = limit;
		return this;
	}

	public long limit() {
		return mLimit;
	}

	public Query offset(long offset) {
		mOffset = offset;
		return this;
	}

	public long offset() {
		return mOffset;
	}
	
	public Query count(String count) {
		mCount = count;
		return action(COUNT);
	}
	
	public String count() {
		return mCount;
	}
	
	public Query MAX(String column) {
		mMax = column;
		return this;
	}
	
	public IPojo getPojo(){
		return mIEntity;
	}
	
	public Query setReturnPojo(IPojo iEntity){
		mIEntity = iEntity;
		return this;
	}

	public boolean hasJoins() {
		return (mJoinQueries != null && mJoinQueries.size() > 0);
	}

	public List<JoinQuery> joins() {
		return mJoinQueries;
	}

	public Query join(JoinQuery query) {
		if (mJoinQueries == null) {
			mJoinQueries = new ArrayList<JoinQuery>();
		}
		mJoinQueries.add(query);
		return this;
	}

	public JoinQuery join(String tableName, String left, String right) {
		return new JoinQuery(this, tableName, mDbAlias, left, right);
	}

	public Query end() {
		return null;
	}
	
	public Query cursor(String cursor) {
		this.mCursor = cursor;
		return this;
	}
	
	public String cursor() {
		return mCursor;
	}
	
	public Query searchQueryString(String searchQueryString) {
		mSearchQueryString = searchQueryString;
		return this;
	}
	
	public String searchQueryString() {
		return mSearchQueryString;
	}
	
	public void queryResult(QueryResult queryResult) {
		mQueryResult = queryResult;
	}
	public QueryResult queryResult() {
		return mQueryResult;
	}
}
