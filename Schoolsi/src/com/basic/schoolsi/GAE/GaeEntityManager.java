package com.basic.schoolsi.GAE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.basic.schoolsi.util.Codes;
import com.basic.schoolsi.util.Parameters;
import com.basic.schoolsi.util.Util;
import com.google.appengine.api.NamespaceManager;
import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreFailureException;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.DatastoreFailureException;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.PreparedQuery.TooManyResultsException;
import com.google.appengine.api.datastore.QueryResultList;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.files.FileWriteChannel;
import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Document.Builder;
import com.google.appengine.api.search.Field;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.PutException;
import com.google.appengine.api.search.QueryOptions;
import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import com.google.appengine.api.search.SearchException;
import com.google.appengine.api.search.SearchServiceFactory;
import com.google.appengine.api.search.SortExpression;
import com.google.appengine.api.search.SortOptions;
import com.google.appengine.api.search.StatusCode;
import com.google.apphosting.api.ApiProxy;
import com.google.apphosting.api.ApiProxy.ApiDeadlineExceededException;
import com.google.apphosting.api.DeadlineExceededException;
import com.schoolsi.model.pojos.SchoolFile;
import com.server.framework.exception.DBException;
import com.server.framework.persistence.EntityManager;
import com.server.framework.pojo.IPojo;
import com.server.framework.query.CompositeFilter;
import com.server.framework.query.Filter;
import com.server.framework.query.JoinQuery;
import com.server.framework.query.Query;
import com.server.framework.query.QueryBatch;
import com.server.framework.query.SimpleFilter;

public class GaeEntityManager implements EntityManager{

	private static Logger log = Logger.getLogger("Database");
	
	private static GaeEntityManager instance = null;
	
	private DatastoreService datastore = null;
	private BlobstoreService bso;
	
	private GaeEntityManager() {
	    // Only for instantiation
		datastore = DatastoreServiceFactory.getDatastoreService();
		bso = BlobstoreServiceFactory.getBlobstoreService();
	}
		
	public static EntityManager getInstance() {
		if(instance == null) {
			instance = new GaeEntityManager();
		 }
		 return instance;
	}
		
	@Override
	public void put(List<Query> queries) throws DBException  {
		boolean inTransaction = true;
		if (queries.size() <=1) {
			inTransaction = false;
		}
		dispatchBatches(new QueryBatch(queries), inTransaction);	
	}
	
	@Override
	public void update(List<Query> queries) throws DBException {
		boolean inTransaction = true;
		if (queries.size() <=1) {
			inTransaction = false;
		} 
		dispatchBatches(new QueryBatch(queries), inTransaction);
	}
	
	@Override
	public List<Map<String, Object>> query(Query query) throws DBException
			 {
		
		List<Map<String, Object>> results = new ArrayList<Map<String ,Object>>();
		
		NamespaceManager.set(query.dbAlias());
		if (query.action() == Query.SEARCH) {
			return searchIndex(query);
		}
		
		com.google.appengine.api.datastore.Query gaeQuery = GaeQueryGenerator.getSelectQuery(query);
		PreparedQuery pq = datastore.prepare(gaeQuery);
		long limit = query.limit();
		List<Entity> entities = null;
		
		if (limit >0 ) {
			FetchOptions fetchOptions = FetchOptions.Builder.withLimit((int) limit).offset((int)query.offset());
			QueryResultList<Entity> r = pq.asQueryResultList(fetchOptions);
			entities = GaeDataUtil.convertQueryResultListToIEntities(r);
		} else {
			entities = GaeDataUtil.convertPreparedQueryToIEntities(pq);
		}
		Iterator<Entity> itr = entities.iterator();
		while (itr.hasNext()) {
			Entity e = itr.next();
			results.add(modifyEntityPropertiesIfBlobExists(e));
		}
		return results;
	}

