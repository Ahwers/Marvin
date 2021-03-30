package com.ahwers.marvinclient;

import java.util.List;
import java.util.Map;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.ahwers.marvin.Resource;
import com.ahwers.marvin.ResourceType;

public class MarvinBrowserDriver {
	
	private static MarvinBrowserDriver instance;
	public static MarvinBrowserDriver getBrowserDriver() {
		if (instance == null) {
			instance = new MarvinBrowserDriver();
		}
		
		return instance;
	}
	
	private WebDriver browserDriver;
	
	private String activeApplicationName;
	private int activeApplicationStateId;
	
	private MarvinBrowserDriver() {
		System.setProperty("webdriver.gecko.driver", "C:\\WebDriver\\bin\\geckodriver.exe");
		this.browserDriver = new FirefoxDriver();
	}

	// TODO: This code is disgusting.
	public void openApplicationResource(List<Resource> resources) {
//		SELENIUM 4 LOGIC
//		switchToApplicationTab();
		
		Resource htmlResource = null;
		Resource javascriptTransformationResource = null;
		
		for (Resource appResource : resources) {
			ResourceType resourceType = appResource.getType();
			if (resourceType == ResourceType.HTML) {
				htmlResource = appResource;
			}
			else if (resourceType == ResourceType.JAVASCRIPT_UPDATE_SCRIPT) {
				javascriptTransformationResource = appResource;
			}
			
			if (javascriptTransformationResource != null) {
				String jsResourceApplicationName = javascriptTransformationResource.getApplicationName();
				if (this.activeApplicationName.equals(jsResourceApplicationName) && (this.activeApplicationStateId == (javascriptTransformationResource.getStateId() - 1))) {
					JavascriptExecutor js = (JavascriptExecutor) this.browserDriver;
					js.executeScript(javascriptTransformationResource.getContent());
					this.activeApplicationStateId = javascriptTransformationResource.getStateId();
				}
			}
			else {
				browserDriver.get("data:text/html;charset=utf-8," + htmlResource.getContent());
				this.activeApplicationStateId = htmlResource.getStateId();
			}
		}
	}
	
//	SELENIUM 4 LOGIC
//	private Map<String, Map<String, String>> applicationWindowHandles;
//
//	TODO: Selenium version 3 can't open new tabs, need to add a newTab button to all resource HTML templates.
//	  Until then tab selection cannot be accomplised so we need to open all resources on one tab
//	private void switchToApplicationTab() {
//	if (applicationIsOpen(applicationName)) {
//		browserDriver.switchTo().window(getWindowHandleForApplicationName(applicationName));
//	}
//	else {
//
////		browserDriver.switchTo().newWindow(WindowType.);
//		String newWindowHandle = browserDriver.getWindowHandle();
//	}
//	}
//
//	private boolean applicationIsOpen(String applicationName) {
//		boolean appIsOpen = false;
//		if (applicationWindowHandles.containsKey(applicationName)) {
//			appIsOpen = true;
//		}
//		
//		return appIsOpen;
//	}
//	
//	private String getWindowHandleForApplicationName(String appName) {
//		return applicationWindowHandles.get(appName).get("window_handle");
//	}
	
}
