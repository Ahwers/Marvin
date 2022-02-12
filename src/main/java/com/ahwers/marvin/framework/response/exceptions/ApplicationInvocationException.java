package com.ahwers.marvin.framework.response.exceptions;

// TODO: Better name?
public class ApplicationInvocationException extends Exception {

    public ApplicationInvocationException(String errorMessage) {
        super(errorMessage);
    }

    public ApplicationInvocationException() {
        super();
    }

}
