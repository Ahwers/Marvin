package com.ahwers.marvin.applications.graphicalcalculator;

import java.util.Map;

import com.ahwers.marvin.applications.Application;
import com.ahwers.marvin.applications.ApplicationAdaptor;
import com.ahwers.marvin.applications.CommandMatch;
import com.ahwers.marvin.applications.IntegratesApplication;
import com.ahwers.marvin.response.MarvinResponse;
import com.ahwers.marvin.response.RequestOutcome;
import com.ahwers.marvin.response.resource.Resource;
import com.ahwers.marvin.response.resource.ResourceRepresentationType;

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
		MarvinResponse response = new MarvinResponse(RequestOutcome.SUCCESS);
		response.setResource(buildGraphicalCalculatorResource());
		
		return response;
	}
	
	@CommandMatch("^plot (?<expression>.+)$")
	public MarvinResponse addNewAlgebraicExpression(Map<String, String> arguments) {
		DesmosGraphicalCalculator graphicalCalculator = (DesmosGraphicalCalculator) getApplication();
	
		String processedExpression = expressionProcessor.processExpressionIntoAlgebraicExpression(arguments.get("expression"));
		graphicalCalculator.addNewExpression(processedExpression);
			
		MarvinResponse response = new MarvinResponse(RequestOutcome.SUCCESS);
		response.setResource(buildGraphicalCalculatorResource());
		
		return response;
	}
	
	// TODO: Should I make it so that ^ and $ are not required for any matches? Yes. Actually nah, let's stay flexible until we see that the flexibility is not required.
	@CommandMatch("^remove (?:graph|expression) (?<graphIndex>.+)$")
	public MarvinResponse removeAlgebraicExpression(Map<String, String> arguments) {
		String expressionIndex = arguments.get("graphIndex");
		
		// TODO: If graphIndex is a word then this fails. Need to translate word numbers into their numerical representations.
		
		DesmosGraphicalCalculator graphicalCalculator = (DesmosGraphicalCalculator) getApplication();
		
		MarvinResponse response = new MarvinResponse(RequestOutcome.SUCCESS);
		try {
			graphicalCalculator.removeExpression(Integer.valueOf(expressionIndex));
		} catch (NumberFormatException e) {
			response = new MarvinResponse(RequestOutcome.FAILED, "\"Graphical Calculator\" graphs are indexed by integers and the non integer key (" + expressionIndex + ") for \"graphIndex\" was provided.");
			response.setFailException(e);
		} catch (IndexOutOfBoundsException e) {
			response = new MarvinResponse(RequestOutcome.FAILED, "Graph " + expressionIndex + "doesn't exist.");
			response.setFailException(e);
		}
		
		response.setResource(buildGraphicalCalculatorResource());

		return response;
	}

	@CommandMatch("^remove all (?:expressions|graphs)$")
	public MarvinResponse removeAllAlgebraicExpressions(Map<String, String> arguments) {
		DesmosGraphicalCalculator graphicalCalculator = (DesmosGraphicalCalculator) getApplication();

		graphicalCalculator.removeAllExpressions();

		MarvinResponse response = new MarvinResponse(RequestOutcome.SUCCESS);
		response.setResource(buildGraphicalCalculatorResource());

		return response;
	}

	@CommandMatch("^replace (?:expression|graph) (?<graphIndex>.+?) with (?<newExpression>.+?)$")
	public MarvinResponse replaceExpressionOfIndexWithNewExpression(Map<String, String> arguments) {
		DesmosGraphicalCalculator graphicalCalculator = (DesmosGraphicalCalculator) getApplication();
		
		int graphIndex = Integer.valueOf(arguments.get("graphIndex"));
		String newExpression = arguments.get("newExpression");
		
		String newAlgebraicExperession = this.expressionProcessor.processExpressionIntoAlgebraicExpression(newExpression);
		graphicalCalculator.replaceExpressionWith(graphIndex, newAlgebraicExperession);
		
		MarvinResponse response = new MarvinResponse(RequestOutcome.SUCCESS);
		response.setResource(buildGraphicalCalculatorResource());
		
		return response;
	}

	@CommandMatch("^replace (?<targetExpression>.+?) in (?:expression|graph) (?<graphIndex>.+?) with (?<replacementExpression>.+?)$")
	public MarvinResponse replaceSubstringInAlgebraicExpressionWith(Map<String, String> arguments) {
		DesmosGraphicalCalculator graphicalCalculator = (DesmosGraphicalCalculator) getApplication();

		int expressionIndex = Integer.valueOf(arguments.get("graphIndex"));
		String targetExpression = arguments.get("targetExpression");
		String replacementExpression = arguments.get("replacementExpression");

		String targetAlgebraicExpression = this.expressionProcessor.processExpressionIntoAlgebraicExpression(targetExpression);
		String replacementAlgebraicExpression = this.expressionProcessor.processExpressionIntoAlgebraicExpression(replacementExpression);

		graphicalCalculator.replaceAllInExpression(expressionIndex, targetAlgebraicExpression, replacementAlgebraicExpression);

		MarvinResponse response = new MarvinResponse(RequestOutcome.SUCCESS);
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
