package com.ahwers.marvinclient;

import java.util.List;

import com.ahwers.marvin.Resource;
import com.ahwers.marvin.ResourceType;

public class ResourceHandler {
	
	public static void openResource(List<Resource> resources) throws InvalidResponseException {
		ResourceHandler handler = new ResourceHandler(resources);
		handler.openResource();
	}
	
	private List<Resource> resources;
	
	private ResourceHandler(List<Resource> resources) {
		this.resources = resources;
	}
	
	private void openResource() throws InvalidResponseException {
		if (resourceIsWebpage()) {
			openBrowserResource();
		}
	}
	
	private boolean resourceIsWebpage() {
		boolean resourceIsWebpage = false;
		
		for (Resource resourceObject : resources) {
			if ((resourceObject.getType() == ResourceType.HTML) || (resourceObject.getType() == ResourceType.JAVASCRIPT_UPDATE_SCRIPT)) {
				resourceIsWebpage = true;
			}
		}
		
		return resourceIsWebpage;
	}
	
	private void openBrowserResource() throws InvalidResponseException {
		Resource resourceToLoad = null;
		if (browserIsCurrentlyHostingResourceApplication() && browerApplicationStateIsOneBelowResource()) {
			resourceToLoad = getResourceOfType(ResourceType.JAVASCRIPT_UPDATE_SCRIPT);
		}
		else {
			resourceToLoad = getResourceOfType(ResourceType.HTML);
		}
		
		BrowserDriver browserDriver = BrowserDriver.getBrowserDriver();
		browserDriver.loadResource(resourceToLoad);
	}
	
	private Resource getResourceOfType(ResourceType targetType) throws InvalidResponseException {
		Resource targetResource = null;
		for (Resource resourceObject : resources) {
			if (resourceObject.getType() == targetType) {
				if (targetResource == null) {
					targetResource = resourceObject;
				}
				else {
					throw new InvalidResponseException("Multiple resources of the type '"+ targetType.toString() + "' were provided.");
				}
			}
		}
		
		return targetResource;
	}
	
	private boolean browserIsCurrentlyHostingResourceApplication() throws InvalidResponseException {
		String resourceApplicationName = getResourceApplicationName();
		return (BrowserDriver.getBrowserDriver().getHostedApplicationName().equals(resourceApplicationName) ? true : false);
	}
	
	private String getResourceApplicationName() throws InvalidResponseException {
		String appName = resources.get(0).getApplicationName();
		for (Resource resourceObject : resources) {
			if (!resourceObject.getApplicationName().equals(appName)) {
				throw new InvalidResponseException("Resources for multiple applications have been passed here.");
			}
		}
		
		return appName;
	}
	
	private boolean browerApplicationStateIsOneBelowResource() throws InvalidResponseException {
		int resourceStateId = getResourceStateId();
		return (BrowserDriver.getBrowserDriver().getHostedApplicationStateId() == (resourceStateId - 1) ? true : false);
	}
	
	private int getResourceStateId() throws InvalidResponseException {
		int appStateId = resources.get(0).getStateId();
		for (Resource resourceObject : resources) {
			if (resourceObject.getStateId() != appStateId) {
				throw new InvalidResponseException("Resources for multiple application states have been passed here.");
			}
		}
		
		return appStateId;
	}

}
