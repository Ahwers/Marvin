package com.ahwers.marvin.framework.application;

public class TestApplicationState extends ApplicationState {

    private String test = "test";

    public TestApplicationState() {}

    public TestApplicationState(String appName, Integer stateVersion) {
        super(appName, stateVersion);
    }

    public TestApplicationState(String encodedState) {
        super(encodedState);
    }

    @Override
    public boolean isSameAs(ApplicationState appState) {
        TestApplicationState castedApplicationState = (TestApplicationState) appState;
        return castedApplicationState.getTest().equals(this.test);
    }

    public String getTest() {
        return this.test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    @Override
    public String encode() {
        return null;
    }

}
