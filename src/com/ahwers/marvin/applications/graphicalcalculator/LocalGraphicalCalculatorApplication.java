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
public class LocalGraphicalCalculatorApplication extends Application implements GraphicalCalculator {
	
	// TODO: Store graph state so that it can be recovered between browerDriver instances.
	// TODO: Handle the case where the browser is closed then this object is given another command.
	// TODO: Instantiate graph with last known state through URL parameters https://html-online.com/articles/get-url-parameters-javascript/
	private WebDriver browserDriver;
	
	private List<String> activeExpressions = new ArrayList<>();
	
	public static void main(String args[]) {
		LocalGraphicalCalculatorApplication calc = new LocalGraphicalCalculatorApplication();
	}
	
	public LocalGraphicalCalculatorApplication() {
		System.setProperty("webdriver.gecko.driver", "C:\\WebDriver\\bin\\geckodriver.exe");
		
		this.browserDriver = new FirefoxDriver();
		
		String CALCULATOR_APP_PATH = getClass().getResource("/graphical_calculator.html").toString();
		browserDriver.get(CALCULATOR_APP_PATH + "?initialExpression=y=x");
	}

	@Override
	public void plotNewExpression(String expression) {
		expression = AlgebraicExpressionProcessor.getInstance().processExpressionIntoAlgebraicExpression(expression);
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
