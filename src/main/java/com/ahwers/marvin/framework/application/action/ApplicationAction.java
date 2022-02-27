package com.ahwers.marvin.framework.application.action;

public abstract class ApplicationAction {
	
	private String applicationName;
	private String actionName;

	public ApplicationAction() {}

	public ApplicationAction(String appName, String actionName) {
		if (appName == null || actionName == null) {
			throw new IllegalArgumentException("Arguments cannot be null.");
		}

		this.applicationName = appName;
		this.actionName = actionName;
	}

	public String getApplicationName() {
		return this.applicationName;
	}
	
	public String getActionName() {
		return this.actionName;
	}

	public boolean isSameAs(ApplicationAction action) {
		boolean isSameAs = true;

		if (!this.applicationName.equals(action.getApplicationName())) {
			isSameAs = false;
		}
		else if (!this.actionName.equals(action.getActionName())) {
			isSameAs = false;
		}

		return isSameAs;
	}

}
