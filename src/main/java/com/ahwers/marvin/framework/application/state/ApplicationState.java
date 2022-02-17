package com.ahwers.marvin.framework.application.state;

public abstract class ApplicationState implements Cloneable {

    private String applicationName;
    private int version;

    public ApplicationState() {}

    public ApplicationState(String applicationName, Integer version) {
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
        else {
            if (this.version > appState.getVersion()) {
                isFresherThan = true;
            }
            else if (this.version == appState.getVersion()) {
                isFresherThan = (this.isSameAs(appState) == false);
            }
        }

        return isFresherThan;
    }

    public abstract boolean isSameAs(ApplicationState appState);

    @Override
    public ApplicationState clone() throws CloneNotSupportedException {
        ApplicationState clonedState = (ApplicationState) super.clone();
        return clonedState;
    }

}
