package com.ahwers.marvin.applications.graphicalcalculator;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.ahwers.marvin.framework.application.Application;
import com.ahwers.marvin.framework.resource.ResourceTemplate;

public class DesmosGraphicalCalculator extends Application {
	
	private static DesmosGraphicalCalculator instance;
	public static DesmosGraphicalCalculator getInstance() {
		if (instance == null) {
			instance = new DesmosGraphicalCalculator();
		}
		
		return instance;
	}
	
	private int currentStateId = 1;
	private List<String> expressions = new LinkedList<>();
	private int focusX = 0;
	private int focusY = 0;
	private String lastUpdateScript = "";

	private DesmosGraphicalCalculator() {
		expressions.add(""); // To make the expresions list base 1 and as such represetative of the expression keys shown in the app resource.
	}
	
	// TODO: I feel like i want to separate the resource generation aspect from the state.
	public String getHtmlRepresentation() {
		ResourceTemplate calculatorTemplate = new ResourceTemplate("graphical_calculator_html");
		Map<String, String> templateData = new HashMap<>();
		templateData.put("CALCULATOR_SET_UP_SCRIPT", getHtmlStateInstantiationScript());
		return calculatorTemplate.mergeDataWithResourceTemplate(templateData);
	}
	
	public String getHtmlStateInstantiationScript() {
		String script = "var stateId = " + this.currentStateId + ";\n"
				      + "var focusX = " + focusX + ";\n"
				      + "var focusY = " + focusY + ";\n"
				      + "calculator.setBlank();\n";
		
		int expressionId = 0;
		for (String expression : expressions) {
			script += "calculator.setExpression({ id: '" + expressionId + "', latex: '" + expression +"' });\n";
			expressionId++;
		}
		
		return script;
	}
	
	public String getHtmlStateUpdateScriptFromPreviousStateId() {
		return this.lastUpdateScript;
	}
	
	public int getCurrentStateId() {
		return this.currentStateId;
	}
	
	public int getPreviousStateId() {
		return (this.currentStateId - 1);
	}
	
	public void addNewExpression(String expression) {
		int expressionId = (expressions.size() + 1);
		expressions.add(expression);
		
		this.currentStateId++;
		this.lastUpdateScript = ("calculator.setExpression({ id: '" + expressionId + "', latex: '" + expression + "' });");
	}

	public void removeExpression(int expressionIndex) throws IndexOutOfBoundsException {
		expressions.remove(expressionIndex);
		
		this.currentStateId++;
		this.lastUpdateScript = ("calculator.removeExpression({ id: '" + expressionIndex + "' });");
	}

	public void removeAllExpressions() {
		expressions.clear();
		expressions.add("");
		
		this.currentStateId++;
		this.lastUpdateScript = "calculator.setBlank();";
	}

	public void replaceExpressionWith(int expressionIndex, String expression) {
		expressions.remove(expressionIndex);
		expressions.add(expressionIndex, expression);

		this.currentStateId++;
		this.lastUpdateScript = ("calculator.setExpression({ id: '" + expressionIndex + "', latex: '" + expression + "'});");
	}

	// TODO: The adaptor will need to process the target and newValue string with the AlgebraicExpressionProcessor and call this with them.
	public void replaceAllInExpression(int expressionIndex, String target, String newValue) {
		String currentExpression = expressions.get(expressionIndex);
		String updatedExpression = currentExpression.replaceAll(target, newValue);

		expressions.remove(expressionIndex);
		expressions.add(expressionIndex, updatedExpression);

		this.currentStateId++;
		this.lastUpdateScript = ("calculator.setExpression({ id: '" + expressionIndex + "', latex: '" + updatedExpression + "'});");
	}

	public void replaceOnceInExpression(int expressionIndex, String target, String newValue, int occuranceToReplace) {
		// TODO: Implement, will likely need to do it with a regex because String.replaceInstanceOf() doesn't exist.
	}

	public void hideExpression(int expressionIndex) {
		String expression = expressions.get(expressionIndex);

		this.currentStateId++; // TODO: Does true need to be in brackets?
		this.lastUpdateScript = ("calculator.setExpression({ hidden: true, id: '" + expressionIndex + "', latex: '" + expression + "'});");
	}

	public void showExpression(int expressionIndex) {
		String expression = expressions.get(expressionIndex);

		this.currentStateId++; // TODO: Does false need to be in brackets?
		this.lastUpdateScript = ("calculator.setExpression({ hidden: false, id: '" + expressionIndex + "', latex: '" + expression + "'});");
	}

	public void undo() {
		this.currentStateId++;
		this.lastUpdateScript = ("calculator.undo();");
	}

	public void redo() {
		this.currentStateId++;
		this.lastUpdateScript = ("calculator.redo();");
	}

}
