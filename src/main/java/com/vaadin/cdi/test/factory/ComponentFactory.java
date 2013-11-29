package com.vaadin.cdi.test.factory;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import com.vaadin.cdi.test.annotations.ComponentProperties;
import com.vaadin.ui.VerticalLayout;

public class ComponentFactory {

	@Produces
	public VerticalLayout buildLayout(InjectionPoint injectionPoint) {
		VerticalLayout verticalLayout = new VerticalLayout();
		ComponentProperties properties = injectionPoint.getAnnotated()
				.getAnnotation(ComponentProperties.class);
		if (properties != null) {
			setComponentProperties(verticalLayout, properties);
		}
		return verticalLayout;
	}

	private void setComponentProperties(VerticalLayout verticalLayout,
			ComponentProperties properties) {
		verticalLayout.setMargin(properties.margin());
		verticalLayout.addStyleName(properties.additionalStyles());
	}
}
