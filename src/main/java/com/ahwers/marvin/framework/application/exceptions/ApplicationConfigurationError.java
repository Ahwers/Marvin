package com.ahwers.marvin.framework.application.exceptions;

// TODO: I think this should extend from Exception instead. Read the whole exceptions chapter then refactor.
public class ApplicationConfigurationError extends Error {

    public ApplicationConfigurationError(String error) {
        super(error);
    }

}
