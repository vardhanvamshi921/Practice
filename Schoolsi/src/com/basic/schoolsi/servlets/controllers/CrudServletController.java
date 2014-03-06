package com.basic.schoolsi.servlets.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.json.JSONException;

import com.basic.schoolsi.model.controller.BaseModelController;
import com.basic.schoolsi.util.Codes;
import com.basic.schoolsi.util.Parameters;
import com.server.framework.exception.BasicException;
import com.server.framework.exception.DBException;
import com.server.framework.query.Query;
import com.server.framework.query.QueryBatch;
import com.server.framework.query.QueryGenerator;

public class CrudServletController extends BaseServletController{
	/**
	 * Read all entities by filters.
	 * @throws JSONException
	 * @throws IOException
	 * @throws DBException
	 */
	public void readByFiltersAction() throws JSONException, IOException, DBException {
		Map<String, Object> reqParams = getRequestParams();
		try {
			Query query = QueryGenerator.geneateQuery(reqParams);
			BaseModelController baseDataController = new BaseModelController();
			if (QueryGenerator.isJoinQuery(reqParams)) {
				sendResponse(baseDataController.joinQuery(query));
			} else {
				sendResponse(baseDataController.query(query));
			}
		} catch (BasicException e) {
			sendErrorMessage(Codes.QUERY_FAILED, e.getLocalizedMessage());
		}
	}
	
	public void updateAction() throws JSONException, IOException {
		Map<String, Object> reqParams = getRequestParams();
		Object isBatchExecution = reqParams.get(Parameters.BATCHEXECUTION);
		boolean inTransaction = Boolean.parseBoolean((String)isBatchExecution);
		try {
			List<Query> queries = QueryGenerator.generateUpdateQueries(reqParams);
			new BaseModelController().executeBatch(new QueryBatch(queries), inTransaction);
			sendResponse();
		} catch (BasicException e) {
			e.printStackTrace();
		} catch (DBException e) {
			e.printStackTrace();
		}
	}
	
	public void createAction() throws JSONException, IOException {
		Map<String, Object> reqParams = getRequestParams();
		Object isBatchExecution = reqParams.get(Parameters.BATCHEXECUTION);
		boolean inTransaction = Boolean.parseBoolean((String)isBatchExecution);
		try {
			List<Query> queries = QueryGenerator.generateCreateQueries(reqParams);
			new BaseModelController().executeBatch(new QueryBatch(queries), inTransaction);
			sendResponse();
		} catch (BasicException e) {
			e.printStackTrace();
		} catch (DBException e) {
			e.printStackTrace();
		}	

	}
	
	public void deleteAction() throws JSONException, IOException {
		Map<String, Object> reqParams = getRequestParams();
		Object isBatchExecution = reqParams.get(Parameters.BATCHEXECUTION);
		boolean inTransaction = Boolean.parseBoolean((String)isBatchExecution);
		try {
			List<Query> queries = QueryGenerator.generateDeleteQueries(reqParams);
			new BaseModelController().executeBatch(new QueryBatch(queries), inTransaction);
			sendResponse();
		} catch (BasicException e) {
			e.printStackTrace();
		} catch (DBException e) {
			e.printStackTrace();
		}	
	}
}












