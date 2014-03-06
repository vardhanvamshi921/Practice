package com.basic.schoolsi.servlets.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.basic.schoolsi.util.Codes;
import com.basic.schoolsi.util.Parameters;

public abstract class BaseServletController {
	
	protected String action = null;
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	
	private Map<String, Object> requestParameterMap = null;
	private  HttpSession session = null;
	
	public BaseServletController() {
		
	}
	
	public BaseServletController(HttpServletRequest req, HttpServletResponse resp) {
		this.setRequestAndResponse(req, resp);
	}
	
	public void setRequestAndResponse(HttpServletRequest req, HttpServletResponse resp) {
		this.request = req;
		this.response = resp;
		this.action = req.getParameter(Parameters.ACTION);
	}
	
	@SuppressWarnings("rawtypes")
	public void readRequestParams() {
		
		Map<String,Object>reqParamMap = new HashMap<String, Object>();

		Map params = this.request.getParameterMap();		
		
		Iterator i = params.keySet().iterator();
		while ( i.hasNext() ) {
			String key = (String) i.next();
			int pos = key.indexOf("[]");
			Object value;
			if (pos != -1) {
				value = params.get(key);
				key = key.substring(0, pos); 
			} else {
				value = ((String[]) params.get(key))[0];
			}
			
			if(key.equals(Parameters.ACTION) && value != null){
				setAction((String)value);
			} else {
				reqParamMap.put(key, value);
			}
		}
		setRequestParameterMap(reqParamMap);
	}
	public void setRequestParameterMap(Map<String, Object> requestParameterMap) {
		this.requestParameterMap=  Collections.unmodifiableMap(requestParameterMap);
	}
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	
	public void sendResponse(Map<String, Object> data) throws JSONException, IOException {
		List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
		results.add(data);
		sendResponse(Codes.QUERY_SUCCESSFUL, Codes.QUERY_SUCCESSFUL_STR, results);
	}
	
	public void sendResponse(Collection<Map<String, Object>> data) throws JSONException, IOException {
		sendResponse(Codes.QUERY_SUCCESSFUL, Codes.QUERY_SUCCESSFUL_STR, data);
	}
	
	public void sendResponse(Collection<Map<String, Object>> data, String key, Object value) throws JSONException, IOException {
		JSONObject rObj = new JSONObject();
		rObj.put(Parameters.DATA, data);
		rObj.put(key, value);
		rObj.put(Parameters.CODE, Codes.QUERY_SUCCESSFUL);
		rObj.put(Parameters.MSG, Codes.QUERY_SUCCESSFUL_STR);	
		sendResponse(rObj);
	}
	
	public void sendResponse(int code, Map<String, Map<String, Object>> data) throws JSONException, IOException {

		JSONObject rObj = new JSONObject();
		rObj.put(Parameters.DATA, data);
		rObj.put(Parameters.CODE, Codes.QUERY_SUCCESSFUL);
		rObj.put(Parameters.MSG, Codes.QUERY_SUCCESSFUL_STR);
		sendResponse(rObj);
	}
	
	public void sendResponse(int code, List<Long> data) throws JSONException, IOException {
		
		JSONObject rObj = new JSONObject();
		JSONArray jArray = new JSONArray(data);
		rObj.put(Parameters.DATA, jArray);
		rObj.put(Parameters.CODE, Codes.QUERY_SUCCESSFUL);
		rObj.put(Parameters.MSG, Codes.QUERY_SUCCESSFUL_STR);
		sendResponse(rObj);
	}
	
	public void sendResponse(List<Map<String, Map<String, Object>>>  data) throws JSONException, IOException {
		
		JSONObject rObj = new JSONObject();
		JSONArray jArray = new JSONArray(data);
		rObj.put(Parameters.DATA, jArray);
		rObj.put(Parameters.CODE, Codes.QUERY_SUCCESSFUL);
		rObj.put(Parameters.MSG, Codes.QUERY_SUCCESSFUL_STR);
		sendResponse(rObj);
	}
	
