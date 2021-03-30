package com.ahwers.marvin.applications.graphicalcalculator;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.ahwers.marvin.MarvinResponse;
import com.ahwers.marvin.Resource;
import com.ahwers.marvin.ResourceTemplate;
import com.ahwers.marvin.ResourceType;
import com.ahwers.marvin.CommandStatus;
import com.ahwers.marvin.applications.Application;
import com.ahwers.marvin.applications.ApplicationAdaptor;
import com.ahwers.marvin.applications.CommandMatch;
import com.ahwers.marvin.applications.IntegratesApplication;

// TODO: Maybe this should just absorb GraphicalCalculator.
//		 Furthermore, can we get rid of application stuff from this whole class but still allow classes that would like Application style classes to exist to still use them?
@IntegratesApplication("Graphical Calculator")
public class GraphicalCalculatorApplicationAdaptor extends ApplicationAdaptor {
	
	private AlgebraicExpressionProcessor expressionProcessor;
	
	public GraphicalCalculatorApplicationAdaptor() {
		this.expressionProcessor = AlgebraicExpressionProcessor.getInstance();
	}
	
	@Override
	protected Application instantiateApplication() {
		return new GraphicalCalculatorState();
	}
	
	@CommandMatch("^plot (?<expression>.+)$")
	public MarvinResponse addNewAlgebraicExpression(Map<String, String> arguments) {
		String processedExpression = expressionProcessor.processExpressionIntoAlgebraicExpression(arguments.get("expression"));
		
		GraphicalCalculatorState graphicalCalculator = (GraphicalCalculatorState) getApplication();
		int newExpressionId = graphicalCalculator.addNewExpression(processedExpression);
		
		MarvinResponse response = new MarvinResponse(CommandStatus.SUCCESS);
		
		// TODO: Pop "Graphical Calculator" in a final private variable
		response.addResource(new Resource(graphicalCalculator.getStateId(), ResourceType.HTML, getHtmlResourceForCalculatorState(), "Graphical Calculator"));
		
		// TODO: Doesn't work
		String javascriptTransmformationScript = "calculator.setExpression({ id: '" + newExpressionId + "', latex: '" + processedExpression + "' });";
		response.addResource(new Resource(graphicalCalculator.getStateId(), ResourceType.JAVASCRIPT_UPDATE_SCRIPT, javascriptTransmformationScript, "Graphical Calculator"));
		
		return response;
	}
	
	private String getHtmlResourceForCalculatorState() {
		ResourceTemplate calculatorTemplate = new ResourceTemplate("/graphical_calculator.html");
		Map<String, String> templateData = new HashMap<>();
		templateData.put("CALCULATOR_SET_UP_SCRIPT", getCalculatorSetUpScriptForCalculatorState());
		return calculatorTemplate.mergeDataWithResourceTemplate(templateData);
	}
	
	private String getCalculatorSetUpScriptForCalculatorState() {
		GraphicalCalculatorState state = (GraphicalCalculatorState) getApplication();
		
		int stateId = state.getStateId();
		Map<Integer, String> expressions = state.getExpressions();
		int focusX = state.getFocusX();
		int focusY = state.getFocusY();
		
		String script = "var stateId = " + stateId + ";\n"
				      + "var focusX = " + focusX + ";\n"
				      + "var focusY = " + focusY + ";\n";
		for (Integer expressionId : expressions.keySet()) {
			script += "calculator.setExpression({ id: '" + expressionId + "', latex: '" + expressions.get(expressionId) +"' });\n";
		}
		
		return script;
	}
	
	// TODO: Should I make it so that ^ and $ are not required for any matches? Yes. Actually nah, let's stay flexible until we see that the flexibility is not required.
	@CommandMatch("^remove (?:graph|expression) (?<graphIndex>.+)$")
	public MarvinResponse removeAlgebraicExpression(Map<String, String> arguments) {
		String expressionIndex = arguments.get("graphIndex");
		
		GraphicalCalculatorState graphicalCalculator = (GraphicalCalculatorState) getApplication();
		
		MarvinResponse response = new MarvinResponse();
		try {
			graphicalCalculator.removeExpression(Integer.valueOf(expressionIndex));
		} catch (NumberFormatException e) {
			response.setResponseMessage("\"Graphical Calculator\" graphs are indexed by integers and the non integer key (" + expressionIndex + ") for \"graphIndex\" was provided.");
			response.setFailException(e);
			response.setCommandStatus(CommandStatus.FAILED);
		} catch (IndexOutOfBoundsException e) {
			response.setResponseMessage("Graph " + expressionIndex + "doesn't exist.");
			response.setFailException(e);
			response.setCommandStatus(CommandStatus.FAILED);
		}
		
		if (response.getCommandStatus() == null) {
			response.setCommandStatus(CommandStatus.SUCCESS);
		}
		
		response.addResource(new Resource(graphicalCalculator.getStateId(), ResourceType.HTML, getHtmlResourceForCalculatorState(), "Graphical Calculator"));
		
		// TODO: JavaScript transfmormation resource
		
		return response;
	}
	
}
