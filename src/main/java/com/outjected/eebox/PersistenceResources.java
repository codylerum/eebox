package com.outjected.eebox;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.SynchronizationType;

import org.jboss.logging.Logger;

public class PersistenceResources {

	private Logger log = Logger.getLogger(PersistenceResources.class);

	@PersistenceUnit
	private EntityManagerFactory entityManagerFactory;

	@Produces
	@RequestScoped
	public EntityManager entityManager() {
		log.infof("Producing Entity Manager");
		return entityManagerFactory
				.createEntityManager(SynchronizationType.SYNCHRONIZED);
	}

	protected void closeEntityManager(@Disposes EntityManager entityManager) {
		log.infof("Disposing EntityManager");
		if (entityManager.isOpen()) {
			entityManager.close();
		}
	}
}
