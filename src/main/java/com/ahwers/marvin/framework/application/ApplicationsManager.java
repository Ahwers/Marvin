package com.ahwers.marvin.framework.application;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ahwers.marvin.framework.resource.MarvinApplicationResource;

import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

// TODO: This entire class's code is (quite understandably) a mess.
// TODO: This will need to be a singleton if we implement concurrency due to the useful state it holds.
// TODO: Need more framework instantiation error handling. 
//		  - Attempted integration of an application adaptor that does not have an IntegratesApplication annotation.
//			Maybe we should only be searching for application adaptors that have been annotated with this annotation rather than children of ApplicationAdaptor anyway!
// 		  - Other such error handling for uses of my custom annotations such as making sure they are provided values.
public class ApplicationsManager {

	private final String MARVIN_APPLICATION_PACKAGE_PREFIX = "com.ahwers.marvin";

	private Map<String, ApplicationAdaptor> applicationAdaptors = new HashMap<>();
	private List<ActionDefinition> actionDefinitions = new ArrayList<>();
	
	public ApplicationsManager() {
		populateApplicationAdaptorsMap();
		populateActionDefinitionsList();
	}
	
	private void populateApplicationAdaptorsMap() {
		Set<Class<?>> applicationAdaptorClasses = new Reflections(MARVIN_APPLICATION_PACKAGE_PREFIX).getTypesAnnotatedWith(IntegratesApplication.class);
		
		for (Class<?> appAdaptorClass : applicationAdaptorClasses) {
			String applicationName = appAdaptorClass.getDeclaredAnnotation(IntegratesApplication.class).value();
			
			ApplicationAdaptor applicationAdaptor = null;
			try {
				applicationAdaptor = (ApplicationAdaptor) appAdaptorClass.getDeclaredConstructor().newInstance();
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
			
			if (applicationName != null && applicationAdaptor != null) {
				this.applicationAdaptors.put(applicationName, applicationAdaptor);
			}
		}
	}
	
	private void populateActionDefinitionsList() {
		Reflections annotatedMethodsScanner = new Reflections(new ConfigurationBuilder()
			     .setUrls(ClasspathHelper.forPackage(MARVIN_APPLICATION_PACKAGE_PREFIX))
			     .setScanners(new MethodAnnotationsScanner())
			     .filterInputsBy(new FilterBuilder().includePackage(MARVIN_APPLICATION_PACKAGE_PREFIX))
		);
		
		Set<Method> applicationActionMethods = annotatedMethodsScanner.getMethodsAnnotatedWith(CommandMatch.class);
		applicationActionMethods.addAll(annotatedMethodsScanner.getMethodsAnnotatedWith(CommandMatches.class));
		
		for (Method actionMethod : applicationActionMethods) {
			String actionName = actionMethod.getName();
			String applicationName = actionMethod.getDeclaringClass().getDeclaredAnnotation(IntegratesApplication.class).value();
			
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
			
			ActionDefinition action = new ActionDefinition(applicationName, actionName, commandMatchRegexes);
			this.actionDefinitions.add(action);
		}
	}
	
	private boolean actionMethodHasMultipleCommandMatches(Method actionMethod) {
		boolean hasMultipleMatches = false;
		
		CommandMatches multiMatchAnnotation = actionMethod.getDeclaredAnnotation(CommandMatches.class);
		if (multiMatchAnnotation != null) {
			hasMultipleMatches = true;
		}
		
		return hasMultipleMatches;
	}

	public List<ActionInvocation> getApplicationInvocationsThatDirectlyMatchCommand(String command) {
		List<ActionInvocation> matchingActions = new ArrayList<>();

		for (ActionDefinition potentialAction : this.actionDefinitions) {
			if (potentialAction.matchesCommandRequest(command)) {
				ActionInvocation appInvocation = potentialAction.buildActionInvocationForCommandRequest(command);
				matchingActions.add(appInvocation);
			}
		}

		return matchingActions;
	}
	
	public MarvinApplicationResource executeActionInvocation(ActionInvocation actionInvocation) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassCastException {
		String applicationName = actionInvocation.getApplicationName();
		String actionName = actionInvocation.getActionName();
		Map<String, String> actionArguments = actionInvocation.getArguments();
		
		ApplicationAdaptor actionApplicationAdaptor = this.applicationAdaptors.get(applicationName);

		MarvinApplicationResource commandResource = null;
		commandResource = (MarvinApplicationResource) actionApplicationAdaptor.getClass().getDeclaredMethod(actionName, Map.class).invoke(actionApplicationAdaptor, actionArguments);

		return commandResource;
	}
	
}
