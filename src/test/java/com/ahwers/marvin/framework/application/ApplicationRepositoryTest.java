package com.ahwers.marvin.framework.application;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import com.ahwers.marvin.testapplications.TestApplication;

import org.junit.jupiter.api.Test;

public class ApplicationRepositoryTest {

    // TODO: Test case for loading erroneous application class
    
    @Test
    public void getApplications() {
        Set<Class<?>> expectedApplicationClasses = Set.of(TestApplication.class);

        ApplicationRepository appRepo = new ApplicationRepository("com.ahwers.marvin.testapplications");
        Set<Application> actualApplications = appRepo.getSupportedApplications();
        
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
