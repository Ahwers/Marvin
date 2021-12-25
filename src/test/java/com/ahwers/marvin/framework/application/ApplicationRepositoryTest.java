package com.ahwers.marvin.framework.application;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import com.ahwers.marvin.applications.standard.todo.task_creation.TaskCreationApplication;
import com.ahwers.marvin.applications.test.ServiceTestApplication;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class ApplicationRepositoryTest {
    
    private ApplicationRepository appRepo;

    @BeforeAll
    public void instantiateAppRepo() {
        appRepo = ApplicationRepository.getInstance();
    }

    @Test
    public void getStandardApplications() {
        Set<Class<?>> expectedApplicationClasses = new HashSet<>();
        expectedApplicationClasses.add(TaskCreationApplication.class);

        Set<Application> actualApplications = appRepo.getStandardApplications();
        
        assertAll(
            () -> assertTrue(expectedApplicationClasses.size() == actualApplications.size()),
            () -> {
                int i = 0;
                for (Application application : actualApplications) {
                    assertTrue(expectedApplicationClasses.contains(application.getClass()));
                    i++;
                }
            }
        );
    }

    @Test
    public void getTestApplications() {
        Set<Class<?>> expectedApplicationClasses = new HashSet<>();
        expectedApplicationClasses.add(ServiceTestApplication.class);

        Set<Application> actualApplications = appRepo.getTestApplications();
        
        assertAll(
            () -> assertTrue(expectedApplicationClasses.size() == actualApplications.size()),
            () -> {
                int i = 0;
                for (Application application : actualApplications) {
                    assertTrue(expectedApplicationClasses.contains(application.getClass()));
                    i++;
                }
            }
        );
    }

}
