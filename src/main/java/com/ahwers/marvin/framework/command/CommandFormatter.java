package com.ahwers.marvin.framework.command;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ahwers.marvin.framework.tools.ResourceRepository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommandFormatter {

	private static Logger logger = LogManager.getLogger(CommandFormatter.class);
	
	private List<String> politeOpeners;
	private List<String> politeClosers;
	private Map<String, String> alphabeticToNumericNumbers;

	public CommandFormatter() {
		this.alphabeticToNumericNumbers = loadAlphabeticToNumericNumbersMap();
		
		try {
			this.politeOpeners = loadPoliteOpeners();
			this.politeClosers = loadPoliteClosers();
		} catch (FileNotFoundException e) {
			logger.warn(e.getClass().toString() + " thrown. Could not load the polite opener and closer lookups from their files. Exception Message: " + e.getMessage());
			this.politeOpeners = new ArrayList<>();
			this.politeClosers = new ArrayList<>();
		}
	}
		
	private List<String> loadPoliteOpeners() throws FileNotFoundException {
		String openerLookupFilePath = ResourceRepository.getInstance().getResourcePath("command_opening_pleasentries.txt");
		return loadLinesFromFile(openerLookupFilePath);
	}
	
	private List<String> loadPoliteClosers() throws FileNotFoundException {
		String closerLookupFilePath = ResourceRepository.getInstance().getResourcePath("command_closing_pleasentries.txt");
		return loadLinesFromFile(closerLookupFilePath); 
	}
	
	private List<String> loadLinesFromFile(String filePath) throws FileNotFoundException {
		List<String> lines = new ArrayList<>();
		Scanner lookupReader = new Scanner(new File(filePath));
		while (lookupReader.hasNext()) {
			lines.add(lookupReader.nextLine());
		}
		
		return lines;
	}
	
	private Map<String, String> loadAlphabeticToNumericNumbersMap() {
		Map<String, String> alphabeticToNumericNumbers = new HashMap<>();
		
		alphabeticToNumericNumbers.put("zero", "0");
		alphabeticToNumericNumbers.put("one", "1");
		alphabeticToNumericNumbers.put("two", "2");
		alphabeticToNumericNumbers.put("three", "3");
		alphabeticToNumericNumbers.put("four", "4");
		alphabeticToNumericNumbers.put("five", "5");
		alphabeticToNumericNumbers.put("six", "6");
		alphabeticToNumericNumbers.put("seven", "7");
		alphabeticToNumericNumbers.put("eight", "8");
		alphabeticToNumericNumbers.put("nine", "9");
		
		return alphabeticToNumericNumbers;
	}
	
	public String formatCommand(String targetCommand) {
		String command = targetCommand;
		
		command = removeGrammaticalPunctuationFromCommand(command);
		command = stripPleasentriesFromCommand(command);
		command = numerifyAlphabeticNumbersInString(command);
		command = command.toLowerCase();
		
		return command;
	}
	
	private String removeGrammaticalPunctuationFromCommand(String command) {
		String punctuation = "?,.!;";
		
		command = command.replaceAll(("[" + punctuation + "]"), "");
		
		return command;
	}

	private String stripPleasentriesFromCommand(String command) {
		for (String politeOpener : politeOpeners) {
			command = command.replaceFirst(("(?i)^" + politeOpener + "\\s+"), "");
		}
		for (String politeCloser : politeClosers) {
			command = command.replaceFirst(("(?i)\\s+" + politeCloser + "$"), "");
		}
		
		return command;
	}
	
	private String numerifyAlphabeticNumbersInString(String sentence) {
		List<String> wordsInSentence = getWordsInSentence(sentence);
		
		for (String word : wordsInSentence) {
			String numericNumber = convertAlphabeticNumberToNumeric(word);
			if (numericNumber != null) {
				sentence = sentence.replaceFirst(word, numericNumber);
			}
		}
		
		return sentence;
	}
	
	private List<String> getWordsInSentence(String sentence) {
		List<String> words = new ArrayList<>();
		
		Pattern wordPattern = Pattern.compile("([a-zA-z]+)");
		Matcher matcher = wordPattern.matcher(sentence);
		while (matcher.find()) {
			words.add(matcher.group());
		}
		
		return words;
	}
	
	private String convertAlphabeticNumberToNumeric(String alphabeticNumber) {
		return this.alphabeticToNumericNumbers.get(alphabeticNumber.toLowerCase());
	}

}
