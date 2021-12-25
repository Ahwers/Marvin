package com.ahwers.marvin.framework.application;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

import com.ahwers.marvin.framework.application.annotations.IntegratesApplication;

import org.reflections.Reflections;

public class ApplicationRepository {

    // TODO: Test suite, standard apps and test apps.

    private static ApplicationRepository instance;

    public static ApplicationRepository getInstance() {
        if (instance == null) {
            instance = new ApplicationRepository();
        } 

        return instance;
    }

    // TODO: Figure out how to store the test applications in the actual test packages
	private final String MARVIN_STANDARD_APPLICATION_PACKAGE_PREFIX = "com.ahwers.marvin.applications.standard";
    private final String MARVIN_TEST_APPLICATION_PACKAGE_PREFIX = "com.ahwers.marvin.applications.test";

    // TODO: I feel like storing test apps even in production environments could be a security risk. Rethink how to do this.
    private Set<Application> standardApps;
    private Set<Application> testApps;

    private ApplicationRepository() {
        this.standardApps = loadStandardApps();
        this.testApps = loadTestApps();
    }

    private Set<Application> loadStandardApps() {
        return getAppsInPackage(MARVIN_STANDARD_APPLICATION_PACKAGE_PREFIX);
    }

    private Set<Application> loadTestApps() {
        return getAppsInPackage(MARVIN_TEST_APPLICATION_PACKAGE_PREFIX);
    }

    private Set<Application> getAppsInPackage(String packagePrefix) {
        Set<Application> apps = new HashSet<>();

        Set<Class<?>> applicationClasses = new Reflections(packagePrefix).getTypesAnnotatedWith(IntegratesApplication.class);
		for (Class<?> appClass : applicationClasses) {
			Application application = null;
			try {
				application = (Application) appClass.getDeclaredConstructor().newInstance();
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
                // TODO: Throw configuration error. OR can application's validation method catch this? Would be better to focus catching application configuration errors in a single place no? 
				e.printStackTrace();
			}

            apps.add(application);
		}
        
        return apps;
    }

    public Set<Application> getStandardApplications() {
        return standardApps;
    }

    public Set<Application> getTestApplications() {
        return testApps;
    }

}
