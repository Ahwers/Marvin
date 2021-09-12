package com.ahwers.marvin.framework.application;

import java.util.Map;

public class ApplicationAction {
	
	private String applicationName;
	private String actionName;
	private Map<String, String> actionArguments;

	public ApplicationAction(String applicationName, String actionName, Map<String, String> arguments) {
		this.applicationName = applicationName;
		this.actionName = actionName;
		this.actionArguments = arguments;
	}

	public ApplicationAction() {}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public ApplicationAction(String applicationName, String actionName) {
		this.applicationName = applicationName;
		this.actionName = actionName;
		
		if (applicationName == null) {
			throw new Error("NullInstanceVariable: ApplicationAction instances cannot have a null applicationName");
		}
		else if (actionName == null) {
			throw new Error("NullInstanceVariable: ApplicationsAction instances cannot have a null actionName");
		}
	}
	
	public String getApplicationName() {
		return this.applicationName;
	}
	
	public String getActionName() {
		return this.actionName;
	}
	
	public void setActionArguments(Map<String, String> arguments) {
		this.actionArguments = arguments;
	}
	
	public Map<String, String> getArguments() {
		return this.actionArguments;
	}

}
