package com.ahwers.marvin.framework.application;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ahwers.marvin.framework.application.action.ActionDefinition;
import com.ahwers.marvin.framework.application.action.ActionInvocation;
import com.ahwers.marvin.framework.application.exceptions.ApplicationConfigurationError;
import com.ahwers.marvin.framework.application.resource.ApplicationResource;
import com.ahwers.marvin.framework.application.state.ApplicationState;
import com.ahwers.marvin.framework.application.state.ApplicationStateFactory;
import com.ahwers.marvin.framework.application.state.ApplicationStateMarshaller;
import com.ahwers.marvin.framework.application.state.MemoryBasedMarshalledApplicationStateRepository;
import com.ahwers.marvin.framework.application.state.MarshalledApplicationStateRepository;

public class ApplicationsManager {

	private MarshalledApplicationStateRepository appStateRepo;
	private Map<String, Application> applications = new HashMap<>();

	public ApplicationsManager(Set<Application> applicationsSet, MarshalledApplicationStateRepository marshalledAppStateRepo) {
		this.appStateRepo = marshalledAppStateRepo;

		loadApplicationsFromSet(applicationsSet);
		instantiateApplicationStates();
	}
	
	public ApplicationsManager(Set<Application> applicationsSet) {
		this.appStateRepo = MemoryBasedMarshalledApplicationStateRepository.getInstance();

		loadApplicationsFromSet(applicationsSet);
		instantiateApplicationStates();
	}
	
	private void loadApplicationsFromSet(Set<Application> applicationsSet) {
		if (applicationsSet == null) {
			throw new IllegalArgumentException("Cannot be null");
		}
		else if (applicationsSet.size() == 0) {
			throw new IllegalArgumentException("More than one Application must be supplied.");
		}

		for (Application app : applicationsSet) {
			String appName = app.getName();
			if (this.applications.containsKey(appName)) {
				throw new ApplicationConfigurationError("Multiple applications of name '" + appName + "' have been configured. Application names must be unique.");
			}

			this.applications.put(appName, app);
		}	
	}

	private void instantiateApplicationStates() {
		for (Application app : this.applications.values()) {
			if (app.getStateClass() != null) {
				instantiateAppState(app);
			}
		}
	}

	private void instantiateAppState(Application app) {
		ApplicationState appState = null;

		ApplicationState persistedAppState = getPersistedStateForAppWithRepo(app, appStateRepo);
		if (persistedAppState != null) {
			appState = persistedAppState;
		}
		else {
			try {
				appState = app.getStateClass().getDeclaredConstructor(String.class, Integer.class).newInstance(app.getName(), 0);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				// TODO: Are we already checking for this? Should this be moved to application if we're not? Yes.
				throw new ApplicationConfigurationError("The app state class '" + app.getStateClass().toString() + "' does not have a constructor accepting String and int for app name and state id.");
			}
		}

		app.setState(appState);
	}

	private ApplicationState getPersistedStateForAppWithRepo(Application app, MarshalledApplicationStateRepository appStateRepo) {
		String appName = app.getName();

		ApplicationStateFactory appStateFactory = new ApplicationStateFactory(Set.of(app));
		String marshalledAppState = appStateRepo.getMarshalledStateOfApp(appName);

		ApplicationState state = null;
		if (marshalledAppState != null) {
			state = appStateFactory.unmarshallApplicationStateForApplication(marshalledAppState, appName);
		}

		return state;
	}

	public void updateApplicationState(ApplicationState requestAppState) {
		String appName = requestAppState.getApplicationName();
		ApplicationState serverAppState = this.applications.get(appName).getState();

		if (requestAppState.isFresherThan(serverAppState)) {
			requestAppState.incrementVersion();
			this.applications.get(appName).setState(requestAppState);
        
			String marshalledAppState = ApplicationStateMarshaller.marshallApplicationStateToJson(requestAppState);
			appStateRepo.saveMarshalledStateUnderApplicationName(marshalledAppState, appName);
		}
	}

	public List<ActionInvocation> getApplicationInvocationsThatDirectlyMatchCommand(String command) {
		List<ActionInvocation> matchingActions = new ArrayList<>();

		for (Application app : this.applications.values()) {
			List<ActionDefinition> appActions = app.getActions();
			for (ActionDefinition potentialAction : appActions) {
				if (potentialAction.canServiceCommandRequest(command)) {
					ActionInvocation appInvocation = potentialAction.buildActionInvocationForCommandRequest(command);
					matchingActions.add(appInvocation);
				}
			}
		}

		return matchingActions;
	}
	
	public ApplicationResource executeActionInvocation(ActionInvocation actionInvocation) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassCastException {
		String applicationName = actionInvocation.getApplicationName();
		String actionName = actionInvocation.getActionName();
		Map<String, String> actionArguments = actionInvocation.getArguments();
		
		Application actionApplication = this.applications.get(applicationName);

		ApplicationResource commandResource = (ApplicationResource) actionApplication.getClass().getDeclaredMethod(actionName, Map.class).invoke(actionApplication, actionArguments);

		return commandResource;
	}

	public Application getApplication(String appName) {
		return this.applications.get(appName);
	}
	
}
