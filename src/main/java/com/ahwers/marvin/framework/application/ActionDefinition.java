package com.ahwers.marvin.framework.application;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActionDefinition extends ApplicationAction {

    private List<String> commandMatchRegexes;

    public ActionDefinition(String appName, String actionName, List<String> commandMatchRegexes) {
        super(appName, actionName);

        if (commandMatchRegexes == null) {
            throw new IllegalArgumentException("Arguments cannot be null.");
        }
        else if (commandMatchRegexes.size() < 1) {
            throw new IllegalArgumentException("At least one command match regex must be provided.");
        }

        this.commandMatchRegexes = List.copyOf(commandMatchRegexes);
    }
    
	public List<String> getCommandMatchRegexes() {
		return List.copyOf(this.commandMatchRegexes);
	}

	// TODO: I like this wording of CommandRequest for user input and Command for the formal declaration of requests that the action can satisfy.
	//		 Refactor this in.
	public boolean canServiceCommandRequest(String commandRequest) {
		if (commandRequest == null) {
			throw new IllegalArgumentException("Cannot be null.");
		}

		boolean commandRequestMatches = false;

		for (String matchRegex : commandMatchRegexes) {
            commandRequestMatches = requestMatchesCommandRegex(commandRequest, matchRegex);
		}

		return commandRequestMatches;
	}

    private boolean requestMatchesCommandRegex(String request, String commandMatch) {
        boolean matches = false;
        
        String formattedCommandMatch = ("^" + commandMatch + "$");
        Pattern pattern = Pattern.compile(formattedCommandMatch, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(request);
        if (matcher.find()) {
            matches = true;
        }

        return matches;
    }

    public ActionInvocation buildActionInvocationForCommandRequest(String commandRequest) {
        if (canServiceCommandRequest(commandRequest) == false) {
            throw new IllegalArgumentException("Cannot service the request: " + commandRequest + ".");
        }

        Map<String, String> arguments = pullArgumentsFromCommandRequest(commandRequest);
        ActionInvocation invocation = new ActionInvocation(this.getApplicationName(), this.getActionName(), arguments); 

        return invocation;
    }

    private Map<String, String> pullArgumentsFromCommandRequest(String commandRequest) {
        Map<String, String> arguments = new HashMap<>();
        
        for (String matchRegex : this.commandMatchRegexes) {
            if (requestMatchesCommandRegex(commandRequest, matchRegex)) {
                Pattern pattern = Pattern.compile(matchRegex, Pattern.CASE_INSENSITIVE);
                NamedGroupMatcher matcher = new NamedGroupMatcher(pattern.matcher(commandRequest));

                if (matcher.find()) {
                    for (int i = 1; i < matcher.groupCount() + 1; i++) {
                        arguments.put(matcher.groupName(i), matcher.group(i));
                    }
                }	
            }            
        }
		
        return arguments;
    }
}
