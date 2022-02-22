package com.ahwers.marvin.framework.application;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import com.ahwers.marvin.framework.application.action.ActionDefinition;
import com.ahwers.marvin.framework.application.action.annotations.CommandMatch;
import com.ahwers.marvin.framework.application.annotations.IntegratesApplication;
import com.ahwers.marvin.framework.application.annotations.Stateful;
import com.ahwers.marvin.framework.application.exceptions.ApplicationConfigurationError;
import com.ahwers.marvin.framework.application.resource.ApplicationResource;
import com.ahwers.marvin.framework.application.state.ApplicationState;
import com.ahwers.marvin.testapplications.TestApplicationState;

import org.junit.jupiter.api.Test;

public class ApplicationTests {

    @IntegratesApplication("Test")
    @Stateful(TestApplicationState.class)
    private class StandardApplication extends Application {

        @CommandMatch("one match")
        public ApplicationResource actionOne(Map<String, String> arguments) {
            return null;
        }

        @CommandMatch("one match")
        @CommandMatch("two match")
        public ApplicationResource actionTwo(Map<String, String> arguments) {
            return null;
        }

    }

    @Test
    public void validApplicationWithActions() {
        assertTrue(new StandardApplication() != null);
    }

    @IntegratesApplication("Test")
    @Stateful(TestApplicationState.class)
    private class ApplicationWithoutActions extends Application {

    }

    @Test
    public void validApplicationNoActions() {
        assertTrue(new ApplicationWithoutActions() != null);
    }

    @IntegratesApplication("Test")
    private class StatelessApplication extends Application {

        @CommandMatch("one match")
        public ApplicationResource actionOne(Map<String, String> arguments) {
            return null;
        }

    }

    @Test
    public void validApplicationWithoutState() {
        assertTrue(new StatelessApplication() != null);
    }

    private class IncorrectlyConfiguredApplicationNoIntegrationAnnotation extends Application {

    }
    
    @Test
    public void invalidApplicationNoIntegrationAnnotation() {
        assertThrows(ApplicationConfigurationError.class, () -> new IncorrectlyConfiguredApplicationNoIntegrationAnnotation());
    }

    @IntegratesApplication("Test")
    private class IncorrectlyConfiguredApplicationInvalidReturnActionMethod extends Application {

        @CommandMatch("test command")
        public String invalidAction(Map<String, String> arguments) {
            return null;
        }

    }

    @Test
    public void invalidApplicationActionInvalidReturn() {
        assertThrows(ApplicationConfigurationError.class, () -> new IncorrectlyConfiguredApplicationInvalidReturnActionMethod());
    }

    @IntegratesApplication("Test")
    private class IncorrectlyConfiguredApplicationVoidReturnActionMethod extends Application {

        @CommandMatch("test command")
        public void invalidAction(Map<String, String> arguments) {}

    }

    @Test
    public void invalidApplicationActionVoidReturn() {
        assertThrows(ApplicationConfigurationError.class, () -> new IncorrectlyConfiguredApplicationVoidReturnActionMethod());
    }

    @IntegratesApplication("Test")
    private class IncorrectlyConfiguredApplicationNoArguments extends Application {

        @CommandMatch("test command")
        public ApplicationResource invalidAction() {
            return null;
        }

    }

    @Test
    public void invalidApplicationActionNoArguments() {
        assertThrows(ApplicationConfigurationError.class, () -> new IncorrectlyConfiguredApplicationNoArguments());
    }

    @IntegratesApplication("Test")
    private class IncorrectlyConfiguredApplicationInvalidActionMatchRegex extends Application {

        @CommandMatch("(?<invalid key>test) command")
        public ApplicationResource invalidAction(Map<String, String> arguments) {
            return null;
        }

    }

    @Test
    public void invalidApplicationActionInvalidMatchRegex() {
        assertThrows(ApplicationConfigurationError.class, () -> new IncorrectlyConfiguredApplicationInvalidActionMatchRegex());
    }

    @IntegratesApplication("Test")
    private class IncorrectlyConfiguredApplicationInvalidMapArgument extends Application {

        @CommandMatch("test command")
        public ApplicationResource invalidAction(Map<Integer, Double> arguments) {
            return null;
        }

    }

    @Test
    public void invalidApplicationActionInvalidMapArgument() {
        assertThrows(ApplicationConfigurationError.class, () -> new IncorrectlyConfiguredApplicationInvalidMapArgument());
    }

