package com.ahwers.marvin.testapplications;

import com.ahwers.marvin.framework.application.Application;
import com.ahwers.marvin.framework.application.annotations.IntegratesApplication;
import com.ahwers.marvin.framework.application.annotations.Stateful;
import com.ahwers.marvin.framework.application.state.TestApplicationState;

@IntegratesApplication("Test Application")
@Stateful(TestApplicationState.class)
public class TestApplication extends Application {

}
