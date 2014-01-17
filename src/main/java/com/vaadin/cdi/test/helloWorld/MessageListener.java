package com.vaadin.cdi.test.helloWorld;

import javax.enterprise.event.Observes;

public interface MessageListener {

	public abstract void messageAdded(@Observes BoardMessage boardMessage);

}