package com.vaadin.cdi.test.helloWorld;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.annotations.Push;
import com.vaadin.cdi.CDIUI;
import com.vaadin.cdi.test.annotations.ComponentProperties;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.AbstractTextField.TextChangeEventMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
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
	@ComponentProperties(margin = false)
	private VerticalLayout outerLayout;
	@Inject
	@ComponentProperties(margin = false, additionalStyles = "message")
	private VerticalLayout messageLayout;
	@Inject
	@ComponentProperties(margin = false, additionalStyles = "messageinput")
	private VerticalLayout inputLayout;


	private Panel messagePanel = new Panel();

	private TextField input;

	private Button addButton;

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
		messagePanel.setContent(messageLayout);
		messagePanel.setStyleName("panel");
		messagePanel.setHeight("100%");
		outerLayout.addComponent(messagePanel);
		outerLayout.addComponent(inputLayout);
		outerLayout.setExpandRatio(messagePanel, 1.0f);

		createInput();

		VerticalLayout dummyLayout = new VerticalLayout();
		messageLayout.addComponent(dummyLayout);
		messageLayout.setExpandRatio(dummyLayout, 1.0f);
		rebuildMessages();
	}

	private void createInput() {
		input = new TextField();
		addButton = new Button("Abschicken");
		addButton.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				addMessage(input.getValue());
				input.setValue("");
				addButton.setEnabled(false);
				input.focus();
			}
		});
		addButton.setEnabled(false);
		inputLayout.addComponent(input);
		inputLayout.addComponent(addButton);
		input.setTextChangeEventMode(TextChangeEventMode.EAGER);
		input.addTextChangeListener(new FieldEvents.TextChangeListener() {
			@Override
			public void textChange(TextChangeEvent event) {
				if(event.getText().trim().isEmpty()) {
					addButton.setEnabled(false);
				} else {
					addButton.setEnabled(true);
				}
			}
		});
	}

	private String rebuildMessages() {
		StringBuilder builder = new StringBuilder();
		for (BoardMessage message: globalState.getMessages()) {
			addMessageToView(message.getMessage(), message.getSenderId());
		}
		return builder.toString();
	}

	private void addMessageToView(String message, String senderID) {
		HorizontalLayout messageBox = new HorizontalLayout();
		messageBox.setMargin(true);
		TextArea area = new TextArea();
		area.setValue(message);
		area.setReadOnly(true);
		messageBox.addComponent(new Label(senderID + ": "));
		messageBox.addComponent(area);
		messageLayout.addComponent(messageBox);
		messagePanel.setScrollTop(1000000);
	}

	protected void addMessage(String message) {
		sender.sendTextMessage(message);
	}

	@Override
	public void messageAdded(final BoardMessage message) {
		LOG.debug("received: " + message.getMessage());
		access(new Runnable() {
			@Override
			public void run() {
				addMessageToView(message.getMessage(), message.getSenderId());
				push();
			}
		});
	}

}
