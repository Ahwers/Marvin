package com.ahwers.marvin.framework.application.state;

import java.util.Set;

import com.ahwers.marvin.framework.application.Application;
import com.fasterxml.jackson.core.JsonProcessingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ApplicationStateFactory {

	private static Logger logger = LogManager.getLogger(ApplicationStateFactory.class);

    private Set<Application> supportedApps;

    public ApplicationStateFactory(Set<Application> supportedApps) {
        this.supportedApps = supportedApps;
    }

    public ApplicationState unmarshallApplicationStateForApplication(String marshalledAppState, String applicationName) {
        Application app = getApplicationWithName(applicationName);
        Class<? extends ApplicationState> appStateClass = app.getStateClass();

        ApplicationState appState = null;
        try {
            appState = ApplicationStateMarshaller.unmarshallApplicationStateToClass(marshalledAppState, appStateClass);
        } catch (JsonProcessingException e) {
    		logger.error("Could not unmarshall app state from json. Exception of class " + e.getClass().toString() + " thrown with message: " + e.getMessage());
        }

        return appState;
    }

    private Application getApplicationWithName(String appName) {
        Application targetApp = null;

        for (Application app : this.supportedApps) {
            if (app.getName().equals(appName)) {
                targetApp = app;
            }
        }

        if (targetApp == null) {
            throw new IllegalArgumentException("An application with name '" + appName + "' has not been loaded.");
        }

        return targetApp;
    }

    public Set<Application> getSupportedApplications() {
        return this.supportedApps;
    }
    
}
