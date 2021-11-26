package com.ahwers.marvin.framework;

import com.ahwers.marvin.framework.application.ApplicationsManager;
import com.ahwers.marvin.framework.application.action.ActionInvocation;
import com.ahwers.marvin.framework.command.CommandFormatter;
import com.ahwers.marvin.framework.response.MarvinResponse;
import com.ahwers.marvin.framework.response.MarvinResponseBuilder;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ahwers.marvin.framework.application.Application;
import com.ahwers.marvin.framework.application.ApplicationRepository;
import com.ahwers.marvin.framework.application.ApplicationState;

public class Marvin {
	
	// TODO: Could implement response routing or something, so like issuing a command from my phone but getting the response sent to my desktop. This class could catch the "to my desktop" string or something and route the response that way.
	
	private CommandFormatter commandFormatter;
	private ApplicationsManager appManager;
	private MarvinResponseBuilder responseBuilder;
	
	public Marvin() {
		this.commandFormatter = new CommandFormatter();
		this.appManager = instantiateStandardApplicationsManager();
		this.responseBuilder = new MarvinResponseBuilder();
	}

	private ApplicationsManager instantiateStandardApplicationsManager() {
		ApplicationRepository appRepo = ApplicationRepository.getInstance();
		Set<Application> apps = appRepo.getStandardApplications();
		
		return new ApplicationsManager(apps); 
	}

	public void updateApplicationStates(Map<String, ApplicationState> applicationStates) {
		for (ApplicationState appState : applicationStates.values()) {
			appManager.updateApplicationState(appState);
		}
	}

	public MarvinResponse processCommand(String originalCommand) {
		String command = commandFormatter.formatCommand(originalCommand);
		System.out.println(command);
		// TODO: Log command recieved and formatted

		List<ActionInvocation> matchingActions = appManager.getApplicationInvocationsThatDirectlyMatchCommand(command);
		MarvinResponse response = responseBuilder.buildResponseForActions(matchingActions, appManager);	
		
		return response;
	}

	public MarvinResponse processActionInvocation(ActionInvocation actionInvocation) {
		return responseBuilder.buildResponseForActionInvocation(actionInvocation, appManager);
	}
	
}
