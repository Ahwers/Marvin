package com.ahwers.marvin.applications;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

public class ApplicationResourcePathRepository {
	
	private static ApplicationResourcePathRepository instance;

	public static void instantiateSingleton(ServletContext context) {
		instance = new ApplicationResourcePathRepository(context);
	}
	
	public static ApplicationResourcePathRepository getInstance() throws Exception {
		if (instance == null) {
			throw new Exception("The singleton wasn't instantiated.");
		}
		
		return instance;
	}
	
	private Map<String, String> resources = new HashMap<>();
	
	private ApplicationResourcePathRepository(ServletContext context) {
		resources.put("graphical_calculator_html", context.getRealPath("/WEB-INF/classes/resources/graphical_calculator.html"));
	}
	
	public String getApplicationResourcePathForKey(String resourceKey) {
		return resources.get(resourceKey);
	}

}
