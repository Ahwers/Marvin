package com.ahwers.marvin.applications.graphicalcalculator;

import java.util.HashMap;
import java.util.Map;

import com.ahwers.marvin.MarvinResponse;
import com.ahwers.marvin.Resource;
import com.ahwers.marvin.ResourceTemplate;
import com.ahwers.marvin.ResourceRepresentationType;
import com.ahwers.marvin.CommandOutcome;
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
		
		MarvinResponse response = new MarvinResponse(CommandOutcome.SUCCESS);
		int newExpressionId = graphicalCalculator.addNewExpressionAndGetId(processedExpression);
		
		Resource graphicalCalculatorResource = buildGraphicalCalculatorResourceWithHtmlAndInstantiationRepresentations();
		
		String htmlStateUpdateScript = "calculator.setExpression({ id: '" + newExpressionId + "', latex: '" + processedExpression + "' });";
		graphicalCalculatorResource.addRepresentation(ResourceRepresentationType.HTML_STATE_UPDATE_SCRIPT, htmlStateUpdateScript);
		
		response.setResource(graphicalCalculatorResource);
		
		return response;
	}
	
	private Resource buildGraphicalCalculatorResourceWithHtmlAndInstantiationRepresentations() {
		GraphicalCalculatorState graphicalCalculator = (GraphicalCalculatorState) getApplication();
		
		int currentStateId = graphicalCalculator.getCurrentStateId();
		int previousStateId = graphicalCalculator.getPreviousStateId();
		
		Resource graphicalCalculatorResource = new Resource(getApplicationName(), currentStateId, previousStateId);
		graphicalCalculatorResource.addRepresentation(ResourceRepresentationType.HTML, getHtmlRepresentationForCalculatorState());
		graphicalCalculatorResource.addRepresentation(ResourceRepresentationType.HTML_STATE_INSTANTIATION_SCRIPT, getCalculatorSetUpScript());
		
		return graphicalCalculatorResource;
	}
	
	private String getHtmlRepresentationForCalculatorState() {
		ResourceTemplate calculatorTemplate = new ResourceTemplate("graphical_calculator");
		Map<String, String> templateData = new HashMap<>();
		templateData.put("CALCULATOR_SET_UP_SCRIPT", getCalculatorSetUpScript());
		return calculatorTemplate.mergeDataWithResourceTemplate(templateData);
	}
	
	private String getCalculatorSetUpScript() {
		GraphicalCalculatorState state = (GraphicalCalculatorState) getApplication();
		
		int stateId = state.getCurrentStateId();
		Map<Integer, String> expressions = state.getExpressions();
		int focusX = state.getFocusX();
		int focusY = state.getFocusY();
		
		String script = "var stateId = " + stateId + ";\n"
				      + "var focusX = " + focusX + ";\n"
				      + "var focusY = " + focusY + ";\n"
				      + "calculator.setBlank();\n";
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
		
		MarvinResponse response = new MarvinResponse(CommandOutcome.SUCCESS);
		try {
			graphicalCalculator.removeExpression(Integer.valueOf(expressionIndex));
		} catch (NumberFormatException e) {
			response = new MarvinResponse(CommandOutcome.FAILED, "\"Graphical Calculator\" graphs are indexed by integers and the non integer key (" + expressionIndex + ") for \"graphIndex\" was provided.");
			response.setFailException(e);
		} catch (IndexOutOfBoundsException e) {
			response = new MarvinResponse(CommandOutcome.FAILED, "Graph " + expressionIndex + "doesn't exist.");
			response.setFailException(e);
		}
		
		Resource graphicalCalculatorResource = buildGraphicalCalculatorResourceWithHtmlAndInstantiationRepresentations();
		// TODO: JavaScript transformation resource
		
		response.setResource(graphicalCalculatorResource);

		return response;
	}
	
}
