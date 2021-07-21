package com.ahwers.marvin.applications;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import com.ahwers.marvin.response.MarvinResponse;
import com.ahwers.marvin.response.RequestOutcome;

// TODO: This entire class's code is (quite understandably) a mess.
// TODO: This will need to be a singleton if we implement concurrency due to the useful state it holds.
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
			    if (matcher.find()) {
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
	
	public MarvinResponse executeApplicationAction(ApplicationAction applicationAction) {
		String applicationName = applicationAction.getApplicationName();
		String actionName = applicationAction.getActionName();
		Map<String, String> actionArguments = applicationAction.getArguments();
		
		ApplicationAdaptor actionApplicationAdaptor = this.applicationAdaptors.get(applicationName);
		actionApplicationAdaptor.setActionArguments(applicationAction.getArguments());

		MarvinResponse outcome = null;
		try {
			outcome = (MarvinResponse) actionApplicationAdaptor.getClass().getDeclaredMethod(actionName, Map.class).invoke(actionApplicationAdaptor, actionArguments);
		} catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException
				| SecurityException e) {
			outcome = new MarvinResponse(RequestOutcome.FAILED, "This action's implementation is erroneous, see logs for cause.");
			outcome.setFailException(e);
		}
		catch (InvocationTargetException e) {
			outcome = new MarvinResponse(RequestOutcome.FAILED, e.getMessage()); // TODO: Applications should add as many error handling exceptions as they can (and provide) messages so that Marvin can read their messages out explaining why the command action failed.
			outcome.setFailException(e);
		}
		
		return outcome;
	}
	
}
