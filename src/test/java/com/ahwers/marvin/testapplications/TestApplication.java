package com.ahwers.marvin.testapplications;

import java.rmi.UnexpectedException;
import java.util.Map;

import com.ahwers.marvin.framework.application.Application;
import com.ahwers.marvin.framework.application.action.annotations.CommandMatch;
import com.ahwers.marvin.framework.application.annotations.IntegratesApplication;
import com.ahwers.marvin.framework.application.annotations.Stateful;
import com.ahwers.marvin.framework.application.resource.ApplicationResource;
import com.ahwers.marvin.framework.application.resource.enums.ResourceRepresentationType;
import com.ahwers.marvin.framework.application.state.TestApplicationState;

@IntegratesApplication("Test Application")
@Stateful(TestApplicationState.class)
public class TestApplication extends Application {

    @CommandMatch("successful marvin command")
    public ApplicationResource successfulMarvinCommand(Map<String, String> arguments) {
        return new ApplicationResource(ResourceRepresentationType.PLAIN_TEXT, "successful");
    }

    @CommandMatch("invalid marvin command")
    public ApplicationResource invalidMarvinCommand(Map<String, String> arguments) throws UnexpectedException {
        throw new UnexpectedException("This action cannot be invoked like that.");
    }

    @CommandMatch("failed marvin command")
    public ApplicationResource failedMarvinCommand(Map<String, String> arguments) throws NoSuchMethodException {
        throw new IllegalArgumentException();
    }

    @CommandMatch("conflicted marvin command")
    public ApplicationResource conflictedMarvinCommandOne(Map<String, String> arguments) {
        return new ApplicationResource(ResourceRepresentationType.PLAIN_TEXT, "conflicted");
    }

    @CommandMatch("conflicted marvin command")
    public ApplicationResource conflictedMarvinCommandTwo(Map<String, String> arguments) {
        return new ApplicationResource(ResourceRepresentationType.PLAIN_TEXT, "conflicted");
    }

    @CommandMatch("get app state")
    public ApplicationResource getAppState(Map<String, String> arguments) {
        TestApplicationState appState = (TestApplicationState) this.getState();
        return new ApplicationResource(ResourceRepresentationType.PLAIN_TEXT, appState.getTest());
    }

    @CommandMatch("set app state (?<newState>.+)")
    public ApplicationResource setAppState(Map<String, String> arguments) {
        TestApplicationState appState = (TestApplicationState) this.getState();
        String newState = arguments.get("newState");
        appState.setTest(newState);
        this.setState(appState);

        return null;
    }

}
