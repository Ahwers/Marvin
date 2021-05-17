package com.ahwers.marvin;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import com.ahwers.marvin.applications.ApplicationResourcePathRepository;

public class CommandFormatter {
	
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
	
	private List<String> loadPoliteOpeners() throws FileNotFoundException {
		String openerLookupFilePath = ApplicationResourcePathRepository.getInstance().getApplicationResourcePathForKey("command_opening_pleasentries");
		return loadLinesFromFile(openerLookupFilePath);
	}
	
	private List<String> loadPoliteClosers() throws FileNotFoundException {
		String closerLookupFilePath = ApplicationResourcePathRepository.getInstance().getApplicationResourcePathForKey("/command_closing_pleasentries");
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
