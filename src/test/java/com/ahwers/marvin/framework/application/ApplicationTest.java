package com.ahwers.marvin.framework.application;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import com.ahwers.marvin.framework.application.action.ActionDefinition;
import com.ahwers.marvin.framework.application.action.annotations.CommandMatch;
import com.ahwers.marvin.framework.application.annotations.IntegratesApplication;
import com.ahwers.marvin.framework.application.exceptions.ApplicationConfigurationError;

import org.junit.jupiter.api.Test;

public class ApplicationTest {
    
    @IntegratesApplication("Test")
    private class StandardApplication extends Application {

        @Override
        protected ApplicationState instantiateState() {
            return new TestApplicationState();
        }

        @CommandMatch("one match")
        public void actionOne() {}

        @CommandMatch("one match")
        @CommandMatch("two match")
        public void actionTwo() {}

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

    private class IncorrectlyConfiguredApplication extends Application {

        @Override
        protected ApplicationState instantiateState() {
            return null;
        }

    }
    
    @Test
    public void invalidApplicationNoIntegrationAnnotation() {
        assertThrows(ApplicationConfigurationError.class, () -> new IncorrectlyConfiguredApplication());
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

}
