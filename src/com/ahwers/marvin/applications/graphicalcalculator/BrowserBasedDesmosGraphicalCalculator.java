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

// TODO: Should create a GraphicalCalculator interface and move the selenium guts to a BrowserApplication super class or something.
public class BrowserBasedDesmosGraphicalCalculator extends Application implements GraphicalCalculator {
	
	private WebDriver browserDriver;
	
	private List<String> activeExpressions = new ArrayList<>();
	
	public static void main(String args[]) {
		BrowserBasedDesmosGraphicalCalculator calc = new BrowserBasedDesmosGraphicalCalculator();
	}
	
	public BrowserBasedDesmosGraphicalCalculator() {
		System.setProperty("webdriver.gecko.driver", "C:\\WebDriver\\bin\\geckodriver.exe");
		
		this.browserDriver = new FirefoxDriver();
		
		String CALCULATOR_APP_PATH = getClass().getResource("/graphical_calculator.html").toString();
		browserDriver.get(CALCULATOR_APP_PATH);
}

	@Override
	public void plotNewExpression(String expression) {
		expression = AlgebraicExpressionProcessor.getInstance().processExpressionIntoAlgebraicExpressionForApplication(expression, ApplicationProvider.DESMOS);
		activeExpressions.add(expression);
		
		JavascriptExecutor js = (JavascriptExecutor) this.browserDriver;
		js.executeScript("calculator.setExpression({ id: '" + activeExpressions.size() + "', latex: '" + expression.toString() + "' });");
	}

	// TODO: Broken
	@Override
	public void removeExpression(int expressionIndex) throws IndexOutOfBoundsException {
		activeExpressions.remove(expressionIndex - 1);
		
		JavascriptExecutor js = (JavascriptExecutor) this.browserDriver;
		js.executeScript("calculator.removeExpression({ id: '" + expressionIndex + "' });");
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
