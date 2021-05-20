package com.ahwers.marvinclient;

import java.util.List;
import java.util.Map;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.ahwers.marvin.Resource;
import com.ahwers.marvin.ResourceRepresentationType;

public class BrowserDriver {
	
	// TODO: When selenium 4 is released we can utilise tabs for each application.
	// TODO: What if the user closes the browser? Can we watch for this and restart the driver on next marvin response?
	
	private static BrowserDriver instance;
	public static BrowserDriver getBrowserDriver() {
		if (instance == null) {
			instance = new BrowserDriver();
		}
		
		return instance;
	}
	
	private WebDriver browserDriver;
	
	private String activeApplicationName = "";
	private int activeApplicationStateId;
	
	private BrowserDriver() {
		System.setProperty("webdriver.gecko.driver", "C:\\WebDriver\\bin\\geckodriver.exe");
		this.browserDriver = new FirefoxDriver();
	}
	
	public void loadRepresentationTypeFromResource(ResourceRepresentationType type, Resource resource) {
		activeApplicationName = resource.getApplicationName();
		activeApplicationStateId = resource.getCurrentStateId();
		
		if (type == ResourceRepresentationType.HTML) {
			browserDriver.get("data:text/html;charset=utf-8," + resource.getRepresentation(type));
		}
		else if (type == ResourceRepresentationType.HTML_STATE_UPDATE_SCRIPT) {
			JavascriptExecutor js = (JavascriptExecutor) this.browserDriver;
			js.executeScript(resource.getRepresentation(type));
		}
	}
	
	public String getHostedApplicationName() {
		return this.activeApplicationName;
	}
	
	public int getHostedApplicationStateId() {
		return this.activeApplicationStateId;
	}

}
