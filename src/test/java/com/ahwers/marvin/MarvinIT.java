package com.ahwers.marvin;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.rmi.UnexpectedException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.ahwers.marvin.framework.Marvin;
import com.ahwers.marvin.framework.application.Application;
import com.ahwers.marvin.framework.application.action.ActionInvocation;
import com.ahwers.marvin.framework.application.state.ApplicationStateFactory;
import com.ahwers.marvin.framework.response.MarvinResponse;
import com.ahwers.marvin.framework.response.enums.InvocationOutcome;
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
        assertTrue(marvinResponse.getStatus() == InvocationOutcome.SUCCESSFUL);
    }

    @Test
    public void invalidMarvinCommand() {
        Marvin marvin = new Marvin("com.ahwers.marvin.testapplications");
        MarvinResponse marvinResponse = marvin.processCommand("invalid marvin command");
        assertAll(
            () -> assertTrue(marvinResponse.getStatus() == InvocationOutcome.INVALID),
            () -> assertTrue(marvinResponse.getFailException().getClass() == UnexpectedException.class)
        );
    }

    @Test
    public void failedMarvinCommand() {
        Marvin marvin = new Marvin("com.ahwers.marvin.testapplications");
        MarvinResponse marvinResponse = marvin.processCommand("failed marvin command");
        assertAll(
            () -> assertTrue(marvinResponse.getStatus() == InvocationOutcome.FAILED),
            () -> assertTrue(marvinResponse.getFailException().getClass() == IllegalArgumentException.class)
        );
    }

    @Test
    public void unmatchedMarvinCommand() {
        Marvin marvin = new Marvin("com.ahwers.marvin.testapplications");
        MarvinResponse marvinResponse = marvin.processCommand("unmatched marvin command");
        assertTrue(marvinResponse.getStatus() == InvocationOutcome.UNMATCHED);
    }

    @Test
    public void conflictedMarvinCommand() {
        Marvin marvin = new Marvin("com.ahwers.marvin.testapplications");
        MarvinResponse marvinResponse = marvin.processCommand("conflicted marvin command");
        assertTrue(marvinResponse.getStatus() == InvocationOutcome.CONFLICTED);
    }

    @Test
    public void successfulMarvinInvocation() {
        Marvin marvin = new Marvin("com.ahwers.marvin.testapplications");
        MarvinResponse marvinResponse = marvin.processActionInvocation(new ActionInvocation("Test Application", "successfulMarvinCommand", Map.of()));
        assertTrue(marvinResponse.getStatus() == InvocationOutcome.SUCCESSFUL);
    }

    @Test
    public void invalidMarvinInvocation() {
        Marvin marvin = new Marvin("com.ahwers.marvin.testapplications");
        MarvinResponse marvinResponse = marvin.processActionInvocation(new ActionInvocation("Test Application", "invalidMarvinCommand", Map.of()));
        assertAll(
            () -> assertTrue(marvinResponse.getStatus() == InvocationOutcome.INVALID),
            () -> assertTrue(marvinResponse.getFailException().getClass() == UnexpectedException.class)
        );
    }

    @Test
    public void failedMarvinInvocation() {
        Marvin marvin = new Marvin("com.ahwers.marvin.testapplications");
        MarvinResponse marvinResponse = marvin.processActionInvocation(new ActionInvocation("Test Application", "failedMarvinCommand", Map.of()));
        assertAll(
            () -> assertTrue(marvinResponse.getStatus() == InvocationOutcome.FAILED),
            () -> assertTrue(marvinResponse.getFailException().getClass() == IllegalArgumentException.class)
        );
    }

    @Test
    public void unmatchedMarvinInvocation() {
        Marvin marvin = new Marvin("com.ahwers.marvin.testapplications");
        MarvinResponse marvinResponse = marvin.processActionInvocation(new ActionInvocation("Test Application", "nonExistentMarvinCommand", Map.of()));
        assertTrue(marvinResponse.getStatus() == InvocationOutcome.UNMATCHED);
    }

    @Test
    public void updateApplicationStates() {
        Marvin marvin = new Marvin("com.ahwers.marvin.testapplications");

        MarvinResponse beforeResponse = marvin.processCommand("get app state");
        String beforeState = beforeResponse.getResource().getContent();

        marvin.processCommand("set app state new_test");
        MarvinResponse afterResponse = marvin.processCommand("get app state");
        String afterState = afterResponse.getResource().getContent();

        assertAll(
            () -> assertTrue(beforeState.equals("test")),
            () -> assertTrue(afterState.equals("new_test"))
        );
    }

}
