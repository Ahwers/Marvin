package com.ahwers.marvin.applications.graphicalcalculator;

public interface GraphicalCalculator {

    public void clear();

	public void addNewExpression(String expression);

	public void removeExpression(int expressionIndex) throws IndexOutOfBoundsException;

	public void removeAllExpressions();

	public void replaceExpressionWith(int expressionIndex, String expression);

	public void replaceAllInExpression(int expressionIndex, String target, String newValue);

	public void replaceOnceInExpression(int expressionIndex, String target, String newValue, int occuranceToReplace);

	public void hideExpression(int expressionIndex);

	public void showExpression(int expressionIndex);

	public void undo();

	public void redo();

}
