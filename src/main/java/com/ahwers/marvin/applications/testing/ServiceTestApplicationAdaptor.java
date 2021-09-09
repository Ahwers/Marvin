package com.ahwers.marvin.applications.testing;

import java.util.Map;

import com.ahwers.marvin.framework.application.Application;
import com.ahwers.marvin.framework.application.ApplicationAdaptor;
import com.ahwers.marvin.framework.application.CommandMatch;
import com.ahwers.marvin.framework.application.IntegratesApplication;
import com.ahwers.marvin.framework.resource.MarvinApplicationResource;
import com.ahwers.marvin.framework.resource.ResourceRepresentationType;
import com.ahwers.marvin.framework.response.MarvinResponse;
import com.ahwers.marvin.framework.response.RequestOutcome;

@IntegratesApplication("Service Tester")
public class ServiceTestApplicationAdaptor extends ApplicationAdaptor {

	@Override
	protected Application instantiateApplication() {
		return null;
	}

	@CommandMatch("^successful marvin request test$")
	public MarvinApplicationResource successfulMarvinRequestTest(Map<String, String> arguments) {
		return new MarvinApplicationResource(ResourceRepresentationType.PLAIN_TEXT, "worked mate");
	}

	@CommandMatch("^failed marvin request test$")
	private MarvinApplicationResource failedMarvinRequestTest(Map<String, String> arguments) {
		return new MarvinApplicationResource(ResourceRepresentationType.PLAIN_TEXT, "failed mate");
	}

	@CommandMatch("^invalid marvin request test$")
	public MarvinApplicationResource invalidMarvinRequestTest(Map<String, String> arguments) {
		throw new RuntimeException();
	}

	@CommandMatch("^outdated marvin request test$")
	public MarvinResponse outdatedMarvinRequestTest(Map<String, String> arguments) {
		return new MarvinResponse(RequestOutcome.SUCCESS);
	}

	@CommandMatch("^conflicted marvin request test$")
	public MarvinApplicationResource conflictedMarvinRequestTest(Map<String, String> arguments) {
		return new MarvinApplicationResource(ResourceRepresentationType.PLAIN_TEXT, "be more specific mate");
	}
	
	@CommandMatch("^conflicted marvin request test$")
	public MarvinApplicationResource reallyConflictedMarvinRequestTest(Map<String, String> arguments) {
		return new MarvinApplicationResource(ResourceRepresentationType.PLAIN_TEXT, "be more specific mate");
	}

}
