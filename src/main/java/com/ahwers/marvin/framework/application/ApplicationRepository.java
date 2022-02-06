package com.ahwers.marvin.framework.application;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

import com.ahwers.marvin.framework.application.annotations.IntegratesApplication;

import org.reflections.Reflections;

public class ApplicationRepository {

    private Set<Application> loadedApps;

    public ApplicationRepository(String packageRoute) {
        this.loadedApps = getAppsInPackage(packageRoute);
    }

    private Set<Application> getAppsInPackage(String packageRoute) {
        Set<Application> apps = new HashSet<>();

        Set<Class<?>> applicationClasses = new Reflections(packageRoute).getTypesAnnotatedWith(IntegratesApplication.class);
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

    public Set<Application> getSupportedApplications() {
        return loadedApps;
    }

}
