package com.server.framework.persistence;


public class DatabaseEntitFactoryMapping {
	
	private static String MYSQLENTITYFACTORY = "com.cloudpact.mowbly.data.mysql.MysqlEntityFactory";
	
	public static String getEntityFactory(String databaseVendor) {
		String entityFactoryClass = MYSQLENTITYFACTORY;
		//TODO: For other databases like oracle and others
		return entityFactoryClass;
	}
	
	public static String getDriverForDatabase(String databaseVendor) {
		String driver = "com.mysql.jdbc.Driver";
		//TODO: For other databases like oracle and others
		return driver;
	}
	
	public static String getJdbcUrl(String host, int port, String dbName, String databaseVendor) {
		String jdbcUrl = String.format("jdbc:mysql://%s:%d/%s", host, port, dbName);
		// TODO: do for oracle and other databases.
		return jdbcUrl;
	}
	
}
