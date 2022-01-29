package com.ahwers.marvin.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.ahwers.marvin.framework.application.state.ApplicationState;
import com.ahwers.marvin.framework.application.state.ApplicationStateFactory;

import org.json.JSONObject;

public class ApplicationStatesMarshaller {

    private ApplicationStateFactory appStateFactory;

    public ApplicationStatesMarshaller(ApplicationStateFactory appStateFactory) {
        this.appStateFactory = appStateFactory;
    }

	// TODO: Better variable names. json and jsoned clash badly
    public Map<String, ApplicationState> unmarshallJSONAppStates(String marshalledAppStates) {

		JSONObject jsonAppStates = new JSONObject(marshalledAppStates);
		Set<String> applicationNames = jsonAppStates.keySet();

		Map<String, ApplicationState> appStates = new HashMap<>();
		for (String appName : applicationNames) {
			JSONObject jsonedAppState = jsonAppStates.getJSONObject(appName);
			String stringedAppState = jsonedAppState.toString(); // TODO: DOES THIS DO WHAT I THINK IT DOES
			ApplicationState appState = appStateFactory.unmarshallApplicationStateForApplication(stringedAppState, appName);
			appStates.put(appName, appState);
		}

		return appStates;
    }
    
}