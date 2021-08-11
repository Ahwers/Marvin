package com.ahwers.marvin.applications.graphicalcalculator;

import java.io.IOException;
import java.util.Map;

import com.ahwers.marvin.applications.Application;
import com.ahwers.marvin.applications.ApplicationAdaptor;
import com.ahwers.marvin.applications.CommandMatch;
import com.ahwers.marvin.applications.IntegratesApplication;
import com.ahwers.marvin.response.MarvinResponse;
import com.ahwers.marvin.response.RequestOutcome;
import com.ahwers.marvin.response.resource.MarvinResource;
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

	@CommandMatch("open graphical calculator")
	public MarvinResource openGraphicalCalculator(Map<String, String> arguments) {
		return buildGraphicalCalculatorResource();
	}
	
	@CommandMatch("plot (?<expression>.+)")
	public MarvinResource addNewAlgebraicExpression(Map<String, String> arguments) {
		DesmosGraphicalCalculator graphicalCalculator = (DesmosGraphicalCalculator) getApplication();
	
		String processedExpression = expressionProcessor.processExpressionIntoAlgebraicExpression(arguments.get("expression"));
		graphicalCalculator.addNewExpression(processedExpression);
			
		return buildGraphicalCalculatorResource();
	}
	
	@CommandMatch("remove (?:graph|expression) (?<graphIndex>.+)")
	public MarvinResource removeAlgebraicExpression(Map<String, String> arguments) {
		DesmosGraphicalCalculator graphicalCalculator = (DesmosGraphicalCalculator) getApplication();
		
		String expressionIndex = arguments.get("graphIndex");
		
		try {
			graphicalCalculator.removeExpression(Integer.valueOf(expressionIndex));
		} catch (NumberFormatException e) {
			throw new NumberFormatException("\"Graphical Calculator\" graphs are indexed by integers and the non integer key (" + expressionIndex + ") for \"graphIndex\" was provided.");
		} catch (IndexOutOfBoundsException e) {
			throw new IndexOutOfBoundsException("Graph " + expressionIndex + "doesn't exist.");
		}
		
		return buildGraphicalCalculatorResource();
	}

	@CommandMatch("remove all (?:expressions|graphs)")
	public MarvinResource removeAllAlgebraicExpressions(Map<String, String> arguments) {
		DesmosGraphicalCalculator graphicalCalculator = (DesmosGraphicalCalculator) getApplication();

		graphicalCalculator.removeAllExpressions();

		return buildGraphicalCalculatorResource();
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
	
	private MarvinResource buildGraphicalCalculatorResource() {
		DesmosGraphicalCalculator graphicalCalculator = (DesmosGraphicalCalculator) getApplication();
		
		int previousStateId = graphicalCalculator.getPreviousStateId();
		int currentStateId = graphicalCalculator.getCurrentStateId();

		MarvinResource graphicalCalculatorResource = new MarvinResource(getApplicationName(), currentStateId, previousStateId);
		graphicalCalculatorResource.addRepresentation(ResourceRepresentationType.HTML, graphicalCalculator.getHtmlRepresentation());
		graphicalCalculatorResource.addRepresentation(ResourceRepresentationType.HTML_STATE_INSTANTIATION_SCRIPT, graphicalCalculator.getHtmlStateInstantiationScript());
		graphicalCalculatorResource.addRepresentation(ResourceRepresentationType.HTML_STATE_UPDATE_SCRIPT, graphicalCalculator.getHtmlStateUpdateScriptFromPreviousStateId());

		return graphicalCalculatorResource;
	}

}
