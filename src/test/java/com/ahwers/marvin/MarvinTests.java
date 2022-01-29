package com.ahwers.marvin;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import com.ahwers.marvin.framework.Marvin;
import com.ahwers.marvin.framework.application.Application;
import com.ahwers.marvin.framework.application.annotations.IntegratesApplication;
import com.ahwers.marvin.framework.application.annotations.Stateful;
import com.ahwers.marvin.framework.application.state.ApplicationStateFactory;
import com.ahwers.marvin.framework.application.state.TestApplicationState;

import org.junit.jupiter.api.Test;

public class MarvinTests {

    // TODO: Implement
    /**
     * Test Cases:
     *  - Invalid, no applications loaded
     */

    @IntegratesApplication("Test")
    @Stateful(TestApplicationState.class)
    private class TestApplication extends Application {

    }

    @Test
    public void getApplicationStateFactory() {
        Set<Application> testApplications = Set.of(new TestApplication());
        Marvin marvin = new Marvin(testApplications);

        Set<Class<? extends Application>> expectedApplicationClasses = Set.of(TestApplication.class);
        
        ApplicationStateFactory factory = marvin.getApplicationStateFactory();
        
        Set<Class<? extends Application>> actualApplicationClasses = new HashSet<>();
        for (Application app : factory.getSupportedApplications()) {
            actualApplicationClasses.add(app.getClass());
        }

        assertAll(
            () -> assertTrue(expectedApplicationClasses.size() == actualApplicationClasses.size()),
            () -> {
                for (Class<?> appClass : expectedApplicationClasses) {
                    assertTrue(actualApplicationClasses.contains(appClass));
                }
            }
        );
    }
    
}
