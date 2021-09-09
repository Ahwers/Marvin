package com.ahwers.marvin.service;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;

import com.ahwers.marvin.framework.application.ApplicationResourcePathRepository;

@ApplicationPath("/services") // TODO: Rename this?
public class MarvinApplication extends Application {

	public MarvinApplication(@Context ServletContext servletContext) throws Exception {
		ApplicationResourcePathRepository.instantiateSingleton(servletContext);
	}

	public Set<Object> getSingletons() {
		Set<Object> set = new HashSet<>();
	
		set.add(new MarvinService());
		
		return set;
	}
	
}
