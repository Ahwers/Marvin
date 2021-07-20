package com.ahwers.marvin;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ahwers.marvin.applications.ApplicationResourcePathRepository;

public class CommandFormatter {
	
	public static void main(String[] args) {
		CommandFormatter formatter = new CommandFormatter();
		System.out.println(formatter.formatCommand("Remove expression one please Marvin."));
	}
	
	private List<String> politeOpeners;
	private List<String> politeClosers;
	
	public CommandFormatter() {
		try {
			this.politeOpeners = loadPoliteOpeners();
			this.politeClosers = loadPoliteClosers();
		} catch (FileNotFoundException e) {
			// TODO: Log lookup load error
			e.printStackTrace();
			this.politeOpeners = new ArrayList<>();
			this.politeClosers = new ArrayList<>();
		}
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
		String punctuation = "?,.!";
		
		command = command.replaceAll("(?<=[\\d\\w])[" + punctuation +"](?=\\s|$)", "");
		
		return command;
	}

	private String stripPleasentriesFromCommand(String command) {
		for (String politeOpener : politeOpeners) {
			command = command.replaceFirst(("(?i)^" + politeOpener + "\s+"), "");
		}
		for (String politeCloser : politeClosers) {
			command = command.replaceFirst(("(?i)\s+" + politeCloser + "$"), "");
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
	
//	TODO: Implement
	private String convertAlphabeticNumberToNumeric(String alphabeticNumber) {
		return null;
	}
	
	private List<String> loadPoliteOpeners() throws FileNotFoundException {
		String openerLookupFilePath = ApplicationResourcePathRepository.getInstance().getApplicationResourcePathForKey("command_opening_pleasentries");
		return loadLinesFromFile(openerLookupFilePath);
	}
	
	private List<String> loadPoliteClosers() throws FileNotFoundException {
		String closerLookupFilePath = ApplicationResourcePathRepository.getInstance().getApplicationResourcePathForKey("command_closing_pleasentries");
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
}
