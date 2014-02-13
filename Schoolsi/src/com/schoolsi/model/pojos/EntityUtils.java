package com.schoolsi.model.pojos;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.basic.schoolsi.annotations.Column;
import com.basic.schoolsi.annotations.PrimaryKey;
import com.basic.schoolsi.annotations.Text;
import com.server.framework.pojo.IPojo;
public class EntityUtils {

	public static List<Field> getAllFields(List<Field> fields, Class<?> type) {
	    for (Field field: type.getDeclaredFields()) {
	        field.setAccessible(true);
	    	if(field.getAnnotation(Column.class) != null){
		    	fields.add(field);
	        }
	    }

	    if (type.getSuperclass() != null) {
	        fields = getAllFields(fields, type.getSuperclass());
	    }

	    return fields;
	}

	public static List<String> getColumns(Class<? extends IPojo> entity) {
		List<String> columns = new ArrayList<String>();

		List<Field> fields = getAllFields(new ArrayList<Field>(), entity);
		Iterator<Field> iterator = fields.iterator();
		while (iterator.hasNext()) {
			Field field = iterator.next();
			columns.add(field.getName());
		}

		return columns;
	}

	public static Map<String, Object> getProperties(IPojo entity) throws IllegalArgumentException, IllegalAccessException {
		Map<String, Object> properties = new HashMap<String, Object>();
		
		List<Field> fields = getAllFields(new ArrayList<Field>(), entity.getClass());
		Iterator<Field> iterator = fields.iterator();
		List<String> primaryKey = new ArrayList<String>();
		List<String> textFields = new ArrayList<String>();
		while(iterator.hasNext()){
			Field field = iterator.next();
			String  name = field.getName();
			Object value = field.get(entity);
			properties.put(name, value);
			if (field.getAnnotation(PrimaryKey.class) != null) {
				primaryKey.add(name);
			}
			
			if (field.getAnnotation(Text.class) != null) {
				textFields.add(name);
			}
		}
		if (primaryKey.size() >0) {
			properties.put("primarykey", primaryKey);
		}
		
		if (textFields.size() > 0) {
			properties.put("textfield", textFields);
		}
		
		return properties;
	}
}
