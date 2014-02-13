package com.server.framework.persistence;

import java.util.List;

import com.server.framework.query.Query;

public interface IPersistenceManger {
	
	void executeQuery(List<Query> queries, boolean inTransaction);
}
