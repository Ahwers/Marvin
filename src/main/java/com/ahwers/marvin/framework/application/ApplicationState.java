package com.ahwers.marvin.framework.application;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class ApplicationState implements Cloneable {

    private String applicationName;
    private int version;

    public ApplicationState() {}

    public ApplicationState(String encoded) {}

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

    public String encode() throws JsonProcessingException, UnsupportedEncodingException {
        ObjectMapper jsonMapper = new ObjectMapper();
        String marshalledAppState = jsonMapper.writeValueAsString(this);
        String encodedAppState = Base64.getEncoder().encodeToString(marshalledAppState.getBytes(StandardCharsets.UTF_8.toString()));  

        return encodedAppState;
    }

    @Override
    public ApplicationState clone() throws CloneNotSupportedException {
        ApplicationState clonedState = (ApplicationState) super.clone();
        return clonedState;
    }

}
