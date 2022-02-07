package com.ahwers.marvin.framework.application.state;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MemoryBasedMarshalledApplicationStateRepositoryTests {

    @BeforeEach
    public void resetStates() {
        MemoryBasedMarshalledApplicationStateRepository stateRepo = MemoryBasedMarshalledApplicationStateRepository.getInstance();
        stateRepo.resetStates();
    }

    @Test
    public void getStateNoneExists() {
        MemoryBasedMarshalledApplicationStateRepository stateRepo = MemoryBasedMarshalledApplicationStateRepository.getInstance();
        String nonExistentState = stateRepo.getMarshalledStateOfApp("Non existent app");
        assertTrue(nonExistentState == null);
    }

    @Test
    public void getStateExistsAlready() {
        MemoryBasedMarshalledApplicationStateRepository stateRepo = MemoryBasedMarshalledApplicationStateRepository.getInstance();
        stateRepo.saveMarshalledStateUnderApplicationName("existing state", "Existing app with state");
        String state = stateRepo.getMarshalledStateOfApp("Existing app with state");
        assertTrue(state.equals("existing state"));
    }

    @Test
    public void saveState() {
        MemoryBasedMarshalledApplicationStateRepository stateRepo = MemoryBasedMarshalledApplicationStateRepository.getInstance();
        stateRepo.saveMarshalledStateUnderApplicationName("saved state", "Existing app with updated state");
        String updatedState = stateRepo.getMarshalledStateOfApp("Existing app with updated state");
        assertTrue(updatedState.equals("saved state"));
    }
    
}
