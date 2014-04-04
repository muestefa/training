package com.vaadin.cdi.test.helloWorld;

public class BoardMessage {

	public BoardMessage(String message, String senderId) {
		super();
		this.message = message;
		this.senderId = senderId;
	}
	public String getMessage() {
		return message;
	}
	public String getSenderId() {
		return senderId;
	}
	private final String message;
	private final String senderId;
}