	@Override
	public List<Map<String, Map<String, Object>>> joinQuery(Query query) throws DBException {
		
		List<Map<String, Map<String, Object>>> joinQueryResults = new ArrayList<Map<String, Map<String, Object>>>();
		String queryTable = query.table();
		
		List<Map<String, Object>> results =  query(query);
		
		List<JoinQuery> joins = query.joins();
		
		if (results != null && results.size() > 0) {
			
			Iterator<Map<String, Object>> itr = results.iterator();
				
			while (itr.hasNext() ) {
				Map<String, Map<String, Object>> rs = new HashMap<String, Map<String, Object>>();
				Map<String, Object> item = itr.next();
				rs.put(queryTable, item);
				
				if (query.hasJoins()) {
					Iterator<JoinQuery> iterator = joins.iterator();
					
					while (iterator.hasNext()) {
						JoinQuery joinQuery = iterator.next();
						String joinTable = joinQuery.table();
						String leftTableColumn = joinQuery.left();
						String rightTableColumn = joinQuery.right();
						com.google.appengine.api.datastore.Query joinGaeQuery= GaeQueryGenerator.getSelectQuery(joinQuery);
						com.google.appengine.api.datastore.Query.Filter f = GaeQueryGenerator.getSimpleFilter(joinTable, rightTableColumn, item.get(leftTableColumn), null);
						joinGaeQuery.setFilter(f);
						PreparedQuery pq = datastore.prepare(joinGaeQuery);
						List<Entity> entities = GaeDataUtil.convertPreparedQueryToIEntities(pq);
						Iterator<Entity> itrtr = entities.iterator();
						
						while (itrtr.hasNext()) {
							Entity e = itrtr.next();
							rs.put(joinTable, modifyEntityPropertiesIfBlobExists(e));
						}
					}
				}
				joinQueryResults.add(rs);
			}
		}
		return joinQueryResults;
	}
	
	private List<BlobKey> blobKeysFromEntities(List<Entity> entites) {
		Iterator<Entity> iterator = entites.iterator();
		List<BlobKey> blobKeys = new ArrayList<BlobKey>();
		while (iterator.hasNext()) {
			blobKeys.addAll(blobKeysFromEntity(iterator.next()));
		}
		return blobKeys;
	}
	
	private List<BlobKey> blobKeysFromEntity(Entity e) {
		List<BlobKey> blobKeys = new ArrayList<BlobKey>();
		Map<String, Object> properties = e.getProperties();
		for (Map.Entry<String, Object>entry: properties.entrySet()) {
			Object value = entry.getValue();
			if (value instanceof BlobKey) {
				blobKeys.add((BlobKey) value);
			}
		} 
		return blobKeys;
	}
	
	private BlobKey createBlob (SchoolFile blob) throws IOException {			
		try {
			FileService fileService = FileServiceFactory.getFileService();
			AppEngineFile file = fileService.createNewBlobFile(blob.getContenttype(), blob.getFilename());

			boolean lock = true;
		    FileWriteChannel writeChannel = fileService
		            .openWriteChannel(file, lock);
		    byte[] content = new byte[0];
		    byte[] contetFromBlob = blob.getContent();
		    
		    if (contetFromBlob != null) {
		    	content = contetFromBlob;
		    }
		    writeChannel.write(java.nio.ByteBuffer.wrap(content));
		   
		    writeChannel.closeFinally();
		    BlobKey fileKey = fileService.getBlobKey(file);
		    return  fileKey;
		} catch(Exception e ) {
			log.error("Create blob - " + e.getLocalizedMessage());
			// throw new IOException(e.getLocalizedMessage());
			
		}
		return null;
	}
	
	private void deleteBlobs(List<BlobKey> blobKeys){
		try {
			bso.delete(blobKeys.toArray(new BlobKey[blobKeys.size()]));
		} catch(BlobstoreFailureException e) {
			log.error("Failed to delete the blob-" + blobKeys);
		} catch (DeadlineExceededException e) {
			log.error("Failed to delete the blobs -" + blobKeys);
		} catch(ApiDeadlineExceededException e) {
			log.error("Failed to delete the blobs -" + blobKeys);
		}
	}
	
	@Override
	public void delete(Query query) throws DBException{
		dispatchBatches(new QueryBatch(query), true);
	}
	
