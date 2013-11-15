package com.vaadin.cdi.test.factory;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import com.vaadin.ui.VerticalLayout;

public class ComponentFactory {

	@Produces
	public VerticalLayout buildLayout(InjectionPoint injectionPoint) {
		VerticalLayout verticalLayout = new VerticalLayout();
		Margin margin = injectionPoint.getAnnotated().getAnnotation(
				Margin.class);
		if (margin != null) {
			verticalLayout.setMargin(true);
		}
		return verticalLayout;
	}
}
