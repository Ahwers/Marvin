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
import com.ahwers.marvin.framework.resource.MarvinApplicationResource;

public class ApplicationsManager {

	private Map<String, Application> applications = new HashMap<>();
	
	public ApplicationsManager(Set<Application> applicationsSet) {
		for (Application app : applicationsSet) {
			String appName = app.getName();
			if (this.applications.containsKey(appName)) {
				throw new ApplicationConfigurationError("Multiple applications of name '" + appName + "' have been configured. Application names must be unique.");
			}

			this.applications.put(appName, app);
		}	
	}
	
	public void updateApplicationState(ApplicationState requestAppState) {
		String appName = requestAppState.getApplicationName();
		ApplicationState serverAppState = this.applications.get(appName).getState();

		if (requestAppState.isFresherThan(serverAppState)) {
			requestAppState.incrementVersion();
			this.applications.get(appName).setState(requestAppState);
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
	
	public MarvinApplicationResource executeActionInvocation(ActionInvocation actionInvocation) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassCastException {
		String applicationName = actionInvocation.getApplicationName();
		String actionName = actionInvocation.getActionName();
		Map<String, String> actionArguments = actionInvocation.getArguments();
		
		Application actionApplication = this.applications.get(applicationName);

		// TODO: I think we make actions return CommandResponse objects.
		//		 Maybe they can consist of just simple strings.
		//		 These are added to MarvinResponse too
		MarvinApplicationResource commandResource = null;
		commandResource = (MarvinApplicationResource) actionApplication.getClass().getDeclaredMethod(actionName, Map.class).invoke(actionApplication, actionArguments);

		return commandResource;
	}

	// TODO: Return a copy
	public Application getApplication(String appName) {
		return null;
	}
	
}