    @IntegratesApplication("Test")
    private class IncorrectlyConfiguredApplicationWrongArguments extends Application {

        @CommandMatch("test command")
        public ApplicationResource invalidAction(String argument) {
            return null;
        }

    }

    @Test
    public void invalidApplicationActionWrongArguments() {
        assertThrows(ApplicationConfigurationError.class, () -> new IncorrectlyConfiguredApplicationWrongArguments());
    }

    @IntegratesApplication("Test")
    private class IncorrectlyConfiguredApplicationTooManyArguments extends Application {

        @CommandMatch("test command")
        public ApplicationResource invalidAction(Map<String, String> arguments, String argument) {
            return null;
        }

    }

    @Test
    public void invalidApplicationActionTooManyArguments() {
        assertThrows(ApplicationConfigurationError.class, () -> new IncorrectlyConfiguredApplicationTooManyArguments());
    }

    @IntegratesApplication("Test")
    private class IncorrectlyConfiguredApplicationPrivateActionMethod extends Application {

        @CommandMatch("test command")
        private ApplicationResource invalidAction(Map<String, String> arguments) {
            return null;
        }

    }

    @Test
    public void invalidApplicationPrivateAction() {
        assertThrows(ApplicationConfigurationError.class, () -> new IncorrectlyConfiguredApplicationPrivateActionMethod());
    }

    @Test
    public void nameDetection() {
        Application app = new StandardApplication();
        assertEquals(app.getName(), "Test");
    }

    @Test
    public void actionDetection() {
        Application app = new StandardApplication();
        assertAll(
            () -> assertTrue(app.getActions().size() == 2),
            () -> assertTrue(app.getActions().get(0).getActionName().equals("actionOne")),
            () -> assertTrue(app.getActions().get(1).getActionName().equals("actionTwo"))
        );
    }

    @Test
    public void actionConstructionSingleCommandMatch() {
        ActionDefinition expectedAction = new ActionDefinition("Test", "actionOne", List.of("one match"));
        Application app = new StandardApplication();
        assertTrue(app.getActions().get(0).isSameAs(expectedAction));
    }
    
    @Test
    public void actionConstructionMultiCommandMatch() {
        ActionDefinition expectedAction = new ActionDefinition("Test", "actionTwo", List.of("one match", "two match"));
        Application app = new StandardApplication();
        assertTrue(app.getActions().get(1).isSameAs(expectedAction));
    }

    @Test
    public void getState() {
        ApplicationState expectedState = new TestApplicationState("Test", 0);
        Application app = new StandardApplication();
        app.setState(expectedState);
        assertTrue(app.getState().isSameAs(expectedState));
    }

    @Test
    public void storesCopiesOfStates() {
        Application app = new StandardApplication();
        ApplicationState state = new TestApplicationState("Test", 0);
        app.setState(state);
        assertFalse(app.getState() == state);
    }

    @Test
    public void returnsCopiesOfStates() {
        Application app = new StandardApplication();
        ApplicationState state = new TestApplicationState("Test", 0);
        app.setState(state);
        assertFalse(app.getState() == app.getState());
    }

    @Test
    public void getStateClass() {
        Class<? extends ApplicationState> expectedStateClass = TestApplicationState.class;
        Application app = new StandardApplication();
        assertTrue(app.getStateClass().equals(expectedStateClass));
    }

    @Test
    public void setState() {
        TestApplicationState expectedState = new TestApplicationState("Test", 0);
        expectedState.setTest("works");
        Application app = new StandardApplication();
        app.setState(expectedState);;
        assertTrue(app.getState().isSameAs(expectedState));
    }

    @Test
    public void getName() {
        Application app = new StandardApplication();
        assertTrue(app.getName().equals("Test"));
    }

    @Test
    public void getActionsImmutable() {
        Application app = new StandardApplication();
        List<ActionDefinition> actions = app.getActions();
        assertThrows(UnsupportedOperationException.class, () -> actions.add(actions.get(0)));
    }

    @Test
    public void returnsCopiesOfActions() {
        Application app = new StandardApplication();
        List<ActionDefinition> actions1 = app.getActions();
        List<ActionDefinition> actions2 = app.getActions();
        assertFalse(actions1.get(0) == actions2.get(0));
    }

}
