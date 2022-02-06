package com.ahwers.marvin.testapplications;

import java.util.Map;

import com.ahwers.marvin.framework.application.Application;
import com.ahwers.marvin.framework.application.action.annotations.CommandMatch;
import com.ahwers.marvin.framework.application.annotations.IntegratesApplication;
import com.ahwers.marvin.framework.application.annotations.Stateful;
import com.ahwers.marvin.framework.application.state.TestApplicationState;
import com.ahwers.marvin.framework.resource.MarvinApplicationResource;

@IntegratesApplication("Test Application")
@Stateful(TestApplicationState.class)
public class TestApplication extends Application {

}
