package com.ahwers.marvin.framework.application;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.ahwers.marvin.framework.application.action.annotations.CommandMatch;
import com.ahwers.marvin.framework.application.annotations.IntegratesApplication;
import com.ahwers.marvin.framework.application.exceptions.ApplicationConfigurationError;
import com.ahwers.marvin.framework.resource.MarvinApplicationResource;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class ApplicationsManagerTest {

    /**
     * Test Cases:
     *  - Update application state
     *  - Get apps multiple matches
     *  - Get apps single match
     *  - Execute action invocation
     */

    private Set<Application> standardApplications;
    private Application app1;
    private Application app2;

    @BeforeAll
    public void instantiateStandardApplications() {
        this.standardApplications = new HashSet<Application>();

        this.app1 = new StandardApplication();
        this.standardApplications.add(app1);

        this.app2 = new AnotherStandardApplication();
        this.standardApplications.add(app2);
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

    @IntegratesApplication("Test Application One")
    private class StandardApplication extends Application {

        @Override
        protected ApplicationState instantiateState() {
            return new TestApplicationState();
        }

        @CommandMatch("one match")
        public MarvinApplicationResource actionOne(Map<String, String> arguments) {
            return null;
        }

    }

    @IntegratesApplication("Test Application Two")
    private class AnotherStandardApplication extends Application {

        @Override
        protected ApplicationState instantiateState() {
            return new TestApplicationState();
        }

    }

    @Test
    public void validInstantiation() {
        ApplicationsManager appManager = new ApplicationsManager(standardApplications);
        assertTrue(appManager != null);
    }

    @Test
    public void applicationCopiesInstantiated() {
        ApplicationsManager appManager = new ApplicationsManager(standardApplications);
        TestApplicationState state = (TestApplicationState) app1.getState();
        state.setTest("SUPER NEW TEST");
        TestApplicationState storedState = (TestApplicationState) appManager.getApplication(app1.getName()).getState();
        assertFalse(storedState.getTest().equals(state.getTest()));
    }

    @Test
    public void invalidInstantiationDuplicateApplicationNames() {
       assertThrows(ApplicationConfigurationError.class, () -> new ApplicationsManager(Set.of(new StandardApplication(), new StandardApplication())));
    }

    @Test
    public void invalidInstantiationNullArgument() {
        assertThrows(IllegalArgumentException.class, () -> new ApplicationsManager(null));
    }
 
    @Test
    public void invalidInstantiationEmptySet() {
        assertThrows(IllegalArgumentException.class, () -> new ApplicationsManager(new HashSet<Application>()));
    }

    @Test
    public void getApplication() {
        ApplicationsManager appManager = new ApplicationsManager(standardApplications);
        assertTrue(appManager.getApplication(app1.getName()) != null);
    }

    public void getApplicationReturnsACopy() {
        ApplicationsManager appManager = new ApplicationsManager(standardApplications);
        Application appCopy1 = appManager.getApplication(app1.getName());
        Application appCopy2 = appManager.getApplication(app1.getName());

        assertFalse(appCopy1 == appCopy2);
    }

    @Test
    public void updateApplicationStateNewVersion() {
        // TODO: Implement
    }

    @Test
    public void updateApplicationStateOldVersion() {
        // TODO: Implement
    }

}
