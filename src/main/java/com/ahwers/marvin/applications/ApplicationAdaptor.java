package com.ahwers.marvin.applications;

import java.util.Map;

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
	
}
