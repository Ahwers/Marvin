package com.ahwers.marvin.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.ahwers.marvin.framework.application.Application;
import com.ahwers.marvin.framework.application.annotations.IntegratesApplication;
import com.ahwers.marvin.framework.application.annotations.Stateful;
import com.ahwers.marvin.framework.application.state.ApplicationState;
import com.ahwers.marvin.framework.application.state.ApplicationStateFactory;
import com.ahwers.marvin.framework.application.state.TestApplicationState;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class ApplicationStatesMarshallerTests {

    @IntegratesApplication("Test")
    @Stateful(TestApplicationState.class)
    private class TestApplication extends Application {

    }

    private Set<Application> supportedApps;
    private ApplicationStateFactory appStateFactory;

    @BeforeAll
    public void setUp() {
        this.supportedApps = Set.of(new TestApplication());
        this.appStateFactory = new ApplicationStateFactory(supportedApps);
    }
    
    @Test
    public void validInstantitation() {
        assertTrue(new ApplicationStatesMarshaller(this.appStateFactory) != null);
    }

    @Test
    public void successfulUnmarshallingTest() throws JsonProcessingException {
        Map<String, ApplicationState> expectedAppStates = new HashMap<>();
        expectedAppStates.put("Test", new TestApplicationState("Test", 0));       

        ObjectMapper jsonMapper = new ObjectMapper();
        String marshalledAppStates = jsonMapper.writeValueAsString(expectedAppStates);

        ApplicationStatesMarshaller marshaller = new ApplicationStatesMarshaller(this.appStateFactory);
        Map<String, ApplicationState> unmarshalledAppStates = marshaller.unmarshallJSONAppStates(marshalledAppStates);
        assertAll(
            () -> assertTrue(unmarshalledAppStates.size() == expectedAppStates.size()),
            () -> {
                for (String appName : unmarshalledAppStates.keySet()) {
                    assertTrue(unmarshalledAppStates.get(appName).isSameAs(expectedAppStates.get(appName)));
                }
            }
        );
    }

    @Test
    public void unsuccessfulUnmarshallingTestJsonIssue() throws JsonProcessingException {
        Map<String, String> erroneousAppState = new HashMap<String, String>();
        erroneousAppState.put("Test", "test");

        ObjectMapper jsonMapper = new ObjectMapper();
        String marshalledErroneousAppState = jsonMapper.writeValueAsString(erroneousAppState);

        ApplicationStatesMarshaller marshaller = new ApplicationStatesMarshaller(this.appStateFactory);
        assertThrows(JSONException.class, () -> marshaller.unmarshallJSONAppStates(marshalledErroneousAppState));
    }
    
}
