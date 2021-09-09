package com.ahwers.marvin.framework.application;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

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
	private Map<ApplicationAction, List<String>> applicationActions = new HashMap<>();
	
	public ApplicationsManager() {
		populateApplicationAdaptorsMap();
		populateApplicationActionsMap();
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
	
	private void populateApplicationActionsMap() {
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
			
			ApplicationAction action = new ApplicationAction(applicationName, actionName);
			this.applicationActions.put(action, commandMatchRegexes);
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
	
	public List<ApplicationAction> getApplicationActionsThatDirectlyMatchCommand(String command) {
		List<ApplicationAction> matchingActions = new ArrayList<>();
		
		for (ApplicationAction possibleAppAction : this.applicationActions.keySet()) {
			List<String> actionMatchRegexes = this.applicationActions.get(possibleAppAction);
			
			for (String matchRegex : actionMatchRegexes) {
				Pattern pattern = Pattern.compile(matchRegex, Pattern.CASE_INSENSITIVE);
			    FuzzyMatcher matcher = new FuzzyMatcher(pattern.matcher(command));
			    if (matcher.find()) { // TODO: This can be cleaned the heck up.
			    	Map<String, String> arguments = new HashMap<>();
			    	for (int i = 1; i < matcher.groupCount() + 1; i++) {
			    		arguments.put(matcher.groupName(i), matcher.group(i));
			    	}
			    	possibleAppAction.setArguments(arguments);
					matchingActions.add(possibleAppAction);
				}	
			}
		}
		
		return matchingActions;
	}
	
	public List<ApplicationAction> getApplicationActionsThatPhoneticallyMatchCommand(String command) {
		// TODO: Implement
		return new ArrayList<>();
	}

	public MarvinApplicationResource executeApplicationAction(ApplicationAction applicationAction) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassCastException {
		String applicationName = applicationAction.getApplicationName();
		String actionName = applicationAction.getActionName();
		Map<String, String> actionArguments = applicationAction.getArguments();
		
		ApplicationAdaptor actionApplicationAdaptor = this.applicationAdaptors.get(applicationName);

		MarvinApplicationResource commandResource = null;
		commandResource = (MarvinApplicationResource) actionApplicationAdaptor.getClass().getDeclaredMethod(actionName, Map.class).invoke(actionApplicationAdaptor, actionArguments);

		return commandResource;
	}
	
}
