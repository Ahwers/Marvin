package com.ahwers.marvin.framework.application.state;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ahwers.marvin.testapplications.TestApplicationState;
import com.fasterxml.jackson.core.JsonProcessingException;

import org.junit.jupiter.api.Test;

public class ApplicationStateMarshallerTests {

    @Test
    public void marshall() throws JsonProcessingException {
        ApplicationState appState = new TestApplicationState("Test app", 100);
        assertTrue(ApplicationStateMarshaller.marshallApplicationStateToJson(appState).equals("{\"applicationName\":\"Test app\",\"version\":100,\"test\":\"test\"}"));
    }

    @Test
    public void unmarshall() throws JsonProcessingException {
        String marshalled = "{\"applicationName\":\"Test app\",\"version\":100,\"test\":\"test\"}";
        assertTrue(ApplicationStateMarshaller.unmarshallApplicationStateToClass(marshalled, TestApplicationState.class).isSameAs(new TestApplicationState("Test app", 100)));
    }

    @Test
    public void unmarshallException() {
        String marshalled = "";
        assertThrows(JsonProcessingException.class, () -> ApplicationStateMarshaller.unmarshallApplicationStateToClass(marshalled, TestApplicationState.class).isSameAs(new TestApplicationState("Test app", 100)));
    }
    
}
