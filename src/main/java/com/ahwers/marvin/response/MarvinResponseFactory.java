package com.ahwers.marvin.response;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ahwers.marvin.applications.ApplicationAction;
import com.ahwers.marvin.applications.ApplicationsManager;
import com.ahwers.marvin.applications.FuzzyMatcher;
import com.ahwers.marvin.response.resource.MarvinResource;
import com.ahwers.marvin.response.resource.ResourceRepresentationType;

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
	
	public MarvinResponse getResponseForAppAction(String stringifiedAppAction) {
		ApplicationAction action = constructApplicationActionFromString(stringifiedAppAction);
//		MarvinResource resource = appManager.executeApplicationAction(action);
		MarvinResponse response = new MarvinResponse(RequestOutcome.SUCCESS);
//		response.setResource(resource);
		return response;
	}
	
	public MarvinResponse getResponseForCommand(String command) {
		this.command = command;
		this.directCommandActionMatches = appManager.getApplicationActionsThatDirectlyMatchCommand(command);
		this.phoneticCommandActionMatches = appManager.getApplicationActionsThatPhoneticallyMatchCommand(command);
		
		MarvinResponse response = null;
		if (oneActionMatchedDirectly()) {
			response = buildActionExecutionResponse(directCommandActionMatches.get(0));
		}
		else if (multipleActionsMatchedDirectly()) {
			response = buildActionSelectionResponse(directCommandActionMatches);
		}
		else if (oneOrMoreActionsMatchedPhonetically()) {
			response = buildActionSelectionResponse(phoneticCommandActionMatches);
		}
		else {
			response = buildUnmatchedCommandResponse();
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

	// TODO: Maybe to get rid of marvin status codes we map custom unchecked exceptions to create responses??
	private MarvinResponse buildActionExecutionResponse(ApplicationAction action) {
		MarvinResponse response = new MarvinResponse(RequestOutcome.SUCCESS);
	
		MarvinResource resource = null;
		try {
			resource = appManager.executeApplicationAction(action);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			response = new MarvinResponse(RequestOutcome.FAILED, "There's something wrong.");
			response.setFailException(e);
		} catch (ClassCastException e) {
			response = new MarvinResponse(RequestOutcome.OUTDATED, ("The implementation of '" + action.getActionName() + "' needs updating because it is returning a MarvinResponse object."));
			response.setFailException(e);
		} catch (Exception e) {
			response = new MarvinResponse(RequestOutcome.INVALID, "The invocation of this command was erroneous for some reason.");
			response.setFailException(e);
		}
		response.setResource(resource);
		
		return response;
	}
	
	private MarvinResponse buildActionSelectionResponse(List<ApplicationAction> actionOptions) {
		MarvinResponse response = new MarvinResponse(RequestOutcome.CONFLICTED, "Please be more specific.");
		
		String selectionContent = "";
		for (ApplicationAction action : actionOptions) {
			selectionContent += (stringifyApplicationAction(action) + "\n");
		}
		response.setResource(new MarvinResource(ResourceRepresentationType.COMMAND_OPTION_LIST, selectionContent));
		
		return response;
	}
	
	private String stringifyApplicationAction(ApplicationAction action) {
		String stringifiedAction = "";
		
		stringifiedAction += (action.getApplicationName() + ":" + action.getActionName());
		int argumentCount = 0;
		for (String argumentKey : action.getArguments().keySet()) {
			String delimiter = (argumentCount > 0 ? "&" : "?");
			stringifiedAction += (delimiter + argumentKey + "==" + action.getArguments().get(argumentKey));
			argumentCount++;
		}
		
		return stringifiedAction;
	}
	
	// TODO: This is horrible.
	// 		 Could i refactor it to use an URI class or something instead? 
	private ApplicationAction constructApplicationActionFromString(String stringifiedAppAction) {
		// appName:appAction&field==value&anotherField==value
		
		String[] actionParts = stringifiedAppAction.split("\\?");
		
		String actionMetadata = actionParts[0];
		String[] metadataParts = actionMetadata.split(":");
		String applicationName = metadataParts[0];
		String actionName = metadataParts[1];
		
		Map<String, String> actionArguments = new HashMap<>();
		if (actionParts.length > 1) {
			String[] actionArgumentStrings = actionParts[1].split("&");
			for (String argument : actionArgumentStrings) {
				String[] argumentParts = argument.split("==");
				actionArguments.put(argumentParts[0], argumentParts[1]);
			}
		}
		
		ApplicationAction appAction = new ApplicationAction(applicationName, actionName);
		appAction.setArguments(actionArguments);
		
		return appAction;
	}
	
	private MarvinResponse buildUnmatchedCommandResponse() {
		return new MarvinResponse(RequestOutcome.UNMATCHED, "Sorry, I have not been programmed to process that command.");
	}
	
}
