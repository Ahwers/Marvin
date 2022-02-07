package com.ahwers.marvin.framework.application.state;

public class TestApplicationState extends ApplicationState {

    private String test = "test";

    public TestApplicationState() {}

    public TestApplicationState(String appName, Integer stateVersion) {
        super(appName, stateVersion);
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

}