	private Map<Integer, List<Query>> dispatchQueriesIntoActions(QueryBatch queryBatch) {
		Map<Integer, List<Query>> queriesByAction = new LinkedHashMap<Integer, List<Query>>();
		
		List<Query> queries = queryBatch.getQueries();
		Iterator<Query> iterator = queries.iterator();
		while (iterator.hasNext()) {
			Query query = iterator.next();
			int action = query.action();
			List<Query> queiresOfthisAction = queriesByAction.get(action);
			if (queiresOfthisAction == null) {
				queiresOfthisAction = new ArrayList<Query>();
			}
			queiresOfthisAction.add(query);
			queriesByAction.put(action, queiresOfthisAction);
		}
		return queriesByAction;
	}
	
	private List<Entity> getEntitiesToDelete(List<Query> queries) {
		return getEntities(queries);
	}
	
	private void deleteEntities(List<Key> keys, Transaction txn) throws ConcurrentModificationException,
		IllegalStateException, DatastoreFailureException {
		if (txn == null) {
			datastore.delete(keys);
		} else {
			datastore.delete(txn, keys);
		}
	}
	
	private List<Entity> getEntities(List<Query> queries) {
		Iterator<Query> iterator = queries.iterator();
		List<Entity> entities = new ArrayList<Entity>();
		while (iterator.hasNext()) {
			Query query = iterator.next();
			entities.addAll(getEntities(query));
		}
		return entities;
	}
	
	private List<Entity> getEntities(Query query) {
		List<Entity> entities = new ArrayList<Entity>();
		NamespaceManager.set(query.dbAlias());
		Filter f = query.where();
		
		if (f instanceof CompositeFilter) {
			List<Filter> filters = ((CompositeFilter) f).getFilters();
			Filter.Operator op= ((CompositeFilter) f).getOperator();
			int MAX_FILTERS = 30;
			int endIndex = MAX_FILTERS;
			int fromIndex = 0;
			int filtersSize = filters.size();
			if (filters.size() > MAX_FILTERS) {
				while (fromIndex < filters.size()) {
					List<Filter> sulbFilters = filters.subList(fromIndex, endIndex);
					fromIndex = endIndex;
					int diff = filtersSize - endIndex;
					if (diff < MAX_FILTERS) {
						endIndex = filtersSize;
					} else {
						endIndex += MAX_FILTERS;
					}
					query.where(new CompositeFilter(op, sulbFilters));
					com.google.appengine.api.datastore.Query gaeQuery = GaeQueryGenerator.getSelectQuery(query);
					
					PreparedQuery pq = datastore.prepare(gaeQuery);
					List<Entity> e = GaeDataUtil.convertPreparedQueryToIEntities(pq);
					entities.addAll(e);
				}
				return entities;
			}
		} 
		com.google.appengine.api.datastore.Query gaeQuery = GaeQueryGenerator.getSelectQuery(query);
		PreparedQuery pq = datastore.prepare(gaeQuery);
		List<Entity> e = GaeDataUtil.convertPreparedQueryToIEntities(pq);
		entities.addAll(e);
		return entities;
	}
	
	@Override
	public void executeBatch(QueryBatch queryBatch, boolean inTransaction) throws DBException {
		dispatchBatches(queryBatch, inTransaction);	
	}

	@Override
	public long count(Query query) throws DBException {
		List<Map<String, Object>> results = query(query);
		long count  = (results !=null)? results.size(): 0;
		return count;
	}
	
	private void dispatchBatches(QueryBatch queryBatch, boolean inTransaction ) throws DBException{
		
		Map<String, QueryBatch> querybatches = dispatchQueriesIntoBatches(queryBatch.getQueries());
		for (Map.Entry<String, QueryBatch> entry: querybatches.entrySet()) {
			QueryBatch qBatch = entry.getValue();
			executeBatch(qBatch, inTransaction, entry.getKey());
		}
	}

	private Map<String, QueryBatch> dispatchQueriesIntoBatches(List<Query> queries) {
		Iterator<Query> iterator = queries.iterator();
		Map<String, QueryBatch> queryBatches = new HashMap<String, QueryBatch>();
		
		while (iterator.hasNext()) {
			Query q = iterator.next();
			String dbAlias = q.dbAlias();
			addQueryToQueryBatch(dbAlias, q, queryBatches);
		}
		return queryBatches;
	}
	
