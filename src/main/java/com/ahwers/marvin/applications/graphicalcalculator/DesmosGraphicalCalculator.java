package com.ahwers.marvin.applications.graphicalcalculator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ahwers.marvin.Resource;
import com.ahwers.marvin.ResourceRepresentationType;
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
	private Map<Integer, String> expressions = new HashMap<>();
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
		for (Integer expressionId : this.expressions.keySet()) {
			script += "calculator.setExpression({ id: '" + expressionId + "', latex: '" + this.expressions.get(expressionId) +"' });\n";
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
	
	public Map<Integer, String> getExpressions() {
		return this.expressions;
	}
	
	public int getFocusX() {
		return this.focusX;
	}
	
	public int getFocusY() {
		return this.focusY;
	}
	
	public void addNewExpression(String expression) {
		int expressionId = (expressions.size() + 1);
		expressions.put(expressionId, expression);
		
		this.currentStateId++;
		this.lastUpdateScript = ("calculator.setExpression({ id: '" + expressionId + "', latex: '" + expression + "' });");
	}

	public void removeExpression(int expressionIndex) throws IndexOutOfBoundsException {
		expressions.remove(expressionIndex);
		
		this.currentStateId++;
		this.lastUpdateScript = ("calculator.removeExpression({ id: '" + expressionIndex + "' });");
	}

	public void removeAllExpressions() {
		// TODO Auto-generated method stub
		
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
