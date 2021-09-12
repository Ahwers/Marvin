package com.ahwers.marvin.applications.graphicalcalculator;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

import com.ahwers.marvin.framework.application.Application;
import com.ahwers.marvin.framework.application.ResourceBuilder;
import com.ahwers.marvin.framework.resource.MarvinApplicationResource;
import com.ahwers.marvin.framework.resource.ResourceRepresentationType;
import com.ahwers.marvin.framework.resource.ResourceTemplate;

public class DesmosGraphicalCalculator extends Application implements GraphicalCalculator, ResourceBuilder {
        
    private GraphicalCalculatorState calculatorState = new GraphicalCalculatorState();

    private int currentStateId = 1;
    private String lastUpdateScript = "";

    @Override
    public MarvinApplicationResource buildResourceForApplication(String applicationName) {
		MarvinApplicationResource graphicalCalculatorResource = new MarvinApplicationResource(applicationName, currentStateId, (currentStateId - 1));
		graphicalCalculatorResource.addRepresentation(ResourceRepresentationType.HTML, buildHtmlRepresentation());
		graphicalCalculatorResource.addRepresentation(ResourceRepresentationType.HTML_STATE_INSTANTIATION_SCRIPT, buildHtmlStateInstantiationScript());
		graphicalCalculatorResource.addRepresentation(ResourceRepresentationType.HTML_STATE_UPDATE_SCRIPT, buildHtmlStateUpdateScript());

		return graphicalCalculatorResource;
    }

    private String buildHtmlStateUpdateScript() {
        return this.lastUpdateScript;
    }

    private String buildHtmlStateInstantiationScript() {
        int focusX = calculatorState.getFocusX();
        int focusY = calculatorState.getFocusY();
        List<String> expressions = calculatorState.getExpressions();

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

    private String buildHtmlRepresentation() {
        ResourceTemplate calculatorTemplate = new ResourceTemplate("graphical_calculator_html");

		Map<String, String> templateData = new HashMap<>();
		templateData.put("CALCULATOR_SET_UP_SCRIPT", buildHtmlStateInstantiationScript());
		
        return calculatorTemplate.mergeDataWithResourceTemplate(templateData);
    }

    private void incrementStateId() {
        this.currentStateId++;
    }

    @Override
    public void clear() {
        calculatorState.clear();

        incrementStateId(); 
        this.lastUpdateScript = ("calculator.setBlank();");
    }

    @Override
    public void addNewExpression(String expression) {
		int expressionId = (calculatorState.getNumberOfExpressions() + 1);
        calculatorState.addNewExpression(expression);

        incrementStateId();
		this.lastUpdateScript = ("calculator.setExpression({ id: '" + expressionId + "', latex: '" + expression + "' });");
    }

    @Override
    public void removeExpression(int expressionIndex) throws IndexOutOfBoundsException {
       incrementStateId(); 
    }

    @Override
    public void removeAllExpressions() {
       incrementStateId(); 
    }

    @Override
    public void replaceExpressionWith(int expressionIndex, String expression) {
       incrementStateId(); 
    }

    @Override
    public void replaceAllInExpression(int expressionIndex, String target, String newValue) {
       incrementStateId(); 
    }

    @Override
    public void replaceOnceInExpression(int expressionIndex, String target, String newValue, int occuranceToReplace) {
       incrementStateId(); 
    }

    @Override
    public void hideExpression(int expressionIndex) {
       incrementStateId(); 
    }

    @Override
    public void showExpression(int expressionIndex) {
       incrementStateId(); 
    }

    @Override
    public void undo() {
       incrementStateId(); 
    }

    @Override
    public void redo() {
       incrementStateId(); 
    }

}
