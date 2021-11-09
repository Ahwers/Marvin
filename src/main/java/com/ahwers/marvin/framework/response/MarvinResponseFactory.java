package com.ahwers.marvin.framework.response;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import com.ahwers.marvin.framework.application.ActionInvocation;
import com.ahwers.marvin.framework.application.ApplicationsManager;
import com.ahwers.marvin.framework.resource.MarvinApplicationResource;
import com.ahwers.marvin.framework.resource.ResourceRepresentationType;

public class MarvinResponseFactory {
	
	public static MarvinResponseFactory getResponseFactoryForApplicationManager(ApplicationsManager appManager) {
		return new MarvinResponseFactory(appManager);
	}
	
	private ApplicationsManager appManager;
	
	private List<ActionInvocation> directCommandActionMatches = new ArrayList<>();
	
	private MarvinResponseFactory(ApplicationsManager appManager) {
		this.appManager = appManager;
	}
	
	public MarvinResponse getResponseForActionInvocation(ActionInvocation appAction) {
		return buildActionExecutionResponse(appAction);
	}
	
	public MarvinResponse getResponseForCommand(String command) {
		this.directCommandActionMatches = appManager.getApplicationInvocationsThatDirectlyMatchCommand(command);
		
		MarvinResponse response = null;
		if (oneActionMatchedDirectly()) {
			response = buildActionExecutionResponse(directCommandActionMatches.get(0));
		}
		else if (multipleActionsMatchedDirectly()) {
			response = buildActionSelectionResponse(directCommandActionMatches);
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
	
	// TODO: Maybe to get rid of marvin status codes we map custom unchecked exceptions to create responses??
	private MarvinResponse buildActionExecutionResponse(ActionInvocation action) {
		MarvinResponse response = new MarvinResponse(RequestOutcome.SUCCESS);
	
		MarvinApplicationResource resource = null;
		try {
			resource = appManager.executeActionInvocation(action);
		} catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException e) {
			response = new MarvinResponse(RequestOutcome.FAILED, "There's something wrong.");
			response.setFailException(e);
		} catch (ClassCastException e) {
			response = new MarvinResponse(RequestOutcome.OUTDATED, ("The implementation of '" + action.getActionName() + "' needs updating because it is returning a MarvinResponse object."));
			response.setFailException(e);
		} catch (InvocationTargetException e) {
			response = new MarvinResponse(RequestOutcome.INVALID, "The invocation of this command was erroneous for some reason. The exception message should contain more details.");
			response.setFailException(e);
		} catch (Exception e) {
			response = new MarvinResponse(RequestOutcome.FAILED, "There's something wrong.");{}
			response.setFailException(e);
		}
		response.setResource(resource);

		return response;
	}
	
	private MarvinResponse buildActionSelectionResponse(List<ActionInvocation> actionOptions) {
		MarvinResponse response = new MarvinResponse(RequestOutcome.CONFLICTED, "Please be more specific.");
		
		String selectionContent = "";
		for (ActionInvocation action : actionOptions) {
			selectionContent += (action.toString() + "\n");
		}
		response.setResource(new MarvinApplicationResource(ResourceRepresentationType.COMMAND_OPTION_LIST, selectionContent));
		
		return response;
	}

	private MarvinResponse buildUnmatchedCommandResponse() {
		return new MarvinResponse(RequestOutcome.UNMATCHED, "Sorry, I have not been programmed to process that command.");
	}

}