	private void addQueryToQueryBatch(String dbAlias, Query query, Map<String, QueryBatch> queryBatches) {
		QueryBatch qBatch = queryBatches.get(dbAlias);
		if (qBatch == null) {
			qBatch =  new QueryBatch();
		}
		qBatch.addQuery(query);
		queryBatches.put(dbAlias, qBatch);
	}

	private void executeBatch(QueryBatch queryBatch, boolean inTransaction, String namespace ) throws DBException{
		if (ApiProxy.getCurrentEnvironment() == null) {  
			log.error("Thread environement is null");
			return;
	    }
		NamespaceManager.set(namespace);
		Map<Integer, List<Query>> queriesByAction = dispatchQueriesIntoActions(queryBatch);
		List<BlobKey> blobsToDelete = new ArrayList<BlobKey>();
		 
		Map<Integer, Map<List<Query>, List<Entity>>> actionsToPerform = new HashMap<Integer, Map<List<Query>, List<Entity>>>();
		Map<List<Query>, List<Entity>> entitiesByQueries= null;
		
		for (Map.Entry<Integer, List<Query>> entry: queriesByAction.entrySet() ) {
			int action  = entry.getKey();
			List<Query> queries = entry.getValue();
			entitiesByQueries = new HashMap<List<Query>, List<Entity>>();
			try {
				List<Entity> entitiesToPerformAction =  new ArrayList<Entity>();
				switch (action) {
					case Query.INSERT :
					case Query.CREATEBLOB:
						entitiesToPerformAction = getEntitiesToCreate(queries);
						break;
					case Query.UPDATE:
					case Query.UPDATEBLOB :
						Iterator<Query> iterator =  queries.iterator();
						entitiesToPerformAction = new ArrayList<Entity>();
						while (iterator.hasNext()) {
							Query query = iterator.next();
							List<Entity> entities =getEntities(query);
							blobsToDelete.addAll(mergeLocalEntityWithExixtedEntity(entities, query));
							entitiesToPerformAction.addAll(entities);
						}
						break;
					case Query.DELETE:
						entitiesToPerformAction = getEntitiesToDelete(queries);
						blobsToDelete.addAll(blobKeysFromEntities(entitiesToPerformAction));
						break;
					default:
						break;
				}
				entitiesByQueries.put(queries, entitiesToPerformAction);
				actionsToPerform.put(action, entitiesByQueries);
			} catch(Exception e) {
				
			}
		}
		
		Transaction transaction = null;
		int retryCounter = 3;
		while (true) {
			if (inTransaction) {
				transaction = datastore.beginTransaction();
			}
			try {
				for (Entry<Integer, Map<List<Query>, List<Entity>>> entry: actionsToPerform.entrySet()) {
					int action = entry.getKey();
					Map<List<Query>, List<Entity>> value = entry.getValue();
					
					switch (action) {
						case Query.INSERT :
						case Query.CREATEBLOB:
							for (Entry<List<Query>, List<Entity>> creatEntries: value.entrySet() ) {
								createEntities(creatEntries.getValue(), inTransaction, creatEntries.getKey(), transaction);
							}					
							break;
						case Query.UPDATE:
						case Query.UPDATEBLOB :
							for (Entry<List<Query>, List<Entity>> creatEntries: value.entrySet() ) {
								updateEntities(creatEntries.getValue(), inTransaction, creatEntries.getKey(), transaction);
							}
							break;
						case Query.DELETE:
							List<Key> ekeysToDelete= new ArrayList<Key>();
							for (Entry<List<Query>, List<Entity>> creatEntries: value.entrySet() ) {
								List<Entity> entitesToDelete = creatEntries.getValue();
								Iterator<Entity> deleteEntitiesItr = entitesToDelete.iterator();
								while (deleteEntitiesItr.hasNext()) {
									Entity next = deleteEntitiesItr.next();
									ekeysToDelete.add(next.getKey());
								}
							}
							deleteEntities(ekeysToDelete, transaction);
							break;
						default:
							break;
					}
				}
				if (transaction !=null && transaction.isActive()) {
					transaction.commit();
				}
				break;
			} catch(ConcurrentModificationException e) {
				if (retryCounter == 0) {
					 throw new DBException(e.getLocalizedMessage());
				}
				--retryCounter;
			} catch(DatastoreFailureException e) {
				throw new DBException(e.getLocalizedMessage());
			} catch(IllegalStateException e) {
				throw new DBException(e.getLocalizedMessage());
			} finally {
				if (transaction != null && transaction.isActive()) {
					transaction.rollback();
				}
			}
		}
		addToIndex(queriesByAction.get(Query.ADDINDEX));
		removeDocument(queriesByAction.get(Query.REMOVEINDEX));
		deleteBlobs(blobsToDelete);
	}
	
