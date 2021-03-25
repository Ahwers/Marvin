package com.ahwers.marvin.applications.calculator;

import java.util.Map;

import com.ahwers.marvin.CommandExecutionOutcome;
import com.ahwers.marvin.CommandStatus;
import com.ahwers.marvin.applications.Application;
import com.ahwers.marvin.applications.ApplicationAdaptor;
import com.ahwers.marvin.applications.CommandMatch;
import com.ahwers.marvin.applications.IntegratesApplication;

@IntegratesApplication("Calculator")
public class CalculatorApplicationAdaptor extends ApplicationAdaptor {
	
	// TODO: Integrate wolfram alpha to implement commands such as "solve <equation> for x".
	@Override
	protected Application instantiateApplication() {
		// TODO Auto-generated method stub
		return null;
	}
	
	// TODO: What is the soundex of "+"? Does it equal "plus"?
	@CommandMatch("^(?:whats|what is) (?<first>.+?) (?:add|plus|\\+) (?<second>.+)$")
	public CommandExecutionOutcome simpleAddition(Map<String, String> arguments) {
		String first = arguments.get("first");
		String second = arguments.get("second");
		Integer answer = Integer.valueOf(first) + Integer.valueOf(second);
		
		return new CommandExecutionOutcome(CommandStatus.SUCCESS, answer.toString());
	}

}
