package com.ahwers.marvin.testapplications;

import java.util.Map;

import com.ahwers.marvin.framework.application.Application;
import com.ahwers.marvin.framework.application.action.annotations.CommandMatch;
import com.ahwers.marvin.framework.application.annotations.IntegratesApplication;
import com.ahwers.marvin.framework.application.resource.ApplicationResource;

@IntegratesApplication("Invalid Application Private Action")
public class InvalidApplicationPrivateAction extends Application {
    
    @CommandMatch("invalid because private")
    private ApplicationResource invalidBecausePrivate(Map<String, String> arguments) {
        return null;
    }

}
