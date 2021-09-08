package com.ahwers.marvin.applications.testing;

import java.util.Map;

import com.ahwers.marvin.applications.Application;
import com.ahwers.marvin.applications.ApplicationAdaptor;
import com.ahwers.marvin.applications.CommandMatch;
import com.ahwers.marvin.applications.IntegratesApplication;
import com.ahwers.marvin.response.resource.MarvinApplicationResource;
import com.ahwers.marvin.response.resource.ResourceRepresentationType;

@IntegratesApplication("Service Tester")
public class ServiceTestApplicationAdaptor extends ApplicationAdaptor {

	@Override
	protected Application instantiateApplication() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@CommandMatch("successful marvin request test")
	public MarvinApplicationResource successfulMarvinRequestTest(Map<String, String> arguments) {
		return new MarvinApplicationResource(ResourceRepresentationType.PLAIN_TEXT, "worked mate");
	}

}
