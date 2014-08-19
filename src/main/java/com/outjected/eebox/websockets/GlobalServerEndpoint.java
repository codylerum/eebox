package com.outjected.eebox.websockets;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.enterprise.event.Observes;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.RemoteEndpoint.Async;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.jboss.logging.Logger;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.SetMultimap;

@ServerEndpoint(value = "/s/ws/global/{interest}")
public class GlobalServerEndpoint {

	private static final String PING = "ping";
	private static final ByteBuffer PONG_BB = ByteBuffer
			.wrap("pong".getBytes());
	private static final SetMultimap<String, Session> interests = createInterests();

	private static final Logger log = org.jboss.logging.Logger
			.getLogger(GlobalServerEndpoint.class);

	private static final Set<Session> sessions = Collections
			.synchronizedSet(new HashSet<Session>());

	public void push(@Observes WebSocketMessage wsm) {
		int sentCount = 0;

		for (Session session : interests.get(wsm.getInterest())) {
			if (session.isOpen()) {
				Async client = session.getAsyncRemote();
				client.sendText(wsm.getMessage());
				sentCount++;
			}
		}
		log.infof("Sent Message <%s> to <%s> Sessions", wsm.getMessage(),
				sentCount);
	}

	@OnOpen
	public void onOpen(Session session, @PathParam("interest") String interest) {
		sessions.add(session);
		if (interest != null && interest.length() > 1) {
			interests.put(interest, session);
		}
	}

	@OnClose
	public void onClose(Session session) {
		sessions.remove(session);
		interests.values().remove(session);
		cleanUpInactive();
	}

	@OnMessage
	public void onMessage(Session session, String message) {
		if (message.equals(PING)) {
			try {
				session.getAsyncRemote().sendPong(PONG_BB);
			} catch (IOException | IllegalArgumentException e) {
				log.infof("Failed to send Pong");
			}
		}
	}

	private void cleanUpInactive() {
		List<Session> sessionsToClose = new ArrayList<>();
		for (Session session : sessions) {
			if (!session.isOpen()) {
				sessionsToClose.add(session);
			}
		}
		closeInactive(sessionsToClose);

		int inactiveInterests = 0;
		for (Session s : interests.values()) {
			if (!s.isOpen()) {
				inactiveInterests++;
			}
		}
		if (inactiveInterests > 0) {
			log.infof(
					"After closing %s inactive interest assocations remained",
					inactiveInterests);
		}
	}

	private void closeInactive(List<Session> closingSessions) {
		int closedCount = 0;
		for (Session s : closingSessions) {
			try {
				if (!s.isOpen()) {
					s.close();
					closedCount++;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (closedCount > 10) {
			log.infof("Closed %s inactive sessions. Now %s sessions",
					closedCount, sessions.size());
		}
	}

	private static SetMultimap<String, Session> createInterests() {
		HashMultimap<String, Session> hmm = HashMultimap.create();
		return Multimaps.synchronizedSetMultimap(hmm);
	}
}