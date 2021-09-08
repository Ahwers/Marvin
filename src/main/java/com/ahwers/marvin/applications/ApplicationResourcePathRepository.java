package com.ahwers.marvin.applications;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

// TODO: This should be in a different package I think. In fact this entire package is very bloated.
public class ApplicationResourcePathRepository {
	
	private static ApplicationResourcePathRepository instance;

	// TODO: Try to remove this method and make getInstance() always instantiate the object correctly based on context. Maybe it tries to access the context itself and if that fails makes the basic one.
	public static void instantiateSingleton(ServletContext context) {
		instance = new ApplicationResourcePathRepository(context);
	}
	
	public static ApplicationResourcePathRepository getInstance() {
		if (instance == null) {
			instance = new ApplicationResourcePathRepository();
		}
		
		return instance;
	}
	
	private Map<String, String> resources = new HashMap<>();

	private ApplicationResourcePathRepository(ServletContext context) {
		resources.put("graphical_calculator_html", context.getRealPath("/WEB-INF/classes/resources/graphical_calculator.html"));
		resources.put("algebraic_expression_syntax", context.getRealPath("/WEB-INF/classes/resources/algebraic_expression_syntax.txt"));
		resources.put("azure_key", context.getRealPath("/WEB-INF/classes/resources/azure_key.txt"));
		resources.put("command_closing_pleasentries", context.getRealPath("/WEB-INF/classes/resources/command_closing_pleasentries.txt"));
		resources.put("command_opening_pleasentries", context.getRealPath("/WEB-INF/classes/resources/command_opening_pleasentries.txt"));
		resources.put("wolfram_app_id", context.getRealPath("/WEB-INF/classes/resources/wolfram_app_id.txt"));
		resources.put("login", context.getRealPath("/WEB-INF/classes/resources/login.txt"));
	}

	// TODO: Get rid of this because we will always be testing on a deployed application
	private ApplicationResourcePathRepository() {
		resources.put("graphical_calculator_html", getClass().getClassLoader().getResource("./resources/graphical_calculator.html").getPath().toString());
		resources.put("algebraic_expression_syntax", getClass().getClassLoader().getResource("./resources/algebraic_expression_syntax.txt").getPath().toString());
		resources.put("azure_key", getClass().getClassLoader().getResource("./resources/azure_key.txt").getPath().toString());
		resources.put("command_closing_pleasentries", getClass().getClassLoader().getResource("./resources/command_closing_pleasentries.txt").getPath().toString());
		resources.put("command_opening_pleasentries", getClass().getClassLoader().getResource("./resources/command_opening_pleasentries.txt").getPath().toString());
		resources.put("wolfram_app_id", getClass().getClassLoader().getResource("./resources/wolfram_app_id.txt").getPath().toString());
		resources.put("login", getClass().getClassLoader().getResource("./resources/login.txt").getPath().toString());
	}

	// TODO: Throw an exception if the resource doesn't exist!!!
	// TODO: Refactor to just search for the resource using the string provided rather than populating a hash at instantiation
	//		 Can make this just a normal static class and it won't need to be a singleton
	public String getApplicationResourcePathForKey(String resourceKey) {
		return resources.get(resourceKey);
	}

}
