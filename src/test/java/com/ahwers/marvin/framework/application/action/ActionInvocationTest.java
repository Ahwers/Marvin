package com.ahwers.marvin.framework.application.action;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class ActionInvocationTest {

    @Test
    public void validConstruction() {
        assertTrue(new ActionInvocation("Test", "test", Map.of("first_argument", "has a value")) != null);
    }
    
    @Test
    public void invalidConstruction() {
        assertThrows(IllegalArgumentException.class, () -> new ActionInvocation("Test", "test", null));
    }

    @Test
    public void invalidConstructionInvalidArgumentKey() {
        assertThrows(IllegalArgumentException.class, () -> new ActionInvocation("Test", "test", Map.of("illegal argument key", "value")));
    }

    @Test
    public void copyOfArgumentsMapIsKept() {
        Map<String, String> arguments = new HashMap<String, String>(Map.of("first_argument", "first value"));
        ActionInvocation invocation = new ActionInvocation("Test", "test", arguments);
        arguments.put("second_argument", "second value");
        assertFalse(invocation.getArguments().equals(arguments));
    }

    @Test
    public void getArguments() {
        ActionInvocation invocation = new ActionInvocation("Test", "test", Map.of("first_argument", "has a value"));
        Map<String, String> expectedArguments = Map.of("first_argument", "has a value");
        assertEquals(invocation.getArguments(), expectedArguments);
    } 

    @Test
    public void getArgumentsImmutable() {
        ActionInvocation invocation = new ActionInvocation("Test", "test", Map.of("first_argument", "has a value"));
        assertThrows(UnsupportedOperationException.class, () -> invocation.getArguments().put("key", "value"));
    }

    @Test
    public void isSameAsValid() {
        ActionInvocation invocationOne = new ActionInvocation("Test", "test", Map.of("first_argument", "has a value"));
        ActionInvocation invocationTwo = new ActionInvocation("Test", "test", Map.of("first_argument", "has a value"));
        assertTrue(invocationOne.isSameAs(invocationTwo));
    }

    @Test
    public void isSameAsInvalid() {
        ActionInvocation invocation = new ActionInvocation("Test", "test", Map.of("first_argument", "has a value"));
        ApplicationAction action = new ApplicationAction("Test", "test");
        assertThrows(IllegalArgumentException.class, () -> invocation.isSameAs(action));
    }

}
