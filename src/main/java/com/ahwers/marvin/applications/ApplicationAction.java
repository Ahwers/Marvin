package com.ahwers.marvin.applications;

import java.util.Map;

public class ApplicationAction {
	
	private String applicationName;
	private String actionName;
	private Map<String, String> actionArguments;
	
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
	
	public void setArguments(Map<String, String> arguments) {
		this.actionArguments = arguments;
	}
	
	public Map<String, String> getArguments() {
		return this.actionArguments;
	}

}
