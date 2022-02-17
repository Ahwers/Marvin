package com.ahwers.marvin.framework.application;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import com.ahwers.marvin.testapplications.TestApplication;

import org.junit.jupiter.api.Test;

public class ApplicationRepositoryTest {

    // TODO: Test case for not loading erroneous application class
    // TODO: Test case for null package route argument
    
    @Test
    public void getApplicationsValid() {
        Set<Class<?>> expectedApplicationClasses = Set.of(TestApplication.class);
        Set<Application> actualApplications = ApplicationRepository.getMarvinApplicationsInPackage("com.ahwers.marvin.testapplications");
        
        assertAll(
            () -> assertTrue(expectedApplicationClasses.size() == actualApplications.size()),
            () -> {
                for (Application application : actualApplications) {
                    assertTrue(expectedApplicationClasses.contains(application.getClass()));
                }
            }
        );
    }

}
