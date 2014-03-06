package com.basic.schoolsi.util;

import java.util.HashMap;
import java.util.Map;

public class Configuration {
	
	
	private static Map<String, String> routes = new HashMap<String, String>();
	private static String servletControllerPkg = "com.basic.schoolsi.servlets.controllers.";

	public static Map<String, String> getRoutes() {
		return routes;
	}

	static {
		
		routes.put("/logout", servletControllerPkg +"LogoutServletController");
		routes.put("/wsvc", servletControllerPkg +"WebServiceServletController");
		routes.put("/buildservice", servletControllerPkg +"BuildServletController");
		routes.put("/mobiledata", servletControllerPkg +"MobileDataServletController");
		routes.put("/companyservice", servletControllerPkg +"CompanyServletController");
		routes.put("/passwordservice", servletControllerPkg +"PasswordServletController");
		
		routes.put("/fileservice", servletControllerPkg +"ProjectFileServletController");
		routes.put("/importservice", servletControllerPkg +"ImportServletController");
		routes.put("/exportservice", servletControllerPkg +"ExportServletController");
		routes.put("/projectservice", servletControllerPkg +"ProjectServletController");
		
		routes.put("/packservice", servletControllerPkg +"PackServletController");
		routes.put("/rolepackservice", servletControllerPkg +"RolePackServletController");
		routes.put("/roleservice", servletControllerPkg +"RoleServletController");
		
		routes.put("/userservice", servletControllerPkg +"UserServletController");
		routes.put("/userroleservice", servletControllerPkg +"UserRoleServletController");
		
		routes.put("/deviceservice", servletControllerPkg +"DeviceServletController");
		routes.put("/deviceuserservice", servletControllerPkg +"DeviceUserServletController");
		
		routes.put("/policy", servletControllerPkg +"PolicyServletController");
		routes.put("/obligationservice", servletControllerPkg +"ObligationServletController");
		
		routes.put("/libraryservice", servletControllerPkg +"LibraryServletController");
		routes.put("/searchservice", servletControllerPkg +"SearchServletController");
		routes.put("/simulatorservice", servletControllerPkg +"SimulatorServletController");
		routes.put("/pushservice", servletControllerPkg +"PushNotificationServletController");
		routes.put("/companyconfigureservice", "com.cloudpact.mowbly.configuration.ConfigureServletController");
		
		routes.put("/taskqueues", servletControllerPkg +"TaskQueueServletController");
		routes.put("/reachus", servletControllerPkg +"ReachusServletController");
		routes.put("/mowblydownloads", servletControllerPkg +"MowblyDownloadServletController");
		routes.put("/cpuserservice", servletControllerPkg +"CpUserServletController");
		
	}
}
