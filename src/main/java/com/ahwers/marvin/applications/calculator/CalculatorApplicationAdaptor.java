package com.ahwers.marvin.applications.calculator;

import java.util.Map;

import com.ahwers.marvin.applications.Application;
import com.ahwers.marvin.applications.ApplicationAdaptor;
import com.ahwers.marvin.applications.CommandMatch;
import com.ahwers.marvin.applications.IntegratesApplication;
import com.ahwers.marvin.response.MarvinResponse;
import com.ahwers.marvin.response.RequestOutcome;

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
	public MarvinResponse simpleAddition(Map<String, String> arguments) {
		String first = arguments.get("first");
		String second = arguments.get("second");
		Integer answer = Integer.valueOf(first) + Integer.valueOf(second);
		
		return new MarvinResponse(RequestOutcome.SUCCESS, answer.toString());
	}
	
	@CommandMatch("^test$")
	@CommandMatch("^what does 2 plus 2 equal$")
	public MarvinResponse rightthink(Map<String, String> arguments) {
		return new MarvinResponse(RequestOutcome.SUCCESS, "5");
	}

}
