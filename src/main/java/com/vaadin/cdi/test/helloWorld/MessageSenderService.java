package com.vaadin.cdi.test.helloWorld;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

@Stateless
public class MessageSenderService implements MessageSender {
	public static final String SENDER_ID = "senderID";
	@Resource(lookup = "java:/jms/talkQueue")
	private Queue talkQueue;
	@Resource(lookup = "java:/ConnectionFactory")
	private ConnectionFactory connectionFactory;
	
	@Resource
	private EJBContext ejbContext;

	@Override
	public void sendTextMessage(String message) {
		Connection connection = null;
		Session session = null;
		try {
			connection = connectionFactory.createConnection();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			TextMessage textMessage = session.createTextMessage();
			textMessage.setText(message);
			textMessage.setStringProperty(SENDER_ID, ejbContext.getCallerPrincipal().getName());
			MessageProducer producer = session.createProducer(talkQueue);
			producer.send(textMessage);
			producer.close();
		} catch (JMSException e) {
			throw new RuntimeException("sending failed", e);
		} finally {
			try {
				session.close();
			} catch (Exception e) {
			}
			try {
				connection.close();
			} catch (Exception e) {
			}
		}
	}
}
