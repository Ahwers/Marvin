package com.ahwers.marvin.applications.graphicalcalculator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.ahwers.marvin.ResourceTemplate;
import com.ahwers.marvin.applications.Application;

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
	
	public String getHtmlRepresentation() {
		ResourceTemplate calculatorTemplate = new ResourceTemplate("graphical_calculator");
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
	
	public List<String> getExpressions() {
		return this.expressions;
	}
	
	public int getFocusX() {
		return this.focusX;
	}
	
	public int getFocusY() {
		return this.focusY;
	}
	
	public void addNewExpression(String expression) {
		expressions.add(expression);
		
		this.currentStateId++;
		this.lastUpdateScript = ("calculator.setExpression({ id: '" + expressions.size() + "', latex: '" + expression + "' });");
	}

	public void removeExpression(int expressionIndex) throws IndexOutOfBoundsException {
		expressions.remove(expressionIndex - 1);
		
		this.currentStateId++;
		this.lastUpdateScript = ("calculator.removeExpression({ id: '" + expressionIndex + "' });");
	}

	public void removeAllExpressions() {
		expressions.clear();
		
		this.currentStateId++;
		this.lastUpdateScript = "calculator.setBlank();";
	}

	public void replaceExpressionWith(int expressionIndex, String expression) {
		// TODO Auto-generated method stub
		
	}

	public void replaceInExpression(int expressionIndex, String target, String newValue) {
		// TODO Auto-generated method stub
		
	}

	public void hideExpression(int expressionIndex) {
		// TODO Auto-generated method stub
		
	}

	public void focusOnOrigin() {
		// TODO Auto-generated method stub
		
	}

	public void focusOnExpression(int expressionIndex) {
		// TODO Auto-generated method stub
		
	}

}
