package com.outjected.eebox;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@ConversationScoped
@Named
public class ConversationManagement implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Conversation conversation;

	public void begin() {
		if (conversation.isTransient()) {
			conversation.begin();
		}
	}

	public void end() {
		if (!conversation.isTransient()) {
			conversation.end();
		}
	}
}
