package com.ahwers.marvin.framework.application.action;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ahwers.marvin.framework.application.action.ActionDefinition;
import com.ahwers.marvin.framework.application.action.ActionInvocation;

import org.junit.jupiter.api.Test;

public class ActionDefinitionTest {

    @Test
    public void successfulRequestMatchSingleTest() {
        ActionDefinition action = new ActionDefinition("Test", "test", List.of("to match"));
        String request = "to match";
        assertTrue(action.canServiceCommandRequest(request));
    }

    @Test
    public void successfulRequestMatchMultipleTest() {
        ActionDefinition action = new ActionDefinition("Test", "test", List.of("not to match", "to match"));
        String request = "to match";
        assertTrue(action.canServiceCommandRequest(request));
    }

    @Test
    public void successfulRequestFilterRequestIsSubsetOfCommand() {
        ActionDefinition action = new ActionDefinition("Test", "test", List.of("not to match"));
        String request = "to match";
        assertFalse(action.canServiceCommandRequest(request));
    }

    @Test
    public void failedRequestMatchTest() {
        ActionDefinition action = new ActionDefinition("Test", "test", List.of("not to match"));
        String request = "to match";
        assertFalse(action.canServiceCommandRequest(request));
    }

    @Test
    public void validConversionToInvocationNoArguments() {
        ActionDefinition action = new ActionDefinition("Test", "test", List.of("to match"));
        
        ActionInvocation expectedInvocation = new ActionInvocation("Test", "test", new HashMap<String, String>());
        ActionInvocation actualInvocation = action.buildActionInvocationForCommandRequest("to match");

        assertTrue(actualInvocation.isSameAs(expectedInvocation));
    }

    @Test
    public void validConversionToInvocationWithArguments() {
        ActionDefinition action = new ActionDefinition("Test", "test", List.of("(?<first>to) (?<second>match)"));

        ActionInvocation expectedInvocation = new ActionInvocation("Test", "test", Map.of("first", "to", "second", "match"));
        ActionInvocation actualInvocation = action.buildActionInvocationForCommandRequest("to match");

        assertTrue(actualInvocation.isSameAs(expectedInvocation));
    }

    @Test
    public void invalidConversionToInvocationNoMatch() {
        ActionDefinition action = new ActionDefinition("Test", "test", List.of("not to match"));
        String request = "to match";
        assertThrows(IllegalArgumentException.class, () -> action.buildActionInvocationForCommandRequest(request));
    }

}