	private List<Entity> getEntitiesToCreate(List<Query> queries) throws DBException{
		Iterator<Query> iterator =  queries.iterator();
		List<Entity> entitiesToCreate = new ArrayList<Entity>();
		while (iterator.hasNext()) {
			Query query = iterator.next();
			checkForDuplicateRecordExistence(query);
			entitiesToCreate.add(createEntityObj(query));
		}
		return entitiesToCreate;
	}
	
	private void createEntities(List<Entity> entitiesToCreate, boolean inTransaction, List<Query> queries, Transaction txn) {
		List<Key> keys = new ArrayList<Key>();
		keys.addAll(persistEntities(entitiesToCreate, txn));
		addKeysToQueries(queries, keys);
	}
	
	private void updateEntities(List<Entity> entitiesToUpdate, boolean inTransaction, List<Query> queries, Transaction txn){
		persistEntities(entitiesToUpdate, txn);
		modifyPojoOfQueries(queries);
	}
	
	private List<Key> persistEntities (List<Entity> entities, Transaction txn) throws ConcurrentModificationException, 
			DatastoreFailureException, IllegalStateException  {
		List<Key> keys = null;
		if (txn == null) {
    		keys = datastore.put(entities);
    	 } else {
    		keys = datastore.put(txn, entities);
    	 }
		return keys;
	}
	
	private Entity createEntityObj(Query query) throws DBException{
		
		Map<String, Object> values = query.values();
		@SuppressWarnings("unchecked")
		List<String> textFields = (List<String>) values.get("textfield");
		Entity entity = null;
		Key dsKey = KeyFactory.createKey("mowblydatastore", "mowblydatastore");
		entity = new Entity(query.table(), dsKey);
		
		for (Map.Entry<String, Object> entry: values.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			
			if (textFields != null && textFields.contains(key)) {
				if (value == null ) {
					value = new Text("");
				} else {
					value = new Text((String)value);
				}
			}
			
			if (value instanceof SchoolFile) {
				try {
					value = createBlob((SchoolFile)value);
				} catch (IOException e) {
					throw new DBException(e.getLocalizedMessage());
				}
			}
			if (!key.equals(Parameters.EID) && !key.equals("primarykey") && !key.equals("textfield")) {
				entity.setProperty(key, value);
			}
		}
		return entity;
	}
	
	private void checkForDuplicateRecordExistence(Query query) throws DBException {
		Map<String, Object> values = query.values();
		@SuppressWarnings("unchecked")
		List<String> primaryKeys = (List<String>) values.get("primarykey");
		Query getQuery = query;
		Entity result  = null;
		if (primaryKeys != null &&primaryKeys.size() > 0) {
			List<Filter> filters = new ArrayList<Filter>();
			for ( int i=0; i<primaryKeys.size(); i++) {
				String key = primaryKeys.get(i);
				Filter f = new SimpleFilter(key, values.get(key));
				filters.add(f);
			}
			if (filters !=null && filters.size() >0) {
				Filter f = filters.get(0);
				if (filters.size()>1) {
					f = new CompositeFilter(Filter.Operator.AND, filters);
				}
				getQuery.where(f);
				com.google.appengine.api.datastore.Query gaeQuery = GaeQueryGenerator.getSelectQuery(getQuery);
				PreparedQuery pq = datastore.prepare(gaeQuery);
				try {
					result = pq.asSingleEntity();
					if (result != null) {
						throw new DBException(Codes.DUPLICATE_ENTITY_EXCEPTION);
					}
				} catch(TooManyResultsException e) {
					throw new DBException(Codes.DUPLICATE_ENTITY_EXCEPTION);
				}
			}
		}
	}
		
