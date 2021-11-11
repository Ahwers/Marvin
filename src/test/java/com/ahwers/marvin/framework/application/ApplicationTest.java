package com.ahwers.marvin.framework.application;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import com.ahwers.marvin.framework.application.action.ActionDefinition;
import com.ahwers.marvin.framework.application.action.annotations.CommandMatch;
import com.ahwers.marvin.framework.application.annotations.IntegratesApplication;
import com.ahwers.marvin.framework.application.exceptions.ApplicationConfigurationError;

import org.junit.jupiter.api.Test;

// TODO: Make sure we're returning copies of mutable objects
public class ApplicationTest {
    
    /**
     * Test Cases:
     *  - getState
     *  - getStateClass
     *  - setState
     *  - getName
     *  - getActions
     **/    

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

    // TODO: Maybe this test case should be split up
    @Test
    public void actionDetectionAndConstruction() {
        List<ActionDefinition> expectedActions = new ArrayList<>();
        String appName = "Test";
        String actionOneName = "actionOne";
        List<String> actionOneCommandRegexes = new ArrayList<>();
        actionOneCommandRegexes.add("one match");
        String actionTwoName = "actionTwo";
        List<String> actionTwoCommandRegexes = new ArrayList<>();
        actionTwoCommandRegexes.add("one match");
        actionTwoCommandRegexes.add("two match");
        expectedActions.add(new ActionDefinition(appName, actionOneName, actionOneCommandRegexes));
        expectedActions.add(new ActionDefinition(appName, actionTwoName, actionTwoCommandRegexes));

        Application app = new StandardApplication();
        List<ActionDefinition> actualActions = app.getActions();

        // TODO: This sometimes fails!!!!!!!!!!!!
        assertAll(
            () -> assertEquals(expectedActions.size(), actualActions.size()),
            () -> {
                for (int i = 0; i < expectedActions.size(); i++) {
                    assertTrue(actualActions.get(i).isSameAs(expectedActions.get(i)));
                }
            }
        );
    }

}
