package com.ahwers.marvin.framework;

import com.ahwers.marvin.framework.application.ApplicationsManager;
import com.ahwers.marvin.framework.application.action.ActionInvocation;
import com.ahwers.marvin.framework.application.resource.ApplicationResource;
import com.ahwers.marvin.framework.application.state.ApplicationState;
import com.ahwers.marvin.framework.application.state.ApplicationStateFactory;
import com.ahwers.marvin.framework.application.state.ApplicationStateMarshaller;
import com.ahwers.marvin.framework.command.CommandFormatter;
import com.ahwers.marvin.framework.response.MarvinResponse;
import com.ahwers.marvin.framework.response.MarvinResponseBuilder;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ahwers.marvin.framework.application.Application;
import com.ahwers.marvin.framework.application.ApplicationRepository;

// TODO: If this object handles the attachment of app states to response objects, unit test it
public class Marvin {
	
	// TODO: Could implement response routing or something, so like issuing a command from my phone but getting the response sent to my desktop. This class could catch the "to my desktop" string or something and route the response that way.
	
	private CommandFormatter commandFormatter;
	private ApplicationsManager appManager;
	private MarvinResponseBuilder responseBuilder;
	private ApplicationStateFactory appStateFactory;
	
	public Marvin(String packageRoute) {
		// TODO: Just make the repo static
		ApplicationRepository appRepo = new ApplicationRepository(packageRoute);
		Set<Application> supportedApps = appRepo.getSupportedApplications();

		this.commandFormatter = new CommandFormatter();
		this.appManager = new ApplicationsManager(supportedApps);
		this.responseBuilder = new MarvinResponseBuilder();
		this.appStateFactory = new ApplicationStateFactory(supportedApps);
    }

    public void updateApplicationStates(Map<String, ApplicationState> applicationStates) {
		for (ApplicationState appState : applicationStates.values()) {
			appManager.updateApplicationState(appState);
		}
	}

	// TODO: Have marvin call manager.invoke and catch the exception, then pass either the resource or exception to the response builder.
	public MarvinResponse processCommand(String originalCommand) {
		String command = commandFormatter.formatCommand(originalCommand);
		System.out.println(command);
		// TODO: Log command recieved and formatted

		MarvinResponse response = null;

		List<ActionInvocation> matchingActions = appManager.getApplicationInvocationsThatDirectlyMatchCommand(command);
		if (matchingActions.size() == 1) {
			response = processActionInvocation(matchingActions.get(0));
		}
		else if (matchingActions.size() == 0) {
			response = this.responseBuilder.buildResponseForUnmatchedCommand(command);
		}
		else {
			response = this.responseBuilder.buildResponseForConflictingActions(matchingActions);
		}

		return response;
	}

	public MarvinResponse processActionInvocation(ActionInvocation actionInvocation) {
		MarvinResponse response = null;

		ApplicationResource appResource = null;
		try {
			appResource = this.appManager.executeActionInvocation(actionInvocation);
			response = responseBuilder.buildResponseForApplicationResource(appResource);
		} catch (NoSuchMethodException e) {
			InvocationTargetException invocationException = new InvocationTargetException(e);
			response = this.responseBuilder.buildResponseForInvocationException(invocationException);
		} catch (Exception e) {
			InvocationTargetException invocationException = null;
			if (e.getClass() != InvocationTargetException.class) {
				invocationException = new InvocationTargetException(e);
			}
			else {
				invocationException = (InvocationTargetException) e;
			}

			response = this.responseBuilder.buildResponseForInvocationException(invocationException);
		}

		String appName = actionInvocation.getApplicationName();
		ApplicationState appState = this.appManager.getApplication(appName).getState();
		if (appState != null) {
			response.setJsonAppState(ApplicationStateMarshaller.marshallApplicationStateToJson(appState));
		}

		return response;
	}
	
	public ApplicationStateFactory getApplicationStateFactory() {
		return this.appStateFactory;
	}
}
