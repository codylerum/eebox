package com.outjected.eebox;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.jboss.logging.Logger;

public class AllMessagesProducer {
	private static Logger log = Logger.getLogger(AllMessagesProducer.class);

	@Inject
	private EntityManager em;

	@Produces
	@RequestScoped
	@Named
	public List<Message> allMessages() {
		log.infof("Producing All Messages List");
		return em.createQuery("select m from Message m order by id desc",
				Message.class).getResultList();
	}
}
