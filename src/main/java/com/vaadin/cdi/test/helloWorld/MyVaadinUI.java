package com.vaadin.cdi.test.helloWorld;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.annotations.Push;
import com.vaadin.cdi.CDIUI;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.label.ContentMode;
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
	private UIState uiState;
	@Inject
	private MessageSender sender;
	private VerticalLayout layout;
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
		layout = new VerticalLayout();
		layout.setMargin(true);
		setContent(layout);

		input = new TextField();
		layout.addComponent(input);

		Button button = new Button("Abschicken");
		button.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				addMessage(input.getValue());
				input.setValue("");
			}
		});
		layout.addComponent(button);

		messages = new Label(createLabelText(), ContentMode.TEXT);
		layout.addComponent(messages);
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
