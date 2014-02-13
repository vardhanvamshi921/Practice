package com.basic.schoolsi.servlets;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONException;


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
		
		
	}
}
