package com.ahwers.marvin.applications.testing;

import java.util.Map;

import com.ahwers.marvin.framework.application.Application;
import com.ahwers.marvin.framework.application.ApplicationState;
import com.ahwers.marvin.framework.application.action.annotations.CommandMatch;
import com.ahwers.marvin.framework.application.annotations.IntegratesApplication;
import com.ahwers.marvin.framework.resource.MarvinApplicationResource;
import com.ahwers.marvin.framework.resource.ResourceRepresentationType;
import com.ahwers.marvin.framework.response.MarvinResponse;
import com.ahwers.marvin.framework.response.RequestOutcome;

// TODO: FIND A WAY TO PUT THIS IN THE TEST PACKAGE SO IT DOESN'T GET DEPLOYED WITH THE LIVE SERVICE
@IntegratesApplication("Service Tester")
public class ServiceTestApplication extends Application {

	@Override
	protected ApplicationState instantiateState() {
		return null;
	}

	@CommandMatch("successful marvin request test")
	public MarvinApplicationResource successfulMarvinRequestTest(Map<String, String> arguments) {
		return new MarvinApplicationResource(ResourceRepresentationType.PLAIN_TEXT, "worked mate");
	}

	@CommandMatch("failed marvin request test")
	private MarvinApplicationResource failedMarvinRequestTest(Map<String, String> arguments) {
		return new MarvinApplicationResource(ResourceRepresentationType.PLAIN_TEXT, "failed mate");
	}

	@CommandMatch("invalid marvin request test")
	public MarvinApplicationResource invalidMarvinRequestTest(Map<String, String> arguments) {
		throw new RuntimeException();
	}

	@CommandMatch("outdated marvin request test")
	public MarvinResponse outdatedMarvinRequestTest(Map<String, String> arguments) {
		return new MarvinResponse(RequestOutcome.SUCCESS);
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
