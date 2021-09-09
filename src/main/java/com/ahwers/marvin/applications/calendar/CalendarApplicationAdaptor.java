package com.ahwers.marvin.applications.calendar;

import java.util.Map;

import com.ahwers.marvin.framework.application.Application;
import com.ahwers.marvin.framework.application.ApplicationAdaptor;
import com.ahwers.marvin.framework.application.CommandMatch;
import com.ahwers.marvin.framework.application.IntegratesApplication;
import com.ahwers.marvin.framework.response.MarvinResponse;
import com.ahwers.marvin.framework.response.RequestOutcome;

@IntegratesApplication("Calendar")
public class CalendarApplicationAdaptor extends ApplicationAdaptor {

	@Override
	protected Application instantiateApplication() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@CommandMatch("^remind me to (?<action>.+)(?: on (?<date>.+))?(?: at (?<time>.+))$")
	public MarvinResponse reminderAtDateTime(Map<String, String> arguments) {
		return null;
	}
	
	@CommandMatch("^in (?<timerLength>.+?) (?<timerUnit>minutes?|hours?|seconds?) remind me to (?<action>.+)$")
	@CommandMatch("^remind me to (?<action>.+) in (?<timerLength>.+) (?<timerUnit>minutes?|hours?|seconds?)$")
	public MarvinResponse reminderOnTimer(Map<String, String> arguments) {
		return new MarvinResponse(RequestOutcome.SUCCESS, "Works");
	}

}
