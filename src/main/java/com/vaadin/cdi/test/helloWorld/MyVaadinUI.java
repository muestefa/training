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
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.TextArea;
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
	@Inject
	@Margin
	private VerticalLayout inputLayout;

	private TextField input;

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
		outerLayout.setSizeFull();

		outerLayout.addComponent(messageLayout);
		outerLayout.addComponent(inputLayout);
		outerLayout.setExpandRatio(messageLayout, 1.0f);

		createInput();

		messageLayout.setSizeFull();
		VerticalLayout dummyLayout = new VerticalLayout();
		messageLayout.addComponent(dummyLayout);
		messageLayout.setExpandRatio(dummyLayout, 1.0f);
		rebuildMessages();
	}

	private void createInput() {
		input = new TextField();
		Button button = new Button("Abschicken");
		button.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				addMessage(input.getValue());
				input.setValue("");
			}
		});
		inputLayout.addComponent(input);
		inputLayout.addComponent(button);
	}

	private String rebuildMessages() {
		StringBuilder builder = new StringBuilder();
		for (String message: globalState.getMessages()) {
			addMessageToView(message);
		}
		return builder.toString();
	}

	private void addMessageToView(String message) {
		TextArea area = new TextArea();
		area.setValue(message);
		area.setReadOnly(true);
		messageLayout.addComponent(area);
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
				addMessageToView(message);
				push();
			}
		});
	}

}
