package com.ahwers.marvin.applications.calculator;

import java.util.Map;

import com.ahwers.marvin.framework.application.Application;
import com.ahwers.marvin.framework.application.ApplicationAdaptor;
import com.ahwers.marvin.framework.application.CommandMatch;
import com.ahwers.marvin.framework.application.IntegratesApplication;
import com.ahwers.marvin.framework.response.MarvinResponse;
import com.ahwers.marvin.framework.response.RequestOutcome;

@IntegratesApplication("Calculator")
public class CalculatorApplicationAdaptor extends ApplicationAdaptor {
	
	// TODO: Instead make it accept general stuff like: "Calculate <ALGEBRAIC_EXPRESSION> for <VARIABLE_DEFINITION>" where expression could be "x^2+2x+3" and variable definition is "x=2"
	// TODO: "Solve <EXPRESSION> for <VARIABLE>" Maybe just "Solve <EXPRESSION>" where <EXPRESSION> is "3x+1 for x" and let wolfram do the heavy lifting
	
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
