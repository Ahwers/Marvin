package com.ahwers.marvin.framework.application;

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
