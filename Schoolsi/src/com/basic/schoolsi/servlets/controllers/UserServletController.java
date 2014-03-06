package com.basic.schoolsi.servlets.controllers;

import java.io.IOException;
import java.util.Map;

import org.json.JSONException;

import com.basic.schoolsi.model.controller.BaseModelController;
import com.schoolsi.model.pojos.PowerUser;
import com.server.framework.Token.IToken;
import com.server.framework.exception.DBException;

public class UserServletController extends BaseServletController{
	
	Map<String, Object> reqParams = getRequestParams();
	
	public void createUserAction() {
		
	}
	
	public void updateUserAction() {
		
	}
	
	public void deleteUserAction() {
		
	}
	
	public void createPowerUserAction() throws JSONException, IOException {
		PowerUser powerUser = new PowerUser(reqParams);
		try {
			new BaseModelController().createEntity(powerUser, PowerUser.DATABASE);
			sendResponse();
		} catch (DBException e) {
			e.printStackTrace();
		}
	}
}
