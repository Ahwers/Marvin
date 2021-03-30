package com.ahwers.marvin.applications.graphicalcalculator;

public interface GraphicalCalculator {
	
	public int addNewExpression(String expression);
	
	public void removeExpression(int expressionIndex) throws Exception;
	
	public void removeAllExpressions();
	
	public void replaceExpressionWith(int expressionIndex, String expression);
	
	public void replaceInExpression(int expressionIndex, String target, String newValue);
	
	public void hideExpression(int expressionIndex);
	
	public void focusOnExpression(int expressionIndex);
	
	public void focusOnOrigin();
	
}