	private List<BlobKey> mergeLocalEntityWithExixtedEntity(List<Entity> entities, Query query) throws DBException{
		NamespaceManager.set(query.dbAlias());
		Map<String, Object> values = query.values();
		
		Iterator<Entity> itr = entities.iterator();
		List<BlobKey> blobKeysToDeleteBlobs = new ArrayList<BlobKey>();
		while (itr.hasNext()) {
			Entity ent = itr.next();
			
			if (values != null) {
				for (Map.Entry<String, Object>entry: values.entrySet()) {
					String key = entry.getKey();
					Object value = entry.getValue();
					Object oldValue = ent.getProperty(key);
					
					if (oldValue instanceof Text) {
						if (value == null ) {
							value = new Text("");
						} else {
							value = new Text((String)value);
						}
					}
					
					if (value instanceof SchoolFile || oldValue instanceof BlobKey) {
						try {
							Object blobKey = ent.getProperty(key);
							if (blobKey != null) {
								blobKeysToDeleteBlobs.add((BlobKey) blobKey);
							}
							if (value != null) {
								value = createBlob((SchoolFile)value);
							}
						} catch (IOException e1) {
							throw new DBException("Failed to create the blob");
						}
					}
					if (!key.equals(Parameters.EID) && !key.equals("primarykey")  && !key.equals("textfield")) {
						ent.setProperty(key, value);
					}
				}
			}
			
			
			Map<String, Long> incremntOrdecrementValues = query.expressions();
			if (incremntOrdecrementValues !=null && incremntOrdecrementValues.size() >0) {
				for(Map.Entry<String, Long> entry: incremntOrdecrementValues.entrySet()) {
					String key = entry.getKey();
					Long value = entry.getValue();
					Object oldValue = ent.getProperty(key);
					if (oldValue != null) {
						value = ((Long)oldValue) + value;
						value = (value<0)? 0: value;
						ent.setProperty(key, value);
					} else {
						ent.setProperty(key, value);
					}
				}
			}
		}
		return blobKeysToDeleteBlobs;
	}
	
	private Map<String, Object> modifyEntityPropertiesIfBlobExists(Entity e) throws DBException{
		Map<String, Object> resultProperties = new HashMap<String, Object>();
		Map<String, Object> properties = e.getProperties();
		
		for (Map.Entry<String, Object> entry: properties.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			if (value instanceof Text) {
				if (value != null) {
					value = ((Text) value).getValue();
				}
			}
			
			if (value instanceof BlobKey) {
				byte[] content = getDataFromBlobStore((BlobKey)value);
				if (content != null) {
					SchoolFile blob = new SchoolFile(content);
					blob.setColumnname(key);
					blob.setLength(content.length);
					value = blob;
				} else {
					value = null;
				}
			}
			resultProperties.put(key, value);
		}
		resultProperties.put(Parameters.EID, e.getKey().getId());
		return resultProperties;
	}
	
