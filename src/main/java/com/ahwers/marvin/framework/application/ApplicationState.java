package com.ahwers.marvin.framework.application;

public abstract class ApplicationState {

    private String applicationName;
    private int version;

    public ApplicationState() {}

    public ApplicationState(String applicationName, int version) {
        this.applicationName = applicationName;
        this.version = version;
    }

    public String getApplicationName() {
        return this.applicationName;
    }

    public int getVersion() {
        return this.version;
    }

    public void incrementVersion() {
        this.version++;
    }

    public boolean isFresherThan(ApplicationState appState) {
        boolean isFresherThan = false;

        if (appState == null) {
            isFresherThan = true;
        }
        if (this.version > appState.getVersion()) {
            isFresherThan = true;
        }
        else if (this.version == appState.getVersion()) {
            isFresherThan = (this.isSameAs(appState) == false);
        }

        return isFresherThan;
    }

    public abstract boolean isSameAs(ApplicationState appState);

}