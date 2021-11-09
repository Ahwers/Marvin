package com.ahwers.marvin.framework.application;

import java.util.Map;

// TODO: Should this and ActionDefintion be renamed to be pre-fixed with Application?
public class ActionInvocation extends ApplicationAction {
    
    private Map<String, String> arguments;

    public ActionInvocation(String applicationName, String actionName, Map<String, String> arguments) {
        super(applicationName, actionName);

        if (arguments == null) {
            throw new IllegalArgumentException("Arguments cannot be null.");
        }
        
        this.arguments = Map.copyOf(arguments);
    }

    public Map<String, String> getArguments() {
        return Map.copyOf(arguments);
    }

    public boolean isLike(ActionInvocation invocation) {
        boolean isLike = super.isLike(invocation);

        if (!arguments.equals(invocation.getArguments())) {
            isLike = false;
        }

        return isLike;
    }

}
