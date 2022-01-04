package com.ahwers.marvin.applications.test;

import java.util.Map;

import com.ahwers.marvin.framework.application.Application;
import com.ahwers.marvin.framework.application.action.annotations.CommandMatch;
import com.ahwers.marvin.framework.application.annotations.IntegratesApplication;
import com.ahwers.marvin.framework.resource.MarvinApplicationResource;
import com.ahwers.marvin.framework.resource.ResourceRepresentationType;

// TODO: FIND A WAY TO PUT THIS IN THE TEST PACKAGE SO IT DOESN'T GET DEPLOYED WITH THE LIVE SERVICE
@IntegratesApplication("Service Tester")
public class ServiceTestApplication extends Application {

	@CommandMatch("successful marvin request test")
	public MarvinApplicationResource successfulMarvinRequestTest(Map<String, String> arguments) {
		return new MarvinApplicationResource(ResourceRepresentationType.PLAIN_TEXT, "worked mate");
	}

	@CommandMatch("invalid marvin request test")
	public MarvinApplicationResource invalidMarvinRequestTest(Map<String, String> arguments) {
		throw new RuntimeException();
	}

	@CommandMatch("conflicted marvin request test")
	public MarvinApplicationResource conflictedMarvinRequestTest(Map<String, String> arguments) {
		return new MarvinApplicationResource(ResourceRepresentationType.PLAIN_TEXT, "be more specific mate");
	}
	
	@CommandMatch("conflicted marvin request test")
	public MarvinApplicationResource reallyConflictedMarvinRequestTest(Map<String, String> arguments) {
		return new MarvinApplicationResource(ResourceRepresentationType.PLAIN_TEXT, "be more specific mate");
	}

}
