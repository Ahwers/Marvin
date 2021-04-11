package com.ahwers.marvin.applications.calendar;

import java.util.Map;

import com.ahwers.marvin.CommandOutcome;
import com.ahwers.marvin.MarvinResponse;
import com.ahwers.marvin.applications.Application;
import com.ahwers.marvin.applications.ApplicationAdaptor;
import com.ahwers.marvin.applications.CommandMatch;
import com.ahwers.marvin.applications.IntegratesApplication;

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
		return new MarvinResponse(CommandOutcome.SUCCESS, "Works");
	}

}
