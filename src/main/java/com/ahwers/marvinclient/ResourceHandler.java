package com.ahwers.marvinclient;

import java.util.List;

import com.ahwers.marvin.response.resource.MarvinResource;
import com.ahwers.marvin.response.resource.ResourceRepresentationType;

public class ResourceHandler {
	
	public static void openResource(MarvinResource resource) throws InvalidResponseException {
		ResourceHandler handler = new ResourceHandler(resource);
		handler.openResource();
	}
	
	private MarvinResource resource;
	
	private ResourceHandler(MarvinResource resource) {
		this.resource = resource;
	}
	
	private void openResource() throws InvalidResponseException {
		if (resourceHasHTML()) {
			openBrowserResource();
		}
	}
	
	private boolean resourceHasHTML() {
		boolean resourceHasHTML = false;
		
		String htmlRepresentation = this.resource.getRepresentation(ResourceRepresentationType.HTML);
		if (htmlRepresentation != null) {
			resourceHasHTML = true;
		}
		
		return resourceHasHTML;
	}
	
	private void openBrowserResource() throws InvalidResponseException {
		BrowserDriver browserDriver = BrowserDriver.getBrowserDriver();
		browserDriver.loadRepresentationTypeFromResource(ResourceRepresentationType.HTML, this.resource);
	}
	
	private boolean browserIsCurrentlyHostingResourceApplication() throws InvalidResponseException {
		String resourceApplicationName = getResourceApplicationName();
		return (BrowserDriver.getBrowserDriver().getHostedApplicationName().equals(resourceApplicationName) ? true : false);
	}
	
	private String getResourceApplicationName() throws InvalidResponseException {
		String appName = resource.getApplicationName();
		
		return appName;
	}
	
	private boolean browerApplicationStateIsOneBelowResource() throws InvalidResponseException {
		int resourceStateId = getResourceStateId();
		return (BrowserDriver.getBrowserDriver().getHostedApplicationStateId() == (resourceStateId - 1) ? true : false);
	}
	
	private int getResourceStateId() throws InvalidResponseException {
		int appStateId = resource.getCurrentStateId();
		
		return appStateId;
	}

}
