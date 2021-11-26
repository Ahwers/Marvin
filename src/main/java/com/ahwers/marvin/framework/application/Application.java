package com.ahwers.marvin.framework.application;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.ahwers.marvin.framework.application.action.ActionDefinition;
import com.ahwers.marvin.framework.application.action.annotations.CommandMatch;
import com.ahwers.marvin.framework.application.action.annotations.CommandMatches;
import com.ahwers.marvin.framework.application.annotations.IntegratesApplication;
import com.ahwers.marvin.framework.application.exceptions.ApplicationConfigurationError;

public abstract class Application {
	
	private String name;
	private List<ActionDefinition> actions;
	private ApplicationState state;
	private Class<? extends ApplicationState> stateClass = null;

	public Application() {
		this.name = loadName();
		this.actions = loadActions();
		this.state = instantiateState();

		if (this.state != null) {
			this.stateClass = this.state.getClass();
		}
	}

	private String loadName() {
		IntegratesApplication integrationAnnotation = this.getClass().getDeclaredAnnotation(IntegratesApplication.class);
		if (integrationAnnotation == null) {
			// TODO: The service will need to map this to a 5xx error response
			throw new ApplicationConfigurationError("The application class " + this.getClass().getName() + " has not been annotated with IntegratesApplication.class");
		}
		String name = integrationAnnotation.value();

		return name;
	}

	private List<ActionDefinition> loadActions() {
		List<ActionDefinition> actionDefinitions = new ArrayList<>();

		for (Method classMethod : this.getClass().getDeclaredMethods()) {
			if (methodIsAnAppAction(classMethod) == true) {
				String actionName = classMethod.getName();
				List<String> commandMatchRegexes = getCommandMatchRegexesFromActionMethod(classMethod);
				
				ActionDefinition action = new ActionDefinition(this.name, actionName, commandMatchRegexes);
				actionDefinitions.add(action);
			}
		}

		Collections.sort(actionDefinitions, new ArbitrarySort());

		return actionDefinitions;
	}

	private List<String> getCommandMatchRegexesFromActionMethod(Method actionMethod) {
		List<String> commandMatchRegexes = new ArrayList<>();

		if (actionMethodHasMultipleCommandMatches(actionMethod)) {
			CommandMatch[] matches = actionMethod.getDeclaredAnnotation(CommandMatches.class).value();
			for (CommandMatch match : matches) {
				commandMatchRegexes.add(match.value());
			}
		}
		else {
			commandMatchRegexes.add(actionMethod.getDeclaredAnnotation(CommandMatch.class).value());
		}

		return commandMatchRegexes;
	}

	private boolean methodIsAnAppAction(Method method) {
		boolean isAnAppAction = false;

		CommandMatch singleMatchAnnotation = method.getDeclaredAnnotation(CommandMatch.class);
		if (singleMatchAnnotation != null) {
			isAnAppAction = true;
		}
		else if (actionMethodHasMultipleCommandMatches(method)) {
			isAnAppAction = true;
		}

		return isAnAppAction;
	}

	private boolean actionMethodHasMultipleCommandMatches(Method actionMethod) {
		boolean hasMultipleMatches = false;
		
		CommandMatches multiMatchAnnotation = actionMethod.getDeclaredAnnotation(CommandMatches.class);
		if (multiMatchAnnotation != null) {
			hasMultipleMatches = true;
		}
		
		return hasMultipleMatches;
	}
	
	protected abstract ApplicationState instantiateState();
	
	public Class<? extends ApplicationState> getStateClass() {
		return this.stateClass;
	}

	public ApplicationState getState() {
		return this.state;
	}

	public void setState(ApplicationState newAppState) {
		this.state = newAppState;
	}

	public String getName() {
		return this.name;
	}

	public List<ActionDefinition> getActions() {
		return List.copyOf(this.actions);
	}

	private class ArbitrarySort implements Comparator<ActionDefinition> {

		@Override
		public int compare(ActionDefinition o1, ActionDefinition o2) {
			return o1.toString().compareTo(o2.toString());
		}

	}

}
