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
import com.ahwers.marvin.framework.response.enums.InvocationOutcome;
import com.fasterxml.jackson.core.JsonProcessingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ahwers.marvin.framework.application.Application;
import com.ahwers.marvin.framework.application.ApplicationRepository;

public class Marvin {

	private static Logger logger = LogManager.getLogger(Marvin.class);
	
	private CommandFormatter commandFormatter;
	private ApplicationsManager appManager;
	private MarvinResponseBuilder responseBuilder;
	private ApplicationStateFactory appStateFactory;
	
	public Marvin(String packageRoute) {
		Set<Application> supportedApps = ApplicationRepository.getMarvinApplicationsInPackage(packageRoute);

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

	public MarvinResponse processCommand(String originalCommand) {
		logger.info("Command recieved: " + originalCommand);
		String command = commandFormatter.formatCommand(originalCommand);
		logger.info("Command formatted: " + command);

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

		logResponse(response);

		return response;
	}

	public MarvinResponse processActionInvocation(ActionInvocation actionInvocation) {
		MarvinResponse response = invokeActionInvocation(actionInvocation);
		logResponse(response);
		
		return response;
	}

	private MarvinResponse invokeActionInvocation(ActionInvocation actionInvocation) {
		MarvinResponse response = null;

		ApplicationResource appResource = null;
		try {
			appResource = this.appManager.executeActionInvocation(actionInvocation);
			response = responseBuilder.buildResponseForApplicationResource(appResource);
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
		Application app = this.appManager.getApplication(appName);
		if (app != null) {
			ApplicationState appState = app.getState();
			if (appState != null) {
				try {
					String marshalledState = ApplicationStateMarshaller.marshallApplicationStateToJson(appState);
					response.setJsonAppState(marshalledState);
				} catch (JsonProcessingException e) {
    		        logger.error("Could not marshall state to json in order to attach state to response.\nException of class " + e.getClass().toString() + " thrown with message: " + e.getMessage());
				}
			}
		}

		return response;
	}

	private void logResponse(MarvinResponse response) {
		if (response.getFailException() != null) {
			logger.error("Command execution failed with exception: " + response.getFailException().getClass() + ".\nMessage: " + response.getMessage());
		}
		else if (response.getStatus() == InvocationOutcome.SUCCESSFUL) {
			logger.trace("Command execution successful.");
		}
		else if (response.getStatus() == InvocationOutcome.UNMATCHED) {
			logger.error("Command unmatched.");
		}
		else if (response.getStatus() == InvocationOutcome.CONFLICTED) {
			logger.error("Command matched conflicting application actions.");
		}
	}
	
	public ApplicationStateFactory getApplicationStateFactory() {
		return this.appStateFactory;
	}
}
