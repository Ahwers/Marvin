package com.ahwers.marvin.framework.application.state;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ApplicationStateMarshaller {

    public static String marshallApplicationStateToJson(ApplicationState appState) {
		String marshalledAppState = null;
        try {
            marshalledAppState = new ObjectMapper().writeValueAsString(appState);
        } catch (JsonProcessingException e) {
            // TODO: Log the error
            e.printStackTrace();
        }

        return marshalledAppState;
    }
    
}
