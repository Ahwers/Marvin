package com.ahwers.marvin.framework.application.state;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ApplicationStateMarshaller {

    public static String marshallApplicationStateToJson(ApplicationState appState) throws JsonProcessingException {
        String marshalledAppState = new ObjectMapper().writeValueAsString(appState);

        return marshalledAppState;
    }

    public static ApplicationState unmarshallApplicationStateToClass(String marshalledAppState, Class<? extends ApplicationState> appStateClass) throws JsonProcessingException {
        ApplicationState appState = new ObjectMapper().readValue(marshalledAppState, appStateClass);

        return appState;
    }

}
