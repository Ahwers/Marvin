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
import com.ahwers.marvin.framework.application.exceptions.ApplicationConfigurationError;
import com.ahwers.marvin.framework.resource.MarvinApplicationResource;

import org.junit.jupiter.api.Test;

public class ApplicationTest {
    
    @IntegratesApplication("Test")
    private class StandardApplication extends Application {

        @Override
        protected ApplicationState instantiateState() {
            return new TestApplicationState();
        }

        @CommandMatch("one match")
        public MarvinApplicationResource actionOne(Map<String, String> arguments) {
            return null;
        }

        @CommandMatch("one match")
        @CommandMatch("two match")
        public MarvinApplicationResource actionTwo(Map<String, String> arguments) {
            return null;
        }

    }

    private class TestApplicationState extends ApplicationState {

        private String test = "test";

        @Override
        public boolean isSameAs(ApplicationState appState) {
            TestApplicationState castedApplicationState = (TestApplicationState) appState;
            return castedApplicationState.getTest().equals(this.test);
        }

        public String getTest() {
            return this.test;
        }

        public void setTest(String test) {
            this.test = test;
        }

    }
    
    @Test
    public void validApplicationWithActions() {
        assertTrue(new StandardApplication() != null);
    }

    @IntegratesApplication("Test")
    private class ApplicationWithoutActions extends Application {

        @Override
        protected ApplicationState instantiateState() {
            return null;
        }

    }

    @Test
    public void validApplicationNoActions() {
        assertTrue(new ApplicationWithoutActions() != null);
    }

    @IntegratesApplication("Test")
    private class StatelessApplication extends Application {

        @Override
        protected ApplicationState instantiateState() {
            return null;
        }

    }

    @Test
    public void validApplicationWithoutState() {
        assertTrue(new StatelessApplication() != null);
    }


    @IntegratesApplication("Test")
    private class StatefulApplication extends Application {

        @Override
        protected ApplicationState instantiateState() {
            return new TestApplicationState();
        }

    }

    @Test
    public void validApplicationWithState() {
        assertTrue(new StatefulApplication() != null);
    }

    private class IncorrectlyConfiguredApplicationNoIntegrationAnnotation extends Application {

        @Override
        protected ApplicationState instantiateState() {
            return null;
        }

    }
    
    @Test
    public void invalidApplicationNoIntegrationAnnotation() {
        assertThrows(ApplicationConfigurationError.class, () -> new IncorrectlyConfiguredApplicationNoIntegrationAnnotation());
    }

    @IntegratesApplication("Test")
    private class IncorrectlyConfiguredApplicationInvalidReturnActionMethod extends Application {

        @Override
        protected ApplicationState instantiateState() {
            return null;
        }

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

        @Override
        protected ApplicationState instantiateState() {
            return null;
        }

        @CommandMatch("test command")
        public void invalidAction(Map<String, String> arguments) { }

    }

    @Test
    public void invalidApplicationActionVoidReturn() {
        assertThrows(ApplicationConfigurationError.class, () -> new IncorrectlyConfiguredApplicationVoidReturnActionMethod());
    }

    @IntegratesApplication("Test")
    private class IncorrectlyConfiguredApplicationNoArguments extends Application {

        @Override
        protected ApplicationState instantiateState() {
            return null;
        }

        @CommandMatch("test command")
        public MarvinApplicationResource invalidAction() {
            return null;
        }

    }

    @Test
    public void invalidApplicationActionNoArguments() {
        assertThrows(ApplicationConfigurationError.class, () -> new IncorrectlyConfiguredApplicationNoArguments());
    }

    @IntegratesApplication("Test")
    private class IncorrectlyConfiguredApplicationInvalidActionMatchRegex extends Application {

        @Override
        protected ApplicationState instantiateState() {
            return null;
        }

        @CommandMatch("(?<invalid key>test) command")
        public MarvinApplicationResource invalidAction(Map<String, String> arguments) {
            return null;
        }

    }

    @Test
    public void invalidApplicationActionInvalidMatchRegex() {
        assertThrows(ApplicationConfigurationError.class, () -> new IncorrectlyConfiguredApplicationInvalidActionMatchRegex());
    }

    @IntegratesApplication("Test")
    private class IncorrectlyConfiguredApplicationInvalidMapArgument extends Application {

        @Override
        protected ApplicationState instantiateState() {
            return null;
        }

        @CommandMatch("test command")
        public MarvinApplicationResource invalidAction(Map<Integer, Double> arguments) {
            return null;
        }

    }

    @Test
    public void invalidApplicationActionInvalidMapArgument() {
        assertThrows(ApplicationConfigurationError.class, () -> new IncorrectlyConfiguredApplicationInvalidMapArgument());
    }

    @IntegratesApplication("Test")
    private class IncorrectlyConfiguredApplicationWrongArguments extends Application {

        @Override
        protected ApplicationState instantiateState() {
            return null;
        }

        @CommandMatch("test command")
        public MarvinApplicationResource invalidAction(String argument) {
            return null;
        }

    }

    @Test
    public void invalidApplicationActionWrongArguments() {
        assertThrows(ApplicationConfigurationError.class, () -> new IncorrectlyConfiguredApplicationWrongArguments());
    }

    @IntegratesApplication("Test")
    private class IncorrectlyConfiguredApplicationTooManyArguments extends Application {

        @Override
        protected ApplicationState instantiateState() {
            return null;
        }

        @CommandMatch("test command")
        public MarvinApplicationResource invalidAction(Map<String, String> arguments, String argument) {
            return null;
        }

    }

    @Test
    public void invalidApplicationActionTooManyArguments() {
        assertThrows(ApplicationConfigurationError.class, () -> new IncorrectlyConfiguredApplicationTooManyArguments());
    }

    @IntegratesApplication("Test")
    private class IncorrectlyConfiguredApplicationPrivateActionMethod extends Application {

        @Override
        protected ApplicationState instantiateState() {
            return null;
        }

        @CommandMatch("test command")
        private MarvinApplicationResource invalidAction(Map<String, String> arguments) {
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
        ApplicationState expectedState = new TestApplicationState();
        Application app = new StandardApplication();
        assertTrue(app.getState().isSameAs(expectedState));
    }

    @Test
    public void storesCopiesOfStates() {
        Application app = new StandardApplication();
        ApplicationState state = new TestApplicationState();
        app.setState(state);
        assertFalse(app.getState() == state);
    }

    @Test
    public void returnsCopiesOfStates() {
        Application app = new StandardApplication();
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
        TestApplicationState expectedState = new TestApplicationState();
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
