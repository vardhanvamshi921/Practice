package com.basic.schoolsi.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class Util {
	
	 public static Properties readProperties(String filename) throws IOException{
        Properties props = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream stream = loader.getResourceAsStream(filename);
        if (stream == null ) {
        	throw new IOException(filename + "Not found");
        }
        props.load(stream);
        return props;   
    }
	 
	public static InputStream getResourceAsStream(String resoucePath) {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream stream = loader.getResourceAsStream(resoucePath);
        return stream;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> List<T> getList(JSONArray array) throws JSONException {
		List<T> list = new ArrayList<T>();
		for(int i = 0, j = array.length(); i < j; i++){
			list.add((T) array.get(i));
		}
		return list;
	}
	public static List<Long> getJSONArrayAsListOfLongs(JSONArray jArray) throws JSONException {
		ArrayList<Long> listdata = new ArrayList<Long>();     
		if (jArray != null) { 
		   for (int i=0;i<jArray.length();i++){ 
		    listdata.add(jArray.getLong(i));
		   } 
		}
		return listdata; 
	}
	
	public static List<String> getJSONArrayAsArrayListOfString(JSONArray jArray) throws JSONException {
		List<String> list = new ArrayList<String>();
		if (jArray != null) {
			for(int i=0; i<jArray.length(); i++){
				list.add(jArray.getString(i));
			}
		}
		
		return list;
	}

	public static List<Object> getJSONArrayAsArrayList(JSONArray jArray) throws JSONException{
		List<Object> list = new ArrayList<Object>();
		for(int i=0; i<jArray.length(); i++){
			list.add(jArray.get(i));
		}
		return list;
	}
	
	public static List<Integer> getJSONArrayAsArrayListAsInteger(JSONArray jArray) throws JSONException{
		List<Integer> list = new ArrayList<Integer>();
		for(int i=0; i<jArray.length(); i++){
			list.add(jArray.getInt(i));
		}
		return list;
	}
	
	public static List<Map<String, Object>> getJSONArrayAsListOfMaps(JSONArray jArray) throws JSONException {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for(int i=0; i<jArray.length(); i++){
			list.add(getJSONObjectASmap(jArray.getJSONObject(i)));
		}
		return list;
	}
	
	public static Map<String, Object> getJSONObjectASmap(JSONObject jObj) throws JSONException {
		Map<String, Object> map = new HashMap<String, Object>();
		Iterator<?> iterator = jObj.keys();
		while(iterator.hasNext()) {
			String key = (String) iterator.next();
			
			Object value = jObj.get(key);
			if (jObj.isNull(key)) {
				value = null;
			}
			if( value != null && value instanceof JSONObject) {
				value = getJSONObjectASmap((JSONObject) value);
			} 
			map.put(key, value);
		}
		
		return map;
	}
	
	public static Long[] getStringArrayAsLongArray(String[] arr) {
		Long[] data = new Long[arr.length];  
		for (int i = 0; i < arr.length; i++) {  
		  data[i] = Long.valueOf(arr[i]);  
		}
		return data;
	}
	
	public static String[] getStringArrayAsStringArray(String[] arr) {
		String[] data = new String[arr.length];  
		for (int i = 0; i < arr.length; i++) {  
		  data[i] = String.valueOf(arr[i]);  
		}
		return data;
	}
	
	public static void writeZipEntry(String path, String name , byte[] content, ZipOutputStream zos) throws IOException{
		
		String fPath = path;
		String zpath = (fPath != null && fPath.length()>0) ? fPath + "/" + name : name;
		ZipEntry ze = new ZipEntry(zpath);
		zos.putNextEntry(ze);
		zos.write(content);
		
	}

	public static String getPropertyAsString(Object propertyvalue) {
		if (propertyvalue != null) {
			return String.valueOf(propertyvalue);
		}
		return null;
	}

	public static Long getPropertyAsLong(Object propertyvalue) {
		
		String value = getPropertyAsString(propertyvalue);
		if (value != null && value != "") {
			return Long.valueOf(value);
		}
		return null;
	}

	public static Integer getPropertyAsInteger(Object propertyvalue) {
		
		String value = getPropertyAsString(propertyvalue).trim();
		if (value != null) {
			return Integer.valueOf(value);
		}
		return null;
	}
	
	public static int getRandomNuber() {
		Random rn = new Random();
		int number = rn.nextInt(20);
		if(number<= 0){
			number = number+1;
		}
		return number;
	}
	
	public static List<Long> getLongList(JSONArray array) throws JSONException {
		List<Long> list = new ArrayList<Long>();
		for(int i = 0, j = array.length(); i < j; i++){
			Object obj = array.get(i);
			if (obj instanceof Integer) {
				list.add(new Long((Integer) obj));
			} else {
				list.add((Long) obj);
			}
		}
		return list;
	}
	
	public static void copyInputStreamToOutPutStream(InputStream input, OutputStream output) throws IOException{
	    byte[] buffer = new byte[1024]; // Adjust if you want
	    int bytesRead;
	    while ((bytesRead = input.read(buffer)) != -1) {
	        output.write(buffer, 0, bytesRead);
	    }
	    input.close();
	}
	
	public static String getIpAddr(HttpServletRequest request) {      
		   String ip = request.getHeader("x-forwarded-for");      
		   if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {      
		       ip = request.getHeader("Proxy-Client-IP");      
		   }      
		   if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {      
		       ip = request.getHeader("WL-Proxy-Client-IP");      
		   }      
		   if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {      
		       ip = request.getRemoteAddr();      
		   }      
		   return ip;      
		}  
}
