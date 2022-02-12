package com.ahwers.marvin.framework.application.resource;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ahwers.marvin.framework.application.resource.enums.ResourceRepresentationType;

import org.junit.jupiter.api.Test;

public class ApplicationResourceTests {
    
    @Test
    public void validInstantiationNoMessage() {
        assertTrue(new ApplicationResource(ResourceRepresentationType.HTML_APPLICATION_INTERFACE, "<html></html>") != null);
    }

    @Test
    public void validInstantiationWithMessage() {
        assertTrue(new ApplicationResource(ResourceRepresentationType.HTML_APPLICATION_INTERFACE, "<html></html>", "message") != null);
    }

    @Test
    public void invalidInstantiationNoType() {
        assertThrows(IllegalArgumentException.class, () -> new ApplicationResource(null, "string"));
    }

    @Test
    public void invalidInstantiationNoContent() {
        assertThrows(IllegalArgumentException.class, ()-> new ApplicationResource(ResourceRepresentationType.HTML_APPLICATION_INTERFACE, null));
    }

    @Test
    public void getType() {
        ApplicationResource appResource = new ApplicationResource(ResourceRepresentationType.HTML_APPLICATION_INTERFACE, "<html></html>");
        assertTrue(appResource.getType() == ResourceRepresentationType.HTML_APPLICATION_INTERFACE);
    }

    @Test
    public void getContent() {
        ApplicationResource appResource = new ApplicationResource(ResourceRepresentationType.HTML_APPLICATION_INTERFACE, "<html></html>");
        assertTrue(appResource.getContent().equals("<html></html>"));
    }

    @Test
    public void getMessage() {
        ApplicationResource appResource = new ApplicationResource(ResourceRepresentationType.HTML_APPLICATION_INTERFACE, "<html></html>", "message");
        assertTrue(appResource.getMessage().equals("message"));
    }

}
