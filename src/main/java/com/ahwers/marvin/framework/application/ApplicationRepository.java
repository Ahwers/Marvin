package com.ahwers.marvin.framework.application;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

import com.ahwers.marvin.framework.application.annotations.IntegratesApplication;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;

public class ApplicationRepository {

	private static Logger logger = LogManager.getLogger(ApplicationRepository.class);

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
                apps.add(application);
            } catch (InvocationTargetException e) {
                logger.error("The application class " + appClass.toString() + " could not be instantiated.\nException (Wrapped by InvocationTargetException): " + e.getCause().getClass().toString() + "\nMessage: " + e.getCause().getMessage());
			} catch (Exception e) {
                logger.error("The application class " + appClass.toString() + " could not be instantiated.\nException: " + e.getClass().toString() + "\nMessage: " + e.getMessage());
			}
		}
        
        return apps;
    }

}
