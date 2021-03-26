package com.ahwers.marvin.applications.graphicalcalculator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO: Todo's mentioned in AlgebraicExpression and then delete it
// TODO: "Minus" doesn't work properly, two minus signs get picked up by azure
public class AlgebraicExpressionProcessor {
	
	private static AlgebraicExpressionProcessor instance;
	
	public static AlgebraicExpressionProcessor getInstance() {
		if (instance == null) {
			instance = new AlgebraicExpressionProcessor();
		}
		
		return instance;
	}
	
	private Map<String, String> tokenWordToSymbolMap = new HashMap<>();
	
	private AlgebraicExpressionProcessor() {
		try {
			populateTokenWordToSymbolMap();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private void populateTokenWordToSymbolMap() throws FileNotFoundException {
		String lookupFilePath = getClass().getResource("/algebraic_expression_syntax.txt").getPath();
		
		Scanner fileScanner = new Scanner(new File(lookupFilePath));
		
		String lookupItemRegex = "^(.+)->(.+)$";
		while (fileScanner.hasNextLine()) {
			String text = fileScanner.nextLine();
			
	        Pattern pattern = Pattern.compile(lookupItemRegex);
	        Matcher matcher = pattern.matcher(text);

	        while (matcher.find()) {
	        	tokenWordToSymbolMap.put(matcher.group(1).toLowerCase(), matcher.group(2).toLowerCase());
	        }
		}

		fileScanner.close();
	}
	
	/*
	 * 1. Lowercase entire expression
	 * 2. Isolate each token of the expression (including coefficients)
	 * 		2.a. Tokens are delimited by white space
	 * 		2.b. Tokens are further delimited by alphabetic/numeric/symbol divides (eg. 3ab*2 -> {3, ab, *, 2})
	 * 		2.c. Words separated by the space delimitation are joined back up
	 *  3. Alphabetic word tokens (and word token sequences) are replaced by their symbolic form if they have one (which are defined in the lookup file), otherwise left for the user to correct.
	 *	    This includes those nested between unrelated characters such as "caddd" becoming "c+d"
	 *	3.a. The longest word sequences are checked first (eg. "to the power of" should become "^2", even if one of the words is wrong; eg. "to the power off".
	 *	
	 */
	public String processExpressionIntoAlgebraicExpression(String expression) {
		// 1. Lowercase entire expression
		expression = expression.toLowerCase();
		
		// 2. Isolate each token of the expression (including coefficients)
		List<String> expressionTokens = new ArrayList<>();
		expressionTokens.add(expression);
		
		// 2.a. Tokens are delimited by white space
		// 2.b. Tokens are further delimited by alphabetic/numeric/symbol divides (eg. 3ab*2 -> {3, ab, *, 2})
		String[] tokenDelimiters = {
				" ",														// Space delimited
				"((?=\\d)(?<=[^\\d])|(?<=\\d)(?=[^\\d]))",					// Number (lookahead) delimited
				"((?=[^0-9a-z])(?<=[0-9a-z])|(?<=[^0-9a-z])(?=[0-9a-z]))"	// Symbol (lookahead) delimited
			  //"(((?<=[a-z]) (?=[^a-z]))|((?<=[^a-z]) (?=[a-z])))"			// Sentence delimited attempt
		};
		for (String delimiter : tokenDelimiters) {
			for (int i = 0; i < expressionTokens.size(); i++) {
				String token = expressionTokens.get(i);
				
				List<String> delimitedSubTokens = new ArrayList<>();
				Scanner tokenScanner = new Scanner(token).useDelimiter(delimiter);
				while (tokenScanner.hasNext()) {
					delimitedSubTokens.add(tokenScanner.next());
				}
				expressionTokens.remove(i);
				expressionTokens.addAll(i, delimitedSubTokens);
			}
		}
		
		// 2.c. Words separated by the space delimitation are joined back up
		// TODO: Need to improve this logic so it's not leaning on catching an IndexOutOfBoundsException, that should not be thrown at all.
		try {
			Pattern pattern = Pattern.compile("[a-z ]+");
			for (int i = 0; i < expressionTokens.size(); i++) {
				String token = expressionTokens.get(i);
				Matcher firstMatcher = pattern.matcher(token);
				if (firstMatcher.find()) {
					String nextToken = expressionTokens.get(i + 1);
					Matcher secondMatcher = pattern.matcher(nextToken);
					if (secondMatcher.find()) {
						expressionTokens.remove(i + 1);
						expressionTokens.remove(i);
						expressionTokens.add(i, token + " " + nextToken);
						i--; // Check this token and it's next again
					}
				}
			}
		} catch (IndexOutOfBoundsException e) {
			
		}
		
		// 3. Alphabetic word tokens (and word token sequences) are replaced by their symbolic form if they have one (which are defined in the lookup file), otherwise left for the user to correct.
		//    This includes those nested between unrelated characters such as "caddd" becoming "c+d"
		// 3.a. The longest word sequences are checked first (eg. "to the power of" should become "^2", even if one of the words is wrong; eg. "to the power off".
		// TODO: At the moment this sort only compares the length of the symbol words, may need to change this to compare the number of spaces in the future as i add more to the lookup file
		// TODO: Still need to do the fuzzy match for words picked up wrong, should try the replace first, then fuzzy replace if nothing changed
		// TODO: But it should only be happening for words, not characters (who are variables) who will likely soundex match anything so alphabetic tokens of length > 1
		List<String> symbolWordVersions = new ArrayList<>(tokenWordToSymbolMap.keySet());
		Collections.sort(symbolWordVersions, (Comparator<String>) (String word1, String word2) -> Integer.valueOf(word2.length()).compareTo(word1.length()));
		for (int i = 0; i < expressionTokens.size(); i++) {
			String token = expressionTokens.get(i);
			for (String alphabeticSymbol : symbolWordVersions) {
				token = token.replace(alphabeticSymbol, tokenWordToSymbolMap.get(alphabeticSymbol));
				expressionTokens.remove(i);
				expressionTokens.add(i, token);
			}
		}
		
		// 4. Format the expression in a way that is accepted by Desmos (TODO: Perhaps the strategy pattern should be used to define a formatForApplication() method, this instance of which is for Desmos).
		// TODO: Implement
		
		String processedExpression = "";
		for (String token : expressionTokens) {
			processedExpression += token;
		}
		
		return processedExpression;
	}

}
