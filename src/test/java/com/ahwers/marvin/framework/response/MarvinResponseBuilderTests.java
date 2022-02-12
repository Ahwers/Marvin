package com.ahwers.marvin.framework.response;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import com.ahwers.marvin.framework.application.action.ActionInvocation;
import com.ahwers.marvin.framework.application.resource.ApplicationResource;
import com.ahwers.marvin.framework.application.resource.enums.ResourceRepresentationType;
import com.ahwers.marvin.framework.response.enums.InvocationOutcome;
import com.ahwers.marvin.framework.response.exceptions.ApplicationInvocationException;

import org.junit.jupiter.api.Test;

public class MarvinResponseBuilderTests {

    @Test
    public void successfulInvocation() {
        MarvinResponseBuilder responseBuilder = new MarvinResponseBuilder();
        ApplicationResource appResource = new ApplicationResource(ResourceRepresentationType.PLAIN_TEXT, "Works", "Success message");
        MarvinResponse response = responseBuilder.buildResponseForApplicationResource(appResource);
        assertAll(
            () -> assertTrue(response.getMessage().equals(appResource.getMessage())),
            () -> assertTrue(response.getStatus() == InvocationOutcome.SUCCESSFUL),
            () -> assertTrue(response.getResource() == appResource),
            () -> assertTrue(response.getFailException() == null),
            () -> assertTrue(response.getJsonAppState() == null)
        );
    }

    @Test
    public void failedInvocation() {
        MarvinResponseBuilder responseBuilder = new MarvinResponseBuilder();
        IllegalArgumentException expectedException = new IllegalArgumentException();
        InvocationTargetException invocationException = new InvocationTargetException(expectedException);
        MarvinResponse response = responseBuilder.buildResponseForInvocationException(invocationException);
        assertAll(
            () -> assertTrue(response.getMessage().equals("Something went wrong with my framework.")),
            () -> assertTrue(response.getStatus() == InvocationOutcome.FAILED),
            () -> assertTrue(response.getResource() == null),
            () -> assertTrue(response.getFailException() == expectedException),
            () -> assertTrue(response.getJsonAppState() == null)
        );
    }   
    
    @Test
    public void invalidInvocationWithProvidedErrorMessage() {
        MarvinResponseBuilder responseBuilder = new MarvinResponseBuilder();
        ApplicationInvocationException expectedException = new ApplicationInvocationException("I could not process that command because this is a test for invalid application usage with a custom error message.");
        InvocationTargetException invocationException = new InvocationTargetException(expectedException);
        MarvinResponse response = responseBuilder.buildResponseForInvocationException(invocationException);
        assertAll(
            () -> assertTrue(response.getMessage().equals("I could not process that command because this is a test for invalid application usage with a custom error message.")),
            () -> assertTrue(response.getStatus() == InvocationOutcome.INVALID),
            () -> assertTrue(response.getResource() == null),
            () -> assertTrue(response.getFailException() == expectedException),
            () -> assertTrue(response.getJsonAppState() == null)
        );
    }

    // TODO: Should rename ApplicationInvocationException because it is very similar to InvocationTargetException
    @Test
    public void invalidInvocationWithoutCustomErrorMessage() {
        MarvinResponseBuilder responseBuilder = new MarvinResponseBuilder();
        ApplicationInvocationException expectedException = new ApplicationInvocationException();
        InvocationTargetException invocationException = new InvocationTargetException(expectedException);
        MarvinResponse response = responseBuilder.buildResponseForInvocationException(invocationException);
        assertAll(
            () -> assertTrue(response.getMessage().equals("The invocation of this command was erroneous for some reason.")),
            () -> assertTrue(response.getStatus() == InvocationOutcome.INVALID),
            () -> assertTrue(response.getResource() == null),
            () -> assertTrue(response.getFailException() == expectedException),
            () -> assertTrue(response.getJsonAppState() == null)
        );
    }

    @Test
    public void conflictedCommandResponse() {
        MarvinResponseBuilder responseBuilder = new MarvinResponseBuilder();
        List<ActionInvocation> conflictingActions = List.of(
            new ActionInvocation("Test application", "firstAction", Map.of()),
            new ActionInvocation("Another test application", "secondAction", Map.of())
        );
        String expectedContent = (conflictingActions.get(0).toString() + "\n" + conflictingActions.get(1).toString() + "\n");
        ApplicationResource expectedResource = new ApplicationResource(ResourceRepresentationType.COMMAND_OPTION_LIST, expectedContent);
        MarvinResponse response = responseBuilder.buildResponseForConflictingActions(conflictingActions);
        assertAll(
            () -> assertTrue(response.getMessage().equals("Please be more specific.")),
            () -> assertTrue(response.getStatus() == InvocationOutcome.CONFLICTED),
            () -> assertTrue(response.getResource().getContent().equals(expectedResource.getContent())),
            () -> assertTrue(response.getFailException() == null),
            () -> assertTrue(response.getJsonAppState() == null)
        );
    }

    @Test
    public void unmatchedCommandResponse() {
        MarvinResponseBuilder responseBuilder = new MarvinResponseBuilder();
        MarvinResponse response = responseBuilder.buildResponseForUnmatchedCommand("Unmatched command");
        assertAll(
            () -> assertTrue(response.getMessage().equals("No functionality matched the command 'Unmatched command'.")),
            () -> assertTrue(response.getStatus() == InvocationOutcome.UNMATCHED),
            () -> assertTrue(response.getResource() == null),
            () -> assertTrue(response.getFailException() == null),
            () -> assertTrue(response.getJsonAppState() == null)
        );
    }

}
