package com.ahwers.marvin.framework.application.action;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ApplicationActionTest {

    private class ApplicationActionImplementation extends ApplicationAction {

        public ApplicationActionImplementation(String appName, String actionName) {
            super(appName, actionName);
        }
        
    }
    
    private ApplicationAction testValidAction = new ApplicationActionImplementation("Test", "test");

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
        ApplicationAction expectedAction = new ApplicationActionImplementation("Test", "test");
        assertTrue(testValidAction.isSameAs(expectedAction));
    }

    @Test
    public void invalidConstructionNoAppName() {
        assertThrows(IllegalArgumentException.class, () -> new ApplicationActionImplementation(null, "test"));
    }

    @Test
    public void invalidConstructionNoActionName() {
        assertThrows(IllegalArgumentException.class, () -> new ApplicationActionImplementation("Test", null));
    }

}
