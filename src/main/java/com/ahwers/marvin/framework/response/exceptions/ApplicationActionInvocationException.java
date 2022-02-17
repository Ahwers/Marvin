package com.ahwers.marvin.framework.response.exceptions;

public class ApplicationActionInvocationException extends Exception {

    public ApplicationActionInvocationException(String errorMessage) {
        super(errorMessage);
    }

    public ApplicationActionInvocationException() {
        super();
    }

}
