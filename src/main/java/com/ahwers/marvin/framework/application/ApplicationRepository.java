package com.ahwers.marvin.framework.application;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

import com.ahwers.marvin.framework.application.annotations.IntegratesApplication;

import org.reflections.Reflections;

public class ApplicationRepository {
    
    private static ApplicationRepository instance;

    public static ApplicationRepository getInstance() {
        if (instance == null) {
            instance = new ApplicationRepository();
        } 

        return instance;
    }

	private final String MARVIN_APPLICATION_PACKAGE_PREFIX = "com.ahwers.marvin";

    // TODO: I think by naming a lot of this "standard", i am leaving space for testApps. If i don't need that, rename all of this to just app
    private Set<Application> standardApps;

    private ApplicationRepository() {
        this.standardApps = loadStandardApps();
    }

    private Set<Application> loadStandardApps() {
        Set<Application> apps = new HashSet<>();

        Set<Class<?>> applicationClasses = new Reflections(MARVIN_APPLICATION_PACKAGE_PREFIX).getTypesAnnotatedWith(IntegratesApplication.class);
		for (Class<?> appClass : applicationClasses) {
			Application application = null;
			try {
				application = (Application) appClass.getDeclaredConstructor().newInstance();
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
                // TODO: Throw configuration error
				e.printStackTrace();
			}

            apps.add(application);
		}
        
        return apps;
    }

    public Set<Application> getStandardApplications() {
        return standardApps;
    }

}
