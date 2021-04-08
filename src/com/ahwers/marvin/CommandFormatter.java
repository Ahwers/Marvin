package com.ahwers.marvin;

import java.util.HashMap;
import java.util.Map;

public class CommandFormatter {
	
	public static String formatCommand(String targetCommand) {
		String command = targetCommand;
		
		command = stripPunctuationFromCommand(command);
		command = stripPleasentriesFromCommand(command);
		command = changeAlphabeticNumbersToNumeric(command);
		command = command.toLowerCase();
		
		return command;
	}
	
	// TODO: This will remove decimal places from numbers
	private static String stripPunctuationFromCommand(String command) {
		String punctuationMarks = ",.?'";
		
		command = command.replaceAll(("[" + punctuationMarks + "]"), "");
		
		return command;
	}
	
	private static String stripPleasentriesFromCommand(String command) {
		String[] openers = { "please", "please could you", "please can you", "could you", "can you", "now", "now can you", "now could you", "now please could you", "now please can you" };
		String[] closers = { "please", "for me please", "for me" };
		
		for (String politeOpener : openers) {
			command = command.replaceFirst(("(?i)^" + politeOpener + "\s+"), "");
		}
		
		for (String politeCloser : closers) {
			command = command.replaceFirst(("(?i)\s+" + politeCloser + "$"), "");
		}
		
		return command;
	}
	
	// TODO: Gotta change larger numbers too, we made a perl script for this as training for CPU.
	private static String changeAlphabeticNumbersToNumeric(String command) {
		Map<String, String> alphaToNumericNumbers = new HashMap<>();
		
		alphaToNumericNumbers.put("zero", "0");
		alphaToNumericNumbers.put("one", "1");
		alphaToNumericNumbers.put("two", "2");
		alphaToNumericNumbers.put("three", "3");
		alphaToNumericNumbers.put("four", "4");
		alphaToNumericNumbers.put("five", "5");
		alphaToNumericNumbers.put("six", "6");
		alphaToNumericNumbers.put("seven", "7");
		alphaToNumericNumbers.put("eight", "8");
		alphaToNumericNumbers.put("nine", "9");
		
		for (String alphabetic : alphaToNumericNumbers.keySet()) {
			command = command.replaceAll(("(?i)" + alphabetic), alphaToNumericNumbers.get(alphabetic));
		}
		
		return command;
	}

}
