package com.ahwers.marvin.framework.application.state;

import java.util.HashMap;
import java.util.Map;

import com.ahwers.marvin.framework.application.state.interfaces.MarshalledApplicationStateRepository;

public class MemoryBasedMarshalledApplicationStateRepository implements MarshalledApplicationStateRepository {

    private static MemoryBasedMarshalledApplicationStateRepository instance;

    public static MemoryBasedMarshalledApplicationStateRepository getInstance() {
        if (instance == null) {
            instance = new MemoryBasedMarshalledApplicationStateRepository();
        }

        return instance;
    }

    private MemoryBasedMarshalledApplicationStateRepository() {}

    private Map<String, String> states = new HashMap<>();

    @Override
    public String getMarshalledStateOfApp(String appName) {
        return states.get(appName);
    }

    @Override
    public void saveMarshalledStateUnderApplicationName(String marshalledState, String appName) {
        states.put(appName, marshalledState);
    }

    public void resetStates() {
        this.states = new HashMap<>();
    }

}
