package com.ahwers.marvin.framework.application;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
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
import com.ahwers.marvin.framework.application.resource.ApplicationResource;
import com.ahwers.marvin.framework.application.resource.enums.ResourceRepresentationType;
import com.ahwers.marvin.framework.application.state.TestApplicationState;
import com.ahwers.marvin.framework.application.state.interfaces.MarshalledApplicationStateRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
        public ApplicationResource actionOne(Map<String, String> arguments) {
            return new ApplicationResource(ResourceRepresentationType.PLAIN_TEXT, "WORKS");
        }

        @CommandMatch("two match")
        public ApplicationResource actionTwo(Map<String, String> arguments) {
            return null;
        }

    }

    @IntegratesApplication("Test Application Two")
    @Stateful(TestApplicationState.class)
    private class AnotherStandardApplication extends Application {

        @CommandMatch("two match")
        public ApplicationResource actionTwo(Map<String, String> arguments) {
            return null;
        }

    }

    @Test
    public void validInstantiation() {
        assertTrue(new ApplicationsManager(standardApplications) != null);
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
        ApplicationResource resource = appManager.executeActionInvocation(invocations.get(0));
        assertTrue(resource.getContent().equals("WORKS"));
    }

    private class TestMarshalledAppStateRepository implements MarshalledApplicationStateRepository {

        Map<String, String> states = new HashMap<>();

        @Override
        public String getMarshalledStateOfApp(String appName) {
            return states.get(appName);
        }

        @Override
        public void saveMarshalledStateUnderApplicationName(String marshalledState, String appName) {
            states.put(appName, marshalledState);
        }

    }

    @Test
    public void applicationHasPersistedState() throws JsonProcessingException {
        TestApplicationState appOnePersistedState = new TestApplicationState("Test Application One", 0);
        appOnePersistedState.setTest("persisted_state_of_app_1");
        String marshalledAppOneState = new ObjectMapper().writeValueAsString(appOnePersistedState);

        TestMarshalledAppStateRepository testAppStateRepo = new TestMarshalledAppStateRepository();
        testAppStateRepo.saveMarshalledStateUnderApplicationName(marshalledAppOneState, "Test Application One");

        ApplicationsManager appManager = new ApplicationsManager(standardApplications, testAppStateRepo);
        assertTrue(appManager.getApplication("Test Application One").getState().isSameAs(appOnePersistedState));
    }

    @Test
    public void applicationWithoutPersistedState() {
        TestApplicationState expectedState = new TestApplicationState("Test Application Two", 0);

        TestMarshalledAppStateRepository testAppStateRepo = new TestMarshalledAppStateRepository();

        ApplicationsManager appManager = new ApplicationsManager(standardApplications, testAppStateRepo);
        assertTrue(appManager.getApplication("Test Application Two").getState().isSameAs(expectedState));
    }

    @Test
    public void applicationWithPreviouslyUpdatedState() {
        TestMarshalledAppStateRepository testAppStateRepo = new TestMarshalledAppStateRepository();
        ApplicationsManager appManager = new ApplicationsManager(standardApplications, testAppStateRepo);

        TestApplicationState appOnePersistedState = new TestApplicationState("Test Application One", 0);
        appOnePersistedState.setTest("persisted_state_of_app_1");
        appManager.updateApplicationState(appOnePersistedState);

        ApplicationsManager appManager2 = new ApplicationsManager(standardApplications, testAppStateRepo);
        assertTrue(appManager2.getApplication(appOnePersistedState.getApplicationName()).getState().isSameAs(appOnePersistedState));
    }

}
