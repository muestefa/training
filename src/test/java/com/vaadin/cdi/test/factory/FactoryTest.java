package com.vaadin.cdi.test.factory;

import org.jglue.cdiunit.CdiRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

@RunWith(CdiRunner.class)
public class FactoryTest {

    @Inject
    private ComponentFactory componentFactory;

    @Test
    public void injectFactory() {
        Assert.assertNotNull(componentFactory);
    }
}
