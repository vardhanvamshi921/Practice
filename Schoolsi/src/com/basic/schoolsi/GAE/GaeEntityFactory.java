package com.basic.schoolsi.GAE;

import com.server.framework.persistence.EntityFactory;
import com.server.framework.persistence.EntityManager;


public class GaeEntityFactory implements EntityFactory{
	@Override
	public EntityManager getEntityManager() {
		return GaeEntityManager.getInstance();
	}
}
