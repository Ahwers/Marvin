package com.ahwers.marvin.framework.application;

import com.ahwers.marvin.framework.resource.MarvinApplicationResource;
import com.ahwers.marvin.framework.resource.ResourceRepresentationType;

public abstract class ApplicationAdaptor {
	
	private Application application;
	
	protected abstract Application instantiateApplication();
	
	protected Application getApplication() {
		if (application == null) {
			application = instantiateApplication();
		}
		
		return this.application;
	}

	public String getApplicationName() {
		IntegratesApplication integrationAnnotation = getClass().getDeclaredAnnotation(IntegratesApplication.class);
		return integrationAnnotation.value();
	}
	
	protected MarvinApplicationResource buildResource(String message) {
		MarvinApplicationResource resource = new MarvinApplicationResource(ResourceRepresentationType.PLAIN_TEXT, message);
		return resource;
	}

}
