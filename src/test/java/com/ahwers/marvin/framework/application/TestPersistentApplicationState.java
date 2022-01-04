package com.ahwers.marvin.framework.application;

public class TestPersistentApplicationState extends ApplicationState {

    private String test = null;

    public TestPersistentApplicationState() {}

    public TestPersistentApplicationState(String appName, Integer stateVersion) {
        super(appName, stateVersion);
    }

    public TestPersistentApplicationState(String encodedState) {
        if (encodedState.equals("test_encoded_state")) {
            this.test = "test";
        }
        else if (encodedState.equals("new_test_encoded_state")) {
            this.test = "new_test";
        }
    }

    public String getTest() {
        return this.test;
    }

    @Override
    public boolean isSameAs(ApplicationState appState) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String encode() {
        // TODO Auto-generated method stub
        return null;
    }

}
