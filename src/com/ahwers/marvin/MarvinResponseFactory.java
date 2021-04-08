package com.ahwers.marvin;

import java.util.ArrayList;
import java.util.List;

import com.ahwers.marvin.applications.ApplicationAction;
import com.ahwers.marvin.applications.ApplicationsManager;

public class MarvinResponseFactory {
	
	public static MarvinResponseFactory getResponseFactoryForApplicationManager(ApplicationsManager appManager) {
		return new MarvinResponseFactory(appManager);
	}
	
	private ApplicationsManager appManager;
	
	private String command;
	private List<ApplicationAction> directCommandActionMatches = new ArrayList<>();
	private List<ApplicationAction> phoneticCommandActionMatches = new ArrayList<>();
	
	private MarvinResponseFactory(ApplicationsManager appManager) {
		this.appManager = appManager;
	}
	
	public MarvinResponse getResponseForCommand(String command) {
		this.command = command;
		this.directCommandActionMatches = appManager.getApplicationActionsThatDirectlyMatchCommand(command);
		this.phoneticCommandActionMatches = appManager.getApplicationActionsThatPhoneticallyMatchCommand(command);
		
		MarvinResponse response = null;
		if (oneActionMatchedDirectly()) {
			response = appManager.executeApplicationAction(directCommandActionMatches.get(0));
		}
		else if (multipleActionsMatchedDirectly()) {
			response = buildActionSelectionResponse(directCommandActionMatches);
		}
		else if (oneOrMoreActionsMatchedPhonetically()) {
			response = buildActionSelectionResponse(phoneticCommandActionMatches);
		}
		else {
			response = buildUnmatchedActionResponse();
		}
		
		// Set up default response messages
		if (response.getResponseMessage() == null) {
			addDefaultMessageToResponse(response);
		}
		
		return response;
	}
	
	private boolean oneActionMatchedDirectly() {
		return (directCommandActionMatches.size() == 1 ? true : false);
	}
	
	private boolean multipleActionsMatchedDirectly() {
		return (directCommandActionMatches.size() > 1 ? true : false);
	}
	
	private boolean oneOrMoreActionsMatchedPhonetically() {
		return (phoneticCommandActionMatches.size() >= 1 ? true : false);
	}
	
	private MarvinResponse buildActionSelectionResponse(List<ApplicationAction> actionOptions) {
		// TODO: Implement
		return null;
	}
	
	private MarvinResponse buildUnmatchedActionResponse() {
		// TODO: Implement
		return null;
	}
	
	private void addDefaultMessageToResponse(MarvinResponse response) {
		CommandStatus commandStatus = response.getCommandStatus();
		if (commandStatus == CommandStatus.INVALID) {
			response.setResponseMessage("Sorry, I have not been programmed to process that command.");
		}
		else if (commandStatus == CommandStatus.FAILED) {
			response.setResponseMessage("Something failed, see my logs for it's cause.");
		}
		else if (commandStatus == CommandStatus.UNMATCHED) {
			response.setResponseMessage("Please be more specific.");
		}
	}
	
}
