package com.ahwers.marvin.framework.response;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.ahwers.marvin.framework.application.ApplicationsManager;
import com.ahwers.marvin.framework.application.action.ActionInvocation;
import com.ahwers.marvin.framework.resource.MarvinApplicationResource;
import com.ahwers.marvin.framework.resource.ResourceRepresentationType;

public class MarvinResponseBuilder {
	
	public MarvinResponse buildResponseForActionInvocation(ActionInvocation actionInvocation, ApplicationsManager appManager) {
		return buildActionExecutionResponse(actionInvocation, appManager);
	}
	
	public MarvinResponse buildResponseForActions(List<ActionInvocation> actionInvocations, ApplicationsManager appManager) {
		MarvinResponse response = null;
		if (actionInvocations.size() == 1) {
			response = buildActionExecutionResponse(actionInvocations.get(0), appManager);
		}
		else if (actionInvocations.size() > 1) {
			response = buildActionSelectionResponse(actionInvocations);
		}
		else {
			response = buildUnmatchedCommandResponse();
		}

		return response;
	}

	// TODO: Maybe to get rid of marvin status codes we map custom unchecked exceptions to create responses??
	private MarvinResponse buildActionExecutionResponse(ActionInvocation action, ApplicationsManager appManager) {
		MarvinResponse response = new MarvinResponse(RequestOutcome.SUCCESS);
	
		// TODO: Can we get rid of most of these cases and just have success or failed?
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
