package com.ahwers.marvin.applications.graphicalcalculator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.ahwers.marvin.applications.Application;

// TODO: Make concurrent singleton
public class GraphicalCalculatorState extends Application implements GraphicalCalculator {
	
	private int stateId = 0;
	private Map<Integer, String> expressions = new HashMap<>();
	private int focusX = 0;
	private int focusY = 0;
	
	public int getStateId() {
		return this.stateId;
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
	
	@Override
	public int addNewExpression(String expression) {
		int expressionId = (expressions.size() + 1);
		expressions.put(expressionId, expression);
		this.stateId++;
		
		return expressionId;
	}

	@Override
	public void removeExpression(int expressionIndex) throws IndexOutOfBoundsException {
		expressions.remove(expressionIndex);
		this.stateId++;
	}

	@Override
	public void removeAllExpressions() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void replaceExpressionWith(int expressionIndex, String expression) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void replaceInExpression(int expressionIndex, String target, String newValue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hideExpression(int expressionIndex) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void focusOnOrigin() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void focusOnExpression(int expressionIndex) {
		// TODO Auto-generated method stub
		
	}

}
