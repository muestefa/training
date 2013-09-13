package com.vaadin.cdi.test.helloWorld;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "java:/jms/talkQueue") })
public class MessageReceiver implements MessageListener {
	private static final Logger LOG = LoggerFactory.getLogger(MessageReceiver.class);
	@Inject
	private Board globalState;

	@Override
	public void onMessage(Message arg0) {
		TextMessage message = (TextMessage) arg0;
		try {
			globalState.addMessage(message.getText());
		} catch (JMSException e) {
			LOG.error("sending failed", e);
		}
	}

}