	public void sendResponse(int code, String msg, Collection<Map<String, Object>> data) throws JSONException, IOException {
		JSONObject rObj = new JSONObject();
		rObj.put(Parameters.CODE, code);
		rObj.put(Parameters.MSG, msg);
		if(data !=null){
			rObj.put(Parameters.DATA, new JSONArray(data));
		}
		sendResponse(rObj);
	}
	
	
	public void sendResponse(int code, String msg, Map<String, Object> data) throws JSONException, IOException {
		JSONObject rObj = new JSONObject();
		rObj.put(Parameters.CODE, code);
		rObj.put(Parameters.MSG, msg);
		if(data != null) {
			JSONArray d = new JSONArray();
			JSONObject jData = new JSONObject(data);
			d.put(jData);
			rObj.put(Parameters.DATA, d);
		}
		sendResponse(rObj);
	}
		
	public void sendResponse(int code, String msg) throws JSONException, IOException {
		sendResponse(code, msg, (Map<String, Object>)null);
	}
	
	public void sendResponse(String msg) throws JSONException, IOException {
		sendResponse(Codes.QUERY_SUCCESSFUL, msg, (Map<String, Object>)null);
	}
	
	public void sendResponse() throws JSONException, IOException {
		sendResponse(Codes.QUERY_SUCCESSFUL, Codes.QUERY_SUCCESSFUL_STR, (Map<String, Object>)null);
	}
	
	public void sendResponse(JSONObject rObj) throws IOException, JSONException { 
		setResponseContentType("application/json");
		response.addHeader("Transfer-Encoding", "chunked"); 
		response.getWriter().println(rObj.toString());
		return;
	}
	
	public void sendResponse(byte[] data) throws IOException {
		response.addHeader("Transfer-Encoding", "chunked"); 
		response.getOutputStream().write(data, 0, data.length);
		return;
	}
	
	public void sendErrorMessage(int code, String message) throws JSONException, IOException {
		JSONObject rObj = new JSONObject();
		rObj.put(Parameters.CODE, code);
		rObj.put(Parameters.MSG, message);
		setResponseContentType("application/json");
		response.getWriter().println(rObj.toString());
		return;
	}
	
	public  void setSessionAttribute(String key, Object value) {
		session.setAttribute(key, value);
	}
	
	public void removeSessionProperties() {
		@SuppressWarnings("unchecked")
		Enumeration<String> sessionParamNames = session.getAttributeNames();
		while(sessionParamNames.hasMoreElements()){
			String key = (String) sessionParamNames.nextElement();
			session.removeAttribute(key);
		}
	}
	
	public Object getSessionProperty(String key) {
		return session.getAttribute(key);
	}
	
	public void addResponseHeader(String key, String value) {
		response.addHeader(key, value);
	}
	
	public void setResponseContentType(String contentType) {
		response.setContentType(contentType);
	}
	
	public void sendRedirect(String url) throws IOException {
		response.sendRedirect(request.getContextPath() + url);
		return;
	}
	
	public void sendError(int code) throws IOException {
		response.sendError(code);
		return;
	}
	
	/**
	 * Provides a clone of the original request parameter map
	 * @return
	 */
	public Map<String, Object> getRequestParams() {
		HashMap<String, Object> reqParams = new HashMap<String, Object>();
		if (requestParameterMap != null) {
			reqParams.putAll(requestParameterMap);
		}
		return reqParams;
	}
	
	protected String getFileNameFromContentDisposition(String header) {
		String fileName = null;
		Pattern regex = Pattern.compile("(?<=filename=\").*?(?=\")");
		Matcher regexMatcher = regex.matcher(header);
		if (regexMatcher.find()) {
		    fileName = regexMatcher.group();
		}
		return fileName;
	}
}
