package com.ahwers.marvin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.ahwers.marvin.applications.ApplicationAction;
import com.ahwers.marvin.applications.ApplicationsManager;
import com.microsoft.cognitiveservices.speech.SpeechConfig;
import com.microsoft.cognitiveservices.speech.SpeechRecognitionResult;
import com.microsoft.cognitiveservices.speech.SpeechRecognizer;
import com.microsoft.cognitiveservices.speech.audio.AudioConfig;

public class Marvin {
	
	public static void main(String args[]) throws InterruptedException, ExecutionException, IOException {
		Marvin marvin = new Marvin();
		
//		marvin.runTestCommand("can you plot wye equals ex squared at 3 ex sad 5 please");
//		marvin.runTestCommand("can you plot y = x ^ 2 + (3x + 5)/x please");
//		marvin.runTestCommand("what's 5 plus 6");
//		marvin.runTestCommand("does 2 plus 2 equal 5");
		
		System.out.println("Just Chillin.");
		Scanner sc = new Scanner(System.in);
		while (true) {
			System.out.println("Press any key to issue a command.");
			sc.nextLine();
			
			marvin.runVoiceCommand();
		}
	}
	
	private ApplicationsManager applicationsManager = new ApplicationsManager();
	
	public void runVoiceCommand() throws InterruptedException, ExecutionException {
		String heardCommand = listenToCommand();
		runCommand(heardCommand);
	}
	
	public void runTestCommand(String command) {
		runCommand(command);
	}
	
	public void runCommand(String originalCommand) {
		String command = CommandProcessor.processCommand(originalCommand);
		System.out.println("Command: \"" + command + "\"");
		
		List<ApplicationAction> possibleActions = applicationsManager.getApplicationActionsToConsumeCommand(command);
		
		ApplicationAction actionToInvoke = null;
		if (possibleActions.size() == 1) {
			actionToInvoke = possibleActions.get(0);
		}
		else if (possibleActions.size() > 1) {
			// TODO: Implement decision algorithm, and user selection functionality if a confident decision cannot be made.
			// TODO: User selection functionality will need to developed by individual client applications if i go the route of packaging Marvin libraries and using them. Android could create a dialog box, desktop could create a swing thing. Will need to annotate those classes with @CommandSelector or something and have this method search for that class so we can inject it.
		}
		
		CommandExecutionOutcome outcome = new CommandExecutionOutcome(CommandStatus.INVALID);
		if (actionToInvoke != null) {
			outcome = applicationsManager.executeApplicationAction(actionToInvoke);
		}
		this.reconcileCommandExecution(command, outcome);
	}
	
	private String listenToCommand() throws InterruptedException, ExecutionException {
		System.out.println("Listening...");
		
		String azureKeyFilePath = getClass().getResource("/azure_key.txt").getPath();
		Scanner azureKeyScanner;
		String azureKey = "";
		try {
			azureKeyScanner = new Scanner(new File(azureKeyFilePath));
			azureKey = azureKeyScanner.nextLine();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		SpeechConfig speechConfig = SpeechConfig.fromSubscription(azureKey, "uksouth");
		
		AudioConfig audioConfig = AudioConfig.fromDefaultMicrophoneInput();
        SpeechRecognizer recognizer = new SpeechRecognizer(speechConfig, audioConfig);

        Future<SpeechRecognitionResult> task = recognizer.recognizeOnceAsync();
        SpeechRecognitionResult result = task.get();
        
        recognizer.close();
        
        return result.getText();
	}
	
	private void reconcileCommandExecution(String command, CommandExecutionOutcome outcome) {
		CommandStatus commandStatus = outcome.getCommandStatus();
		
		String marvinResponseMessage = null;
		
		// Set up default responses
		if (commandStatus == CommandStatus.INVALID) {
			marvinResponseMessage = "Sorry, I have not been programmed to process that command.";
		}
		else if (commandStatus == CommandStatus.FAILED) {
			marvinResponseMessage = "Something failed, see logs for cause.";
			outcome.getFailException().printStackTrace();
		}
		
		if (outcome.getResponseMessage() != null) {
			marvinResponseMessage = outcome.getResponseMessage();
		}
		
		this.respond(marvinResponseMessage);
	}
	
	private void respond(String message) {
		if (message != null) {
			System.out.println(message);
		}
	}

}