	private byte[] getDataFromBlobStore(BlobKey blobKey) throws DBException{		
		BlobInfo blobInfo = new BlobInfoFactory().loadBlobInfo(blobKey);
		  
		 if( blobInfo == null ) {
			 log.info("blob info is null for key - " + blobKey);
			 return null;
		 }

		 if( blobInfo.getSize() > Integer.MAX_VALUE )
			 throw new RuntimeException("This method can only process blobs up to " + Integer.MAX_VALUE + " bytes");
		  
		 int blobSize = (int)blobInfo.getSize();
		 int chunks = (int)Math.ceil(((double)blobSize / BlobstoreService.MAX_BLOB_FETCH_SIZE));
		 int totalBytesRead = 0;
		 int startPointer = 0;
		 int endPointer;
		 byte[] blobBytes = new byte[blobSize];
		  
		 for( int i = 0; i < chunks; i++ ){
		   
		  endPointer = Math.min(blobSize - 1, startPointer + BlobstoreService.MAX_BLOB_FETCH_SIZE - 1);
		   try {
			   byte[] bytes = bso.fetchData(blobKey, startPointer, endPointer);
			  for( int j = 0; j < bytes.length; j++ ) {
				  blobBytes[j + totalBytesRead] = bytes[j];
			  }
			  startPointer = endPointer + 1;
			  totalBytesRead += bytes.length;
		   } catch(IllegalArgumentException e) {
			   log.fatal(e.getLocalizedMessage());
			   throw new DBException(e.getLocalizedMessage());
		   }
		   catch(BlobstoreFailureException e) {
			   log.fatal(e.getLocalizedMessage());
			   throw new DBException(e.getLocalizedMessage());
		   }
		 }
		 return blobBytes;
		 
	}
	
	private void addKeysToQueries(List<Query> queries, List<Key> keys) {
		for (int i=0; i<queries.size(); i++) {
			Query query = queries.get(i);
			updateEntity(query);
			IPojo entity = query.getPojo();
			Key key = keys.get(0);
			addPrimaryKeyToEntity(key.getId(), entity);
		}
	}
	
	private void modifyPojoOfQueries(List<Query> queries) {
		Iterator<Query> iterator = queries.iterator();
		while (iterator.hasNext()) {
			updateEntity(iterator.next());
		}
	}
	
	private void updateEntity(Query query) {
		IPojo updateObj = query.getPojo();	
		if (updateObj != null) {
			Map<String, Object> props = updateObj.getProperties();
			if(query.values() != null) {
				props.putAll(query.values());
			}
			updateObj.setProperties(props);
		}
	}
	
	private void addPrimaryKeyToEntity(long key, IPojo entity) {
		if (entity != null) {
			entity.setId(key);
		}
	}
	
	public void addToIndex(List<Query> queries) {
		if (queries != null) {
			Iterator<Query> iterator = queries.iterator();
			while (iterator.hasNext()) {
				addToIndex(iterator.next());
			}
		}
	}
	
	public void addToIndex(Query query){
		String table = query.table();
		String nameSpace = query.dbAlias();
		Index index = getIndex(table, nameSpace);
		
		Map<String, Object> values = query.values();
		Document document = index.get(String.valueOf(values.get(Parameters.EID)));
		
		Builder builder = Document.newBuilder().setId(String.valueOf(values.get(Parameters.EID)));
		values.remove(Parameters.EID);
		if (document != null) {
			Iterable<Field>existedFields =  document.getFields();	
			if (existedFields != null) {
				Iterator<Field> itr = existedFields.iterator();
				while (itr.hasNext()) {
					Field f = itr.next();
					String key = f.getName();
					if (key.equals("createdon")|| key.equals("modifiedon")
							|| key.equals("createdby")|| key.equals("modifiedby") ) {
						key = "_"+key;
					}
					if (!values.containsKey(key)) {
						builder.addField(f);
					}
				}
			}
		}
		
		for (Map.Entry<String, Object> entry: values.entrySet()) {
			String key = entry.getKey();
			Object o = entry.getValue();
			if(key.equalsIgnoreCase(Parameters.CREATEDON) || key.equalsIgnoreCase(Parameters.MODIFIEDON)){
        		builder.addField(Field.newBuilder().setName(key.substring(1)).setDate(new Date((Long)o)));
        		continue;
        	}
            if (o instanceof String ) {
            	if(key.startsWith("_")) {
            		builder.addField(Field.newBuilder().setName(key.substring(1)).setText((String)o));
            	} else {
            		builder.addField(Field.newBuilder().setName(key).setText((String)o));
            	}
            } else if(o instanceof Number) {
            	if(o instanceof Long) {
            		if(2147483648L > (Long)o)
            			builder.addField(Field.newBuilder().setName(key).setNumber((Long)o));
            	}
            	else if(o instanceof Integer){
            		if(2147483648L > (Integer)o)
            			builder.addField(Field.newBuilder().setName(key).setNumber((Integer)o));
            	}
            	
            }
		}
		Document doc = builder.setId(Util.getPropertyAsString(values.get(Parameters.EID))).build();
		
		try {
		    index.put(doc);
		} catch (PutException e) {
		    if (StatusCode.TRANSIENT_ERROR.equals(e.getOperationResult().getCode())) {
		        // retry putting the document
		    	index.put(doc);
		    }
		}
	}

