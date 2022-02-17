package com.ahwers.marvin.framework.application.state;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ApplicationStateMarshaller {

	private static Logger logger = LogManager.getLogger(ApplicationStateMarshaller.class);

    public static String marshallApplicationStateToJson(ApplicationState appState) {
		String marshalledAppState = null;
        try {
            marshalledAppState = new ObjectMapper().writeValueAsString(appState);
        } catch (JsonProcessingException e) {
            logger.error("Exception of class " + e.getClass().toString() + " thrown with message: " + e.getMessage());
        }

        return marshalledAppState;
    }

    public static ApplicationState unmarshallApplicationStateToClass(String marshalledAppState, Class<? extends ApplicationState> appStateClass) {
        ApplicationState appState = null;
        try {
            appState = new ObjectMapper().readValue(marshalledAppState, appStateClass);
        } catch (JsonProcessingException e) {
            logger.error("Exception of class " + e.getClass().toString() + " thrown with message: " + e.getMessage());
        }

        return appState;
    }

}
