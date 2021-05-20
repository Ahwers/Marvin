package com.ahwers.marvin.applications.graphicalcalculator;

import java.util.Map;

import com.ahwers.marvin.MarvinResponse;
import com.ahwers.marvin.Resource;
import com.ahwers.marvin.ResourceRepresentationType;
import com.ahwers.marvin.CommandOutcome;
import com.ahwers.marvin.applications.Application;
import com.ahwers.marvin.applications.ApplicationAdaptor;
import com.ahwers.marvin.applications.CommandMatch;
import com.ahwers.marvin.applications.IntegratesApplication;

@IntegratesApplication("Graphical Calculator")
public class GraphicalCalculatorApplicationAdaptor extends ApplicationAdaptor {
	
	private AlgebraicExpressionProcessor expressionProcessor;
	
	public GraphicalCalculatorApplicationAdaptor() {
		this.expressionProcessor = AlgebraicExpressionProcessor.getInstance();
	}
	
	@Override
	protected Application instantiateApplication() {
		return DesmosGraphicalCalculator.getInstance();
	}
	
	@CommandMatch("^open graphical calculator$")
	public MarvinResponse openGraphicalCalculator(Map<String, String> arguments) {
		MarvinResponse response = new MarvinResponse(CommandOutcome.SUCCESS);
		response.setResource(buildGraphicalCalculatorResource());
		
		return response;
	}
	
	@CommandMatch("^plot (?<expression>.+)$")
	public MarvinResponse addNewAlgebraicExpression(Map<String, String> arguments) {
		String processedExpression = expressionProcessor.processExpressionIntoAlgebraicExpression(arguments.get("expression"));
		
		DesmosGraphicalCalculator graphicalCalculator = (DesmosGraphicalCalculator) getApplication();
		graphicalCalculator.addNewExpression(processedExpression);
			
		MarvinResponse response = new MarvinResponse(CommandOutcome.SUCCESS);
		response.setResource(buildGraphicalCalculatorResource());
		
		return response;
	}
	
	// TODO: Should I make it so that ^ and $ are not required for any matches? Yes. Actually nah, let's stay flexible until we see that the flexibility is not required.
	@CommandMatch("^remove (?:graph|expression) (?<graphIndex>.+)$")
	public MarvinResponse removeAlgebraicExpression(Map<String, String> arguments) {
		String expressionIndex = arguments.get("graphIndex");
		
		DesmosGraphicalCalculator graphicalCalculator = (DesmosGraphicalCalculator) getApplication();
		
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
		
		response.setResource(buildGraphicalCalculatorResource());

		return response;
	}
	
	private Resource buildGraphicalCalculatorResource() {
		DesmosGraphicalCalculator graphicalCalculator = (DesmosGraphicalCalculator) getApplication();
		
		int previousStateId = graphicalCalculator.getPreviousStateId();
		int currentStateId = graphicalCalculator.getCurrentStateId();

		Resource graphicalCalculatorResource = new Resource(getApplicationName(), currentStateId, previousStateId);
		graphicalCalculatorResource.addRepresentation(ResourceRepresentationType.HTML, graphicalCalculator.getHtmlRepresentation());
		graphicalCalculatorResource.addRepresentation(ResourceRepresentationType.HTML_STATE_INSTANTIATION_SCRIPT, graphicalCalculator.getHtmlStateInstantiationScript());
		graphicalCalculatorResource.addRepresentation(ResourceRepresentationType.HTML_STATE_UPDATE_SCRIPT, graphicalCalculator.getHtmlStateUpdateScriptFromPreviousStateId());

		return graphicalCalculatorResource;
	}

}
