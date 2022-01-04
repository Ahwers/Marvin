package com.ahwers.marvin.framework.application;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.ahwers.marvin.framework.application.action.ActionDefinition;
import com.ahwers.marvin.framework.application.action.annotations.CommandMatch;
import com.ahwers.marvin.framework.application.action.annotations.CommandMatches;
import com.ahwers.marvin.framework.application.annotations.IntegratesApplication;
import com.ahwers.marvin.framework.application.annotations.Stateful;
import com.ahwers.marvin.framework.application.exceptions.ApplicationConfigurationError;
import com.ahwers.marvin.framework.resource.MarvinApplicationResource;
import com.fasterxml.jackson.core.JsonProcessingException;

public abstract class Application {
	
	private String name;
	private List<ActionDefinition> actions;
	private Class<? extends ApplicationState> stateClass = null;
	private ApplicationState state;

	public Application() {
		this.name = loadName();
		this.actions = loadActions();
		this.stateClass = loadStateClass();

		if (this.stateClass != null) {
			this.state = instantiateState();
		}
	}

	private String loadName() {
		IntegratesApplication integrationAnnotation = this.getClass().getDeclaredAnnotation(IntegratesApplication.class);
		if (integrationAnnotation == null) {
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
				
				validateAppActionMethod(classMethod, commandMatchRegexes);

				ActionDefinition action = new ActionDefinition(this.name, actionName, commandMatchRegexes);
				actionDefinitions.add(action);
			}
		}

		Collections.sort(actionDefinitions, new ArbitrarySort());

		return actionDefinitions;
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

	private void validateAppActionMethod(Method appActionMethod, List<String> regexes) {
		String baseErrorWording = ("Action method of application '" + getName() + "' named '" + appActionMethod.getName() + "' ");

		if (appActionMethod.getReturnType() != MarvinApplicationResource.class) {
			throw new ApplicationConfigurationError(baseErrorWording + "returns the wrong type of '" + appActionMethod.getReturnType().toString() + "'.");
		}
		else if (!Modifier.toString(appActionMethod.getModifiers()).equals("public")) {
			throw new ApplicationConfigurationError(baseErrorWording + "must be public");
		}
		else if (!regexesAreValid(regexes)) {
			throw new ApplicationConfigurationError(baseErrorWording + "has erroneous match regexes.");
		}
		else if ((appActionMethod.getParameterCount() != 1) || (appActionMethod.getParameterTypes()[0] != Map.class)) {
			throw new ApplicationConfigurationError(baseErrorWording + "does not have the correct parameters.");
		}
		else {
			Type[] methodParameterTypes = appActionMethod.getGenericParameterTypes();
			ParameterizedType parameterizedType = (ParameterizedType) methodParameterTypes[0];
			Type[] parameterizedTypeTypes = parameterizedType.getActualTypeArguments();
			if (!(parameterizedTypeTypes[0] == String.class) || !(parameterizedTypeTypes[1] == String.class)) {
				throw new ApplicationConfigurationError(baseErrorWording + "'s Map argument does not have the correct parameterized types");
			}
		}
	}

	private boolean regexesAreValid(List<String> regexes) {
		boolean areValid = true;
		try {
			for (String regex : regexes) {
                Pattern.compile(regex);
			}
		}
		catch (PatternSyntaxException e) {
			areValid = false;
		}

		return areValid;
	}

	private Class<? extends ApplicationState> loadStateClass() {
		Class<? extends ApplicationState> stateClass = null;

		Stateful statefulAnnotation = this.getClass().getDeclaredAnnotation(Stateful.class);
		if (statefulAnnotation != null) {
			stateClass = statefulAnnotation.value();
		}

		return stateClass;
	}

	private ApplicationState instantiateState() {
		ApplicationState state = null;

		ApplicationStateRepository appStateRepo = getAppStateRepository();
		String encodedAppState = appStateRepo.getEncodedStateOfApp(this.name);
		try {
			if (encodedAppState != null) {
				state = this.stateClass.getConstructor(String.class).newInstance(encodedAppState);
			}
			else {
				state = this.stateClass.getConstructor(String.class, Integer.class).newInstance(this.name, 0);
			}
		}
		catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException e) {
			throw new ApplicationConfigurationError("The app state class '" + this.stateClass.getName() + "' has been configured incorrectly.\n" + e.getMessage());
		}

		return state;
	}

	protected ApplicationStateRepository getAppStateRepository() {
		return ApplicationStateRepository.getInstance();
	}

	public Class<? extends ApplicationState> getStateClass() {
		return this.stateClass;
	}

	public ApplicationState getState() {
		ApplicationState clonedState = null;
		if (this.state != null) {
			try {
				clonedState = this.state.clone();
			} catch (CloneNotSupportedException e) {
				throw new ApplicationConfigurationError("The class '" + getStateClass().getName() + "' does not implement Cloneable.");
			}
		}

		return clonedState;
	}

	public void setState(ApplicationState newAppState) {
		try {
			this.state = newAppState.clone();
		} catch (CloneNotSupportedException e) {
			throw new ApplicationConfigurationError("The class '" + getStateClass().getName() + "' does not implement Cloneable.");
		}

		saveState();
	}

	private void saveState() {
		ApplicationStateRepository appStateRepo = getAppStateRepository();
		try {
			appStateRepo.saveState(this.name, this.state.encode());
		} catch (JsonProcessingException | UnsupportedEncodingException e) {
			// TODO: Log could not encode state
			e.printStackTrace();
		}
	}

	public String getName() {
		return this.name;
	}

	public List<ActionDefinition> getActions() {
		List<ActionDefinition> definitionsCopy = new ArrayList<>();
		for (ActionDefinition definition : this.actions) {
			definitionsCopy.add(definition.clone());
		}

		return List.copyOf(definitionsCopy);
	}

	private class ArbitrarySort implements Comparator<ActionDefinition> {

		@Override
		public int compare(ActionDefinition o1, ActionDefinition o2) {
			return o1.toString().compareTo(o2.toString());
		}

	}

}
