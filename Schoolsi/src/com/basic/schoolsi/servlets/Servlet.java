package com.basic.schoolsi.servlets;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONException;

import com.basic.schoolsi.servlets.controllers.BaseServletController;
import com.basic.schoolsi.util.Configuration;


public class Servlet extends HttpServlet {

	private static final long serialVersionUID = -4127394106554154559L;
	
	private static Logger log = Logger.getLogger(Servlet.class);

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		try {
			processReq(req, resp);
		} catch (JSONException e) {
			log.error(e.getMessage());
		}
	} 
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		try {
			processReq(req, resp);
		} catch (JSONException e) {
			log.error(e.getMessage());
		}
	}
	
	public void processReq(HttpServletRequest req, HttpServletResponse resp) throws IOException, 
			JSONException {
		String uri = req.getRequestURI();
		String contextPath = req.getContextPath();	
		uri = uri.substring(contextPath.length());
		log.error("1");
		//AccessPermission.checkAccessPermission(req, "a", null);
		Map<String, String> routes = Configuration.getRoutes();
		String className = routes.get(uri);
		if(className == null){
			uri = "/"+uri.split("/")[1];
			className = routes.get(uri);
		}
		try {
			if (className != null) {
				
				BaseServletController servletController = (BaseServletController) Class.forName(className).newInstance();
				servletController.setRequestAndResponse(req, resp);
				servletController.readRequestParams();
				String action = servletController.getAction();
				try {
					java.lang.reflect.Method method = servletController.getClass().getMethod(action+"Action");
					method.invoke(servletController);
				} catch (SecurityException e) {
				  e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			} else {
				resp.sendError(404);
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
			log.fatal(e.getLocalizedMessage());
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			log.fatal(e.getLocalizedMessage());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			log.fatal(e.getLocalizedMessage());
		}
		
	}
}
