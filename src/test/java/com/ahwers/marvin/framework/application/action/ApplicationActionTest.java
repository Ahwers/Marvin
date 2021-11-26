package com.ahwers.marvin.framework.application.action;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ApplicationActionTest {
    
    private ApplicationAction testValidAction = new ApplicationAction("Test", "test");

    @Test
    public void validConstruction() {
        assertTrue(testValidAction != null);
    }

    @Test
    public void getApplicationName() {
        assertEquals("Test", testValidAction.getApplicationName());
    }

    @Test
    public void getActionName() {
        assertEquals("test", testValidAction.getActionName());
    }

    @Test
    public void isSameAs() {
        ApplicationAction expectedAction = new ApplicationAction("Test", "test");
        assertTrue(testValidAction.isSameAs(expectedAction));
    }

    @Test
    public void invalidConstructionNoAppName() {
        assertThrows(IllegalArgumentException.class, () -> new ApplicationAction(null, "test"));
    }

    @Test
    public void invalidConstructionNoActionName() {
        assertThrows(IllegalArgumentException.class, () -> new ApplicationAction("Test", null));
    }

}
