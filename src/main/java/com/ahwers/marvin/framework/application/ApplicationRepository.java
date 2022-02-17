package com.ahwers.marvin.framework.application;

import java.util.HashSet;
import java.util.Set;

import com.ahwers.marvin.framework.application.annotations.IntegratesApplication;
import com.ahwers.marvin.framework.application.exceptions.ApplicationConfigurationError;

import org.reflections.Reflections;

public class ApplicationRepository {

    public static Set<Application> getMarvinApplicationsInPackage(String packageRoute) {
        if (packageRoute == null) {
            throw new IllegalArgumentException("packageRoute argument cannot be null.");
        }

        Set<Application> apps = new HashSet<>();

        Set<Class<?>> applicationClasses = new Reflections(packageRoute).getTypesAnnotatedWith(IntegratesApplication.class);
		for (Class<?> appClass : applicationClasses) {
			Application application = null;
			try {
				application = (Application) appClass.getDeclaredConstructor().newInstance();
			} catch (Exception e) {
                throw new ApplicationConfigurationError(e.getMessage());
			}

            if (application != null) {
                apps.add(application);
            }
		}
        
        return apps;
    }

}