	public Index getIndex(String table, String nameSpace) {
		NamespaceManager.set(nameSpace);
	    IndexSpec indexSpec = IndexSpec.newBuilder().setName(table + "index").build();
	    return SearchServiceFactory.getSearchService().getIndex(indexSpec);
	}
	
	public void removeDocument(List<Query> queries){
		if (queries != null) {
			Iterator<Query> iterator = queries.iterator();
			while (iterator.hasNext()) {
				Query query = iterator.next();
				String table = query.table();
				String nameSpace = query.dbAlias();
				Index index = getIndex(table, nameSpace);
				Map<String,Object> values = query.values();
				index.delete(String.valueOf(values.get(Parameters.EID)));
			}
		}
	}
	    
	private List<Map<String, Object>> searchIndex(Query query) throws DBException {
		Index index = getIndex(query.table(), query.dbAlias());
		String queryString = query.searchQueryString();
		String cursor = query.cursor();
		List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
		long limit = query.limit();
		Results<ScoredDocument> resultsDocs = null;
		if(cursor == null || cursor.equals(""))  {
			resultsDocs = findDocuments(index, queryString, limit, com.google.appengine.api.search.Cursor.newBuilder().build());
		} else
			resultsDocs = findDocuments(index, queryString, limit, com.google.appengine.api.search.Cursor.newBuilder().build(cursor));
		
		for (ScoredDocument scoredDocument : resultsDocs) {
			 Map<String, Object> obj = new HashMap<String, Object>();
		     for (Field f : scoredDocument.getFields()) {
		    	 String fieldName = f.getName();
		    	 if (fieldName.equals("createdon") || fieldName.equals("modifiedon")) {
		    		 obj.put("_"+fieldName, f.getDate().getTime());
		    		 continue;
		    	 }
		    	 if (f.getType() == Field.FieldType.NUMBER) {
		    		obj.put(fieldName, f.getNumber());  
		    	  } else {
		    		  if (fieldName.equals("createdby") || fieldName.equals("modifiedby")) {
		    			  obj.put("_"+ fieldName, f.getText());
		    			  continue;
		    		  }
		    		obj.put(fieldName, f.getText());
		    		  
		    	  
		    	 }
		     }
		     obj.put(Parameters.EID, scoredDocument.getId());
		     results.add(obj);
		     resultsDocs.getCursor();
		     if(resultsDocs != null && resultsDocs.getCursor() != null) {
		    	 query.cursor(resultsDocs.getCursor().toWebSafeString());
		     } else {
		    	 query.cursor(null);
		     }
		}
		return results;
	}
	
	private Results<ScoredDocument> findDocuments(Index searchIndex, String queryString, long limit, com.google.appengine.api.search.Cursor cursor) throws DBException{
		try {
			 SortOptions sortOptions = SortOptions.newBuilder()
			            .addSortExpression(SortExpression.newBuilder()
			                .setExpression(Parameters.MODIFIEDON.substring(1))
			                .setDirection(SortExpression.SortDirection.DESCENDING).setDefaultValue("1262860474000"))
			                .build();
	        QueryOptions options = QueryOptions.newBuilder()
	            .setLimit((limit == 0)? 1000: (int)limit)
	            .setCursor(cursor)
	            .setSortOptions(sortOptions)
	            .build();
	        com.google.appengine.api.search.Query query = com.google.appengine.api.search.Query.newBuilder().setOptions(options).build(queryString);
	        Results<ScoredDocument> docslist = searchIndex.search(query);
	        return docslist;
	    } catch (SearchException e) {
	        log.error("Search request with query " + queryString + " failed :: " + e.getMessage());
	       throw new DBException(e.getLocalizedMessage());
	    }
	}
}

