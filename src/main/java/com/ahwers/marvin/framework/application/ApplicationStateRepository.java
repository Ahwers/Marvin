package com.ahwers.marvin.framework.application;

public class ApplicationStateRepository {

    public static ApplicationStateRepository getInstance() {
        return new ApplicationStateRepository();
    }

    private ApplicationStateRepository() {}

    public ApplicationState getStateOfApp(String name) {
        return null;
    }

    public String getEncodedStateOfApp(String name) {
        return null;
    }

    public void saveState(String name, String encoded) {

    }

}
