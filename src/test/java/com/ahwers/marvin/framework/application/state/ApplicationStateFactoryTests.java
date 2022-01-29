package com.ahwers.marvin.framework.application.state;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import com.ahwers.marvin.framework.application.Application;
import com.ahwers.marvin.framework.application.annotations.IntegratesApplication;
import com.ahwers.marvin.framework.application.annotations.Stateful;
import com.ahwers.marvin.framework.application.state.ApplicationStateFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class ApplicationStateFactoryTests {

    private Set<Application> supportedApplications;

    @IntegratesApplication("Test")
    @Stateful(TestApplicationState.class)
    private class TestApplication extends Application {

    }

    @BeforeAll
    public void setUp() {
        supportedApplications = Set.of(new TestApplication());
    }

    @Test
    public void validInstantitation() {
        assertTrue(new ApplicationStateFactory(supportedApplications) != null);
    }

    @Test
    public void validUnmarshallApplicationStateForApplication() throws JsonProcessingException {
        TestApplicationState expectedApplicationState = new TestApplicationState("Test", 10);
        
        ObjectMapper jsonMapper = new ObjectMapper();
        String marshalledAppState = jsonMapper.writeValueAsString(expectedApplicationState);

        ApplicationStateFactory factory = new ApplicationStateFactory(supportedApplications);
        TestApplicationState actualApplicationState = (TestApplicationState) factory.unmarshallApplicationStateForApplication(marshalledAppState, "Test");

        assertTrue(actualApplicationState.isSameAs(expectedApplicationState));
    }

    @Test
    public void invalidUnmarshallApplicationNotLoaded() throws JsonProcessingException {
        TestApplicationState expectedApplicationState = new TestApplicationState("not_loaded_application", 10);
        
        ObjectMapper jsonMapper = new ObjectMapper();
        String marshalledAppState = jsonMapper.writeValueAsString(expectedApplicationState);

        ApplicationStateFactory factory = new ApplicationStateFactory(supportedApplications);
        assertThrows(IllegalArgumentException.class, () -> factory.unmarshallApplicationStateForApplication(marshalledAppState, "not_loaded_application"));
    }

    @Test
    public void getSupportedApplications() {
        Set<Class<? extends Application>> expectedApplicationClasses = Set.of(TestApplication.class);
        
        ApplicationStateFactory factory = new ApplicationStateFactory(supportedApplications);
        
        Set<Class<? extends Application>> actualApplicationClasses = new HashSet<>();
        for (Application app : factory.getSupportedApplications()) {
            actualApplicationClasses.add(app.getClass());
        }

        assertAll(
            () -> assertTrue(expectedApplicationClasses.size() == actualApplicationClasses.size()),
            () -> {
                for (Class<?> appClass : expectedApplicationClasses) {
                    assertTrue(actualApplicationClasses.contains(appClass));
                }
            }
        );
    }
    
}
