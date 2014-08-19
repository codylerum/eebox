package com.outjected.eebox.websockets;

public class WebSocketMessage {

	private String interest;
	private String message;

	public WebSocketMessage(String interest, String message) {
		this.interest = interest;
		this.message = message;
	}

	public String getInterest() {
		return interest;
	}

	public String getMessage() {
		return message;
	}
}
