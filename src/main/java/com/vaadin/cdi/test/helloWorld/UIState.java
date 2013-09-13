package com.vaadin.cdi.test.helloWorld;

import java.io.Serializable;

import com.vaadin.cdi.UIScoped;


@UIScoped
public class UIState implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int numberInvocations;

	public int numberInvocations() {
		return numberInvocations++;
	}

}
