package com.vaadin.cdi.test.helloWorld;

import javax.ejb.Local;

@Local
public interface MessageSender {

	void sendTextMessage(String message);

}