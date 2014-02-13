package com.server.framework.query;

/**
 * 
 * @author Vamshavardhan G
 */
public abstract class Filter {

	public static enum Operator {
		OR,
		AND,
		IN,
		EQUAL,
		NOT_EQUAL,
		LESS_THAN,
		LESS_THAN_OR_EQUAL,
		GREATER_THAN,
		GREATER_THAN_OR_EQUAL
	};

	protected Filter.Operator mOperator;

	public Filter(Operator operator) {
		mOperator = operator;
	}

	public Filter.Operator getOperator() {
		return mOperator;
	}
	
	public static Filter.Operator getStringAsFilterOperator(String fString) {
		Filter.Operator op = Filter.Operator.EQUAL;
		
		if (fString.equals("OR")) {
			op = Filter.Operator.OR;
		} else if (fString.equals("AND")) {
			op = Filter.Operator.AND;
		} else if (fString.equals("IN")) {
			op = Filter.Operator.IN;
		} else if (fString.equals("NOT_EQUAL")) {
			op = Filter.Operator.NOT_EQUAL;
		} else if (fString.equals("LESS_THAN")) {
			op = Filter.Operator.LESS_THAN;
		} else if (fString.equals("LESS_THAN_OR_EQUAL")) {
			op = Filter.Operator.LESS_THAN_OR_EQUAL;
		} else if (fString.equals("GREATER_THAN")) {
			op = Filter.Operator.GREATER_THAN;
		} else if (fString.equals("GREATER_THAN_OR_EQUAL")) {
			op = Filter.Operator.GREATER_THAN_OR_EQUAL;
		}
		return op;
	}
}
