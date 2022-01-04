package com.ahwers.marvin.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.ahwers.marvin.framework.application.Application;
import com.ahwers.marvin.framework.application.ApplicationState;
import com.ahwers.marvin.framework.application.TestApplicationState;
import com.ahwers.marvin.framework.application.annotations.IntegratesApplication;
import com.ahwers.marvin.framework.application.annotations.Stateful;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class ApplicationStatesHeaderUnmarshallerTest {
    
    @IntegratesApplication("Test")
    @Stateful(TestApplicationState.class)
    private static class TestApplication extends Application {

    }

    private ApplicationStatesHeaderUnmarshaller unmarshaller;

    @BeforeAll
    public void setUp() {
        Set<Application> testApps = new HashSet<>();
        testApps.add(new TestApplication());

        unmarshaller = new ApplicationStatesHeaderUnmarshaller(testApps);
    }

    @Test
    public void successfulUnmarshallingTest() throws JsonProcessingException {
        Map<String, ApplicationState> expectedAppStates = new HashMap<>();
        expectedAppStates.put("Test", new TestApplicationState("Test", 0));       

        ObjectMapper jsonMapper = new ObjectMapper();
        String marshalledAppStates = jsonMapper.writeValueAsString(expectedAppStates);

        Map<String, ApplicationState> unmarshalledAppStates = unmarshaller.unmarshall(marshalledAppStates);
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
    public void unsuccessfulUnmarshallingTest() throws JsonProcessingException {
        Map<String, String> erroneousAppState = new HashMap<String, String>();
        erroneousAppState.put("test", "test");

        ObjectMapper jsonMapper = new ObjectMapper();
        String marshalledErroneousAppState = jsonMapper.writeValueAsString(erroneousAppState);

        assertThrows(JsonProcessingException.class, () -> unmarshaller.unmarshall(marshalledErroneousAppState));
    }
}
