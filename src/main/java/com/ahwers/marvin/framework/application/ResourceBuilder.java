package com.ahwers.marvin.framework.application;

import com.ahwers.marvin.framework.resource.MarvinApplicationResource;

public interface ResourceBuilder {
   
    public MarvinApplicationResource buildResourceForApplication(String applicationName);

}
