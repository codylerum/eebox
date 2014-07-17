package com.outjected.eebox;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;

public class FacesProducers {

	@Produces
	@RequestScoped
	public FacesContext facesContext() {
		return FacesContext.getCurrentInstance();
	}
}
