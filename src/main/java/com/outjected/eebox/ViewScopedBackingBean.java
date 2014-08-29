package com.outjected.eebox;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.inject.Produces;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.jboss.logging.Logger;

@ViewScoped
@Named
public class ViewScopedBackingBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Logger log;

	private UUID uuid;

	private String foo = "Default";
	private Date date;

	@PostConstruct
	private void postCostruct() {
		log = Logger.getLogger(ViewScopedBackingBean.class);
		uuid = UUID.randomUUID();
		log.infof("Created new View Scoped Backing Bean: %s", uuid.toString());
	}

	@PreDestroy
	private void preDestroy() {
		log.infof("Destroying View Scoped Backing Bean: %s", uuid.toString());
	}

	public void submitValue() {
		log.infof("Set Value: %s", foo);
	}

	public void immediate() {
		log.infof("Didn't Submit");
	}

	public String submitAndNavigate() {
		return "vs.xhtml&faces-redirect=true";
	}

	public String getFoo() {
		return foo;
	}

	public void setFoo(String foo) {
		this.foo = foo;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Produces
	@Named
	public Date currentDate() {
		return new Date();
	}
}
