package com.ahwers.marvin.framework.application.state.interfaces;

public interface MarshalledApplicationStateRepository {

    public String getMarshalledStateOfApp(String appName);

    public void saveMarshalledStateUnderApplicationName(String marshalledState, String appName);

}
