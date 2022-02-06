package com.ahwers.marvin.framework.resource;

import java.util.HashMap;
import java.util.Map;

// TODO: Redesign this component
public class MarvinApplicationResource {
	
	private String applicationName;
	private int currentStateId;
	private int previousStateId;
	private Map<ResourceRepresentationType, String> resourceRepresentations = new HashMap<>();
	private Map<ResourceRepresentationType, String> resourceRepresentationMessages = new HashMap<>();
	
	// TODO: Implement a builder class rather than thousands of public constructors
	public MarvinApplicationResource(String applicationName, int currentStateId, int previousStateId) {
		this.applicationName = applicationName;
		this.currentStateId = currentStateId;
		this.previousStateId = previousStateId;
	}
	
	public MarvinApplicationResource(String applicationName, int currentStateId, ResourceRepresentationType type, String content, String message) {
		this.applicationName = applicationName;
		this.currentStateId = currentStateId;
		resourceRepresentations.put(type, content);
		resourceRepresentationMessages.put(type, message);
	}
	
	public MarvinApplicationResource(String applicationName, int currentStateId, ResourceRepresentationType type, String content) {
		this.applicationName = applicationName;
		this.currentStateId = currentStateId;
		resourceRepresentations.put(type, content);
	}
	
	public MarvinApplicationResource(ResourceRepresentationType type, String content, String message) {
		this.applicationName = "Marvin";
		resourceRepresentations.put(type, content);
		resourceRepresentationMessages.put(type, message);
	}

	public MarvinApplicationResource(ResourceRepresentationType type, String content) {
		this.applicationName = "Marvin";
		resourceRepresentations.put(type, content);
	}
	
	public void addRepresentation(ResourceRepresentationType type, String content, String message) {
		resourceRepresentations.put(type, content);
		resourceRepresentationMessages.put(type, message);
	}

	public void addRepresentation(ResourceRepresentationType type, String content) {
		resourceRepresentations.put(type, content);
	}
	
	public String getRepresentation(ResourceRepresentationType type) {
		return resourceRepresentations.get(type);
	}

	public String getMessage(ResourceRepresentationType type) {
		return resourceRepresentationMessages.get(type);
	}
	
	public Map<ResourceRepresentationType, String> getResourceRepresentations() {
		return this.resourceRepresentations;
	}
	
	public String getApplicationName() {
		return this.applicationName;
	}
	
	public int getCurrentStateId() {
		return this.currentStateId;
	}
	
	public int getPreviousStateId() {
		return this.previousStateId;
	}
	
}
