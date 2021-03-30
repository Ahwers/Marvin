package com.ahwers.marvin.applications;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ahwers.marvin.MarvinResponse;

public abstract class ApplicationAdaptor {
	
	private Application application;
	private Map<String, String> actionArguments;
	
	protected abstract Application instantiateApplication();
	
	protected Application getApplication() {
		if (application == null) {
			application = instantiateApplication();
		}
		
		return this.application;
	}
	
	public void setActionArguments(Map<String, String> actionArguments) {
		this.actionArguments = actionArguments;
	}
	
	public String getActionArgument(String argumentName) {
		return this.actionArguments.get(argumentName);
	}
	
}
