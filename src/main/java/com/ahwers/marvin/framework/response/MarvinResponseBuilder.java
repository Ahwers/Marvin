package com.ahwers.marvin.framework.response;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.ahwers.marvin.framework.application.action.ActionInvocation;
import com.ahwers.marvin.framework.application.resource.ApplicationResource;
import com.ahwers.marvin.framework.application.resource.enums.ResourceRepresentationType;
import com.ahwers.marvin.framework.response.enums.InvocationOutcome;

public class MarvinResponseBuilder {
	
	public MarvinResponse buildResponseForApplicationResource(ApplicationResource applicationResource) {
		MarvinResponse response = new MarvinResponse(InvocationOutcome.SUCCESSFUL);

		response.setResource(applicationResource);

		if ((applicationResource != null) && (applicationResource.getMessage() != null)) {
			response.setMessage(applicationResource.getMessage());
		}

		return response;
	}

	public MarvinResponse buildResponseForInvocationException(InvocationTargetException invocationException) {
		Throwable exception = invocationException.getTargetException();

		MarvinResponse response = null;
		if (exceptionIsForFailedCommand(exception)) {
			response = new MarvinResponse(InvocationOutcome.FAILED);
			response.setMessage("Something went wrong with my framework.");
		}
		else if (exception.getClass() == NoSuchMethodException.class) {
			response = new MarvinResponse(InvocationOutcome.UNMATCHED);
			response.setMessage("An invocation was requested on a non existent application function.");
		}
		else {
			response = new MarvinResponse(InvocationOutcome.INVALID);
			if (exception.getMessage() != null) {
				response.setMessage(exception.getMessage());
			}
			else {
				response.setMessage("The invocation of this command was erroneous for some reason.");
			}
		}

		response.setFailException(exception);

		return response;
	}

	// TODO: Failed commands catch illegal argument exceptions so maybe we shouldn't be throwing it as often as we do. Seriously re look at the exceptions we throw.
	private boolean exceptionIsForFailedCommand(Throwable exception) {
		boolean isForFailedCommand = false;
		if (exception.getClass() == IllegalAccessException.class) {
			isForFailedCommand = true;
		}
		else if (exception.getClass() == IllegalArgumentException.class) {
			isForFailedCommand = true;
		}
		else if (exception.getClass() == SecurityException.class) {
			isForFailedCommand = true;
		}

		return isForFailedCommand;
	}

	public MarvinResponse buildResponseForConflictingActions(List<ActionInvocation> conflictingActions) {
		MarvinResponse response = new MarvinResponse(InvocationOutcome.CONFLICTED);
		response.setMessage("Please be more specific.");

		String commandList = "";
		for (ActionInvocation invocation : conflictingActions) {
			commandList += (invocation.toString() + "\n");
		}
		ApplicationResource conflictedCommandResource = new ApplicationResource(ResourceRepresentationType.COMMAND_OPTION_LIST, commandList);
		response.setResource(conflictedCommandResource);

		return response;
	}

	public MarvinResponse buildResponseForUnmatchedCommand(String command) {
		MarvinResponse response = new MarvinResponse(InvocationOutcome.UNMATCHED);
		response.setMessage("No functionality matched the command '" + command + "'.");

		return response;
	}

}
