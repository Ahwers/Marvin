package com.ahwers.marvin.framework.application.exceptions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ApplicationConfigurationError extends Error {

	private static Logger logger = LogManager.getLogger(ApplicationConfigurationError.class);

    public ApplicationConfigurationError(String errorMessage) {
        super(errorMessage);
        logger.error(errorMessage);
    }

    public ApplicationConfigurationError(Throwable e) {
        logger.error("Exception thrown: '" + e.getClass() + "'. Message: " + e.getMessage());
    }

}
