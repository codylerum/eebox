package com.outjected.eebox;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.jboss.logging.Logger;

@ViewScoped
@Named
public class RequestScopedEntityManagerBackingBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private static Logger log = Logger
			.getLogger(RequestScopedEntityManagerBackingBean.class);

	@Inject
	@RequestParameter
	private Instance<Integer> mid;

	@Inject
	private EntityManager em;

	private Message message;

	@PostConstruct
	private void postConstruct() {
		if (mid.get() != null) {
			message = em.find(Message.class, mid.get());
		} else {
			message = new Message();
		}
	}

	@Transactional
	public String submitValue() {
		log.infof("Executing Update");
		message = em.merge(message);
		em.flush();
		return "rsem.xhtml?faces-redirect=true&mid=" + message.getId();
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}
}
