package com.ahwers.marvin;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.ahwers.marvin.framework.Marvin;
import com.ahwers.marvin.framework.application.Application;
import com.ahwers.marvin.framework.application.action.ActionInvocation;
import com.ahwers.marvin.framework.application.state.ApplicationStateFactory;
import com.ahwers.marvin.framework.resource.ResourceRepresentationType;
import com.ahwers.marvin.framework.response.InvocationOutcome;
import com.ahwers.marvin.framework.response.MarvinResponse;
import com.ahwers.marvin.testapplications.TestApplication;

import org.junit.jupiter.api.Test;

public class MarvinIT {

    @Test
    public void validMarvinInstantiation() {
        assertTrue(new Marvin("com.ahwers.marvin.testapplications") != null);
    }

    @Test
    public void invalidNoApplicationsPackageArgument() {
        assertThrows(IllegalArgumentException.class, () -> new Marvin(null));
    }

    @Test
    public void getApplicationStateFactory() {
        Marvin marvin = new Marvin("com.ahwers.marvin.testapplications");

        Set<Class<? extends Application>> expectedApplicationClasses = Set.of(TestApplication.class);
        
        ApplicationStateFactory factory = marvin.getApplicationStateFactory();
        
        Set<Class<? extends Application>> actualApplicationClasses = new HashSet<>();
        for (Application app : factory.getSupportedApplications()) {
            actualApplicationClasses.add(app.getClass());
        }

        assertAll(
            () -> assertTrue(expectedApplicationClasses.size() == actualApplicationClasses.size()),
            () -> {
                for (Class<?> appClass : expectedApplicationClasses) {
                    assertTrue(actualApplicationClasses.contains(appClass));
                }
            }
        );
    }

    @Test
    public void successfulMarvinCommand() {
        Marvin marvin = new Marvin("com.ahwers.marvin.testapplications");
        MarvinResponse marvinResponse = marvin.processCommand("successful marvin command");
        assertTrue(marvinResponse.getCommandStatus() == InvocationOutcome.SUCCESSFUL);
    }

    @Test
    public void invalidMarvinCommand() {
        Marvin marvin = new Marvin("com.ahwers.marvin.testapplications");
        MarvinResponse marvinResponse = marvin.processCommand("invalid marvin command");
        assertTrue(marvinResponse.getCommandStatus() == InvocationOutcome.INVALID);
    }

    @Test
    public void failedMarvinCommand() {
        Marvin marvin = new Marvin("com.ahwers.marvin.testapplications");
        MarvinResponse marvinResponse = marvin.processCommand("failed marvin command");
        assertTrue(marvinResponse.getCommandStatus() == InvocationOutcome.FAILED);
    }

    @Test
    public void unmatchedMarvinCommand() {
        Marvin marvin = new Marvin("com.ahwers.marvin.testapplications");
        MarvinResponse marvinResponse = marvin.processCommand("unmatched marvin command");
        assertTrue(marvinResponse.getCommandStatus() == InvocationOutcome.UNMATCHED);
    }

    @Test
    public void conflictedMarvinCommand() {
        Marvin marvin = new Marvin("com.ahwers.marvin.testapplications");
        MarvinResponse marvinResponse = marvin.processCommand("conflicted marvin command");
        assertTrue(marvinResponse.getCommandStatus() == InvocationOutcome.CONFLICTED);
    }

    @Test
    public void successfulMarvinInvocation() {
        Marvin marvin = new Marvin("com.ahwers.marvin.testapplications");
        MarvinResponse marvinResponse = marvin.processActionInvocation(new ActionInvocation("Test Application", "successfulAction", Map.of()));
        assertTrue(marvinResponse.getCommandStatus() == InvocationOutcome.SUCCESSFUL);
    }

    @Test
    public void invalidMarvinInvocation() {
        Marvin marvin = new Marvin("com.ahwers.marvin.testapplications");
        MarvinResponse marvinResponse = marvin.processActionInvocation(new ActionInvocation("Test Application", "invalidAction", Map.of()));
        assertTrue(marvinResponse.getCommandStatus() == InvocationOutcome.INVALID);
    }

    @Test
    public void failedMarvinInvocation() {
        Marvin marvin = new Marvin("com.ahwers.marvin.testapplications");
        MarvinResponse marvinResponse = marvin.processActionInvocation(new ActionInvocation("Test Application", "failedAction", Map.of()));
        assertTrue(marvinResponse.getCommandStatus() == InvocationOutcome.FAILED);
    }

    @Test
    public void unmatchedMarvinInvocation() {
        Marvin marvin = new Marvin("com.ahwers.marvin.testapplications");
        MarvinResponse marvinResponse = marvin.processActionInvocation(new ActionInvocation("Test Application", "unmatchedAction", Map.of()));
        assertTrue(marvinResponse.getCommandStatus() == InvocationOutcome.UNMATCHED);
    }

    @Test
    public void conflictedMarvinInvocation() {
        Marvin marvin = new Marvin("com.ahwers.marvin.testapplications");
        MarvinResponse marvinResponse = marvin.processActionInvocation(new ActionInvocation("Test Application", "conflictedAction", Map.of()));
        assertTrue(marvinResponse.getCommandStatus() == InvocationOutcome.CONFLICTED);
    }

    @Test
    public void updateApplicationStates() {
        Marvin marvin = new Marvin("com.ahwers.marvin.testapplications");

        MarvinResponse beforeResponse = marvin.processCommand("get app state");
        String beforeState = beforeResponse.getResource().getMessage(ResourceRepresentationType.PLAIN_TEXT);

        marvin.processCommand("set app state new_test");
        MarvinResponse afterResponse = marvin.processCommand("get app state");
        String afterState = afterResponse.getResource().getMessage(ResourceRepresentationType.PLAIN_TEXT);

        assertAll(
            () -> assertFalse(beforeState.equals(afterState)),
            () -> assertTrue(afterResponse.equals("new_test"))
        );
    }

}
