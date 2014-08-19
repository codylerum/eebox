package com.outjected.eebox.websockets;

import java.io.Serializable;
import java.util.UUID;

import javax.enterprise.event.Event;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

@ViewScoped
@Named
public class WebSocketPageBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String interest = UUID.randomUUID().toString();

	private static final int NUM_TO_SEND = 10;
	private static final int DELAY = 1000;

	@Inject
	private Event<WebSocketMessage> wsm;

	public String startProcessing() throws InterruptedException {
		for (int i = 1; i <= 10; i++) {
			wsm.fire(new WebSocketMessage(interest, String.format(
					"Processing Message %s of %s", i, NUM_TO_SEND)));
			Thread.sleep(DELAY);
		}
		return FacesContext.getCurrentInstance().getViewRoot().getViewId();
	}

	public String getInterest() {
		return interest;
	}
}
