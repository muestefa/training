package com.vaadin.cdi.test.helloWorld;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class Board implements Serializable {

	@Inject
	private MessageSender senderService;

	private static final long serialVersionUID = 1L;

	private Set<MessageListener> listeners = new HashSet<>();

	private List<BoardMessage> messages = new ArrayList<>();

	public void addMessage(String message, String senderID) {
		BoardMessage boardMessage = new BoardMessage(message, senderID);
		messages.add(boardMessage);
		for (MessageListener listener : listeners) {
			listener.messageAdded(boardMessage);
		}
	}

	public List<BoardMessage> getMessages() {
		return messages;
	}

	public int getVersion() {
		return messages.size();
	}

	public void registerListener(MessageListener listener) {
		listeners.add(listener);
	}

	public void unregisterListener(MessageListener listener) {
		listeners.remove(listener);
	}

}
