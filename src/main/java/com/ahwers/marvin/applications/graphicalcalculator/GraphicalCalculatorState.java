package com.ahwers.marvin.applications.graphicalcalculator;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.ahwers.marvin.framework.application.Application;
import com.ahwers.marvin.framework.resource.ResourceTemplate;

public class GraphicalCalculatorState implements GraphicalCalculator {

	private List<String> expressions = new LinkedList<>();
	private int focusX = 0;
	private int focusY = 0;

	public GraphicalCalculatorState() {
		expressions.add("");
	}

	public int getFocusX() {
		return this.focusX;
	}

	public int getFocusY() {
		return this.focusY;
	}

	public List<String> getExpressions() {
		return this.expressions;
	}

	public int getNumberOfExpressions() {
		return this.expressions.size();
	}

	@Override
	public void clear() {
		expressions = new LinkedList<>();
		focusX = 0;
		focusY = 0;
	}

	@Override
	public void addNewExpression(String expression) {
		expressions.add(expression);
	}

	@Override
	public void removeExpression(int expressionIndex) throws IndexOutOfBoundsException {
		expressions.remove(expressionIndex);
		
		// this.lastUpdateScript = ("calculator.removeExpression({ id: '" + expressionIndex + "' });");
	}

	@Override
	public void removeAllExpressions() {
		expressions.clear();
		expressions.add("");
		
		// this.lastUpdateScript = "calculator.setBlank();";
	}

	@Override
	public void replaceExpressionWith(int expressionIndex, String expression) {
		expressions.remove(expressionIndex);
		expressions.add(expressionIndex, expression);

		// this.lastUpdateScript = ("calculator.setExpression({ id: '" + expressionIndex + "', latex: '" + expression + "'});");
	}

	@Override
	// TODO: The adaptor will need to process the target and newValue string with the AlgebraicExpressionProcessor and call this with them.
	public void replaceAllInExpression(int expressionIndex, String target, String newValue) {
		String currentExpression = expressions.get(expressionIndex);
		String updatedExpression = currentExpression.replaceAll(target, newValue);

		expressions.remove(expressionIndex);
		expressions.add(expressionIndex, updatedExpression);

		// this.lastUpdateScript = ("calculator.setExpression({ id: '" + expressionIndex + "', latex: '" + updatedExpression + "'});");
	}

	@Override
	public void replaceOnceInExpression(int expressionIndex, String target, String newValue, int occuranceToReplace) {
		// TODO: Implement, will likely need to do it with a regex because String.replaceInstanceOf() doesn't exist.
	}

	@Override
	public void hideExpression(int expressionIndex) {
		String expression = expressions.get(expressionIndex);

		// this.lastUpdateScript = ("calculator.setExpression({ hidden: true, id: '" + expressionIndex + "', latex: '" + expression + "'});");
	}

	@Override
	public void showExpression(int expressionIndex) {
		String expression = expressions.get(expressionIndex);

		// this.lastUpdateScript = ("calculator.setExpression({ hidden: false, id: '" + expressionIndex + "', latex: '" + expression + "'});");
	}

	// TODO: I could implement a CalculatorState (rename this class to StateController) class that this class will have as an instance variable
	//       For each change to the state, the current state is cloned and saved to the variable previousState so that it can reverted to through undo calls.
	@Override
	public void undo() {
		// this.lastUpdateScript = ("calculator.undo();");
	}

	@Override
	public void redo() {
		// this.lastUpdateScript = ("calculator.redo();");
	}

}
