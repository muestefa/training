package com.vaadin.cdi.test.helloWorld;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.annotations.Push;
import com.vaadin.cdi.CDIUI;
import com.vaadin.cdi.test.factory.Margin;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * The Application's "main" class
 */
@SuppressWarnings("serial")
@CDIUI
@Push
public class MyVaadinUI extends UI implements MessageListener {
	private static final Logger LOG = LoggerFactory.getLogger(MyVaadinUI.class);

	@Inject
	private Board globalState;
	@Inject
	private MessageSender sender;
	@Inject
	@Margin
	private VerticalLayout outerLayout;
	@Inject
	@Margin
	private VerticalLayout messageLayout;
	private TextField input;
	private Label messages;

	@PostConstruct
	public void init() {
		globalState.registerListener(this);
	}

	@PreDestroy
	public void destroy() {
		globalState.unregisterListener(this);
	}

	@Override
	protected void init(VaadinRequest request) {

		setContent(outerLayout);

		outerLayout.setDefaultComponentAlignment(Alignment.BOTTOM_LEFT);

		outerLayout.addComponent(messageLayout);
		input = new TextField();
		outerLayout.addComponent(input);

		Button button = new Button("Abschicken");
		button.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				addMessage(input.getValue());
				input.setValue("");
			}
		});
		outerLayout.addComponent(button);

		messages = new Label(createLabelText(), ContentMode.TEXT);
		messageLayout.addComponent(messages);
	}

	private String createLabelText() {
		StringBuilder builder = new StringBuilder();
		for (String message: globalState.getMessages()) {
			builder.append(message).append("\n");
		}
		return builder.toString();
	}

	
	
	
	protected void addMessage(String message) {
		sender.sendTextMessage(message);
	}

	@Override
	public void messageAdded(final String message) {
		LOG.debug("received: " + message);
		access(new Runnable() {
			@Override
			public void run() {
				messages.setValue(messages.getValue() + "\n" + message);
				push();
			}
		});
	}

}
