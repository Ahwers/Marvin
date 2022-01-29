package com.ahwers.marvin.framework.application;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ahwers.marvin.framework.application.action.ActionDefinition;
import com.ahwers.marvin.framework.application.action.ActionInvocation;
import com.ahwers.marvin.framework.application.action.annotations.CommandMatch;
import com.ahwers.marvin.framework.application.annotations.IntegratesApplication;
import com.ahwers.marvin.framework.application.annotations.Stateful;
import com.ahwers.marvin.framework.application.exceptions.ApplicationConfigurationError;
import com.ahwers.marvin.framework.application.state.TestApplicationState;
import com.ahwers.marvin.framework.resource.MarvinApplicationResource;
import com.ahwers.marvin.framework.resource.ResourceRepresentationType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class ApplicationsManagerTest {

    private Set<Application> standardApplications;
    private Application app1;
    private Application app2;

    @BeforeEach
    public void instantiateStandardApplications() {
        this.standardApplications = new HashSet<Application>();

        this.app1 = new StandardApplication();
        this.standardApplications.add(app1);

        this.app2 = new AnotherStandardApplication();
        this.standardApplications.add(app2);
    }

    @IntegratesApplication("Test Application One")
    @Stateful(TestApplicationState.class)
    private class StandardApplication extends Application {

        @CommandMatch("one match")
        public MarvinApplicationResource actionOne(Map<String, String> arguments) {
            return new MarvinApplicationResource(ResourceRepresentationType.PLAIN_TEXT, "WORKS");
        }

        @CommandMatch("two match")
        public MarvinApplicationResource actionTwo(Map<String, String> arguments) {
            return null;
        }

    }

    @IntegratesApplication("Test Application Two")
    @Stateful(TestApplicationState.class)
    private class AnotherStandardApplication extends Application {

        @CommandMatch("two match")
        public MarvinApplicationResource actionTwo(Map<String, String> arguments) {
            return null;
        }

    }

    @Test
    public void validInstantiation() {
        ApplicationsManager appManager = new ApplicationsManager(standardApplications);
        assertTrue(appManager != null);
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
    public void updateApplicationStateNewVersion() {
        ApplicationsManager appManager = new ApplicationsManager(standardApplications);
        TestApplicationState storedState1 = (TestApplicationState) appManager.getApplication(app1.getName()).getState();
        storedState1.setTest("duplicate state ");
        appManager.updateApplicationState(storedState1);
        TestApplicationState storedState2 = (TestApplicationState) appManager.getApplication(app1.getName()).getState();
        assertTrue(storedState1.isSameAs(storedState2));
    }

    @Test
    public void updateApplicationStateOldVersion() {
        ApplicationsManager appManager = new ApplicationsManager(standardApplications);
        TestApplicationState storedState1 = (TestApplicationState) appManager.getApplication(app1.getName()).getState();
        TestApplicationState storedState2 = (TestApplicationState) appManager.getApplication(app1.getName()).getState();
        storedState1.setTest("duplicate state 2");
        appManager.updateApplicationState(storedState1);
        appManager.updateApplicationState(storedState2);
        TestApplicationState finalStoredState = (TestApplicationState) appManager.getApplication(app1.getName()).getState();
        assertTrue(finalStoredState.isSameAs(storedState1));
    }

    @Test
    public void getActionsSingleMatch() {
        ApplicationsManager appManager = new ApplicationsManager(standardApplications);
        
        String commandRequest = "one match";
        List<ActionInvocation> expectedInvocations = new ArrayList<>();
        List<ActionDefinition> definitions = app1.getActions();
        for (int i = 0; i < definitions.size(); i++) {
            if (definitions.get(i).getActionName().equals("actionOne")) {
                expectedInvocations.add(definitions.get(i).buildActionInvocationForCommandRequest(commandRequest));
            }
        }

        List<ActionInvocation> matchingInvocations = appManager.getApplicationInvocationsThatDirectlyMatchCommand(commandRequest);
        assertAll(
            () -> assertTrue(expectedInvocations.size() == matchingInvocations.size()),
            () -> {
                for (int i = 0; i < expectedInvocations.size(); i++) {
                    assertTrue(matchingInvocations.get(i).isSameAs(expectedInvocations.get(i)));
                }
            }
        );
    }
    
    @Test
    public void getActionsMultipleMatches() {
        ApplicationsManager appManager = new ApplicationsManager(standardApplications);
        
        String commandRequest = "two match";
        List<ActionInvocation> expectedInvocations = new ArrayList<>();
        List<ActionDefinition> definitions = app1.getActions();
        for (int i = 0; i < definitions.size(); i++) {
            if (definitions.get(i).getActionName().equals("actionTwo")) {
                expectedInvocations.add(definitions.get(i).buildActionInvocationForCommandRequest(commandRequest));
            }
        }
        expectedInvocations.add(app2.getActions().get(0).buildActionInvocationForCommandRequest(commandRequest));

        List<ActionInvocation> matchingInvocations = appManager.getApplicationInvocationsThatDirectlyMatchCommand(commandRequest);
        assertAll(
            () -> assertTrue(expectedInvocations.size() == matchingInvocations.size()),
            () -> {
                for (int i = 0; i < expectedInvocations.size(); i++) {
                    assertTrue(matchingInvocations.get(i).isSameAs(expectedInvocations.get(i)));
                }
            }
        );
    }

    @Test
    public void executeActionInvocation() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassCastException {
        ApplicationsManager appManager = new ApplicationsManager(standardApplications);
        List<ActionInvocation> invocations = appManager.getApplicationInvocationsThatDirectlyMatchCommand("one match");
        MarvinApplicationResource resource = appManager.executeActionInvocation(invocations.get(0));
        assertTrue(resource.getResourceRepresentations().get(ResourceRepresentationType.PLAIN_TEXT).equals("WORKS"));
    }

}
