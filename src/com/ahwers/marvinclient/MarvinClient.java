package com.ahwers.marvinclient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.ahwers.marvin.MarvinResponse;
import com.ahwers.marvin.Resource;
import com.ahwers.marvinclient.responsehandlers.ResponseHandler;
import com.ahwers.marvinclient.responsehandlers.ResponseHandlerFactory;
import com.ahwers.marvin.CommandOutcome;
import com.ahwers.marvin.Marvin;
import com.microsoft.cognitiveservices.speech.SpeechConfig;
import com.microsoft.cognitiveservices.speech.SpeechRecognitionResult;
import com.microsoft.cognitiveservices.speech.SpeechRecognizer;
import com.microsoft.cognitiveservices.speech.audio.AudioConfig;

public class MarvinClient {
	
	// TODO: Write test suites, but put them with the core Marvin code
	public static void main(String args[]) throws InterruptedException, ExecutionException, IOException {
		MarvinClient marvinClient = new MarvinClient();
		
//		marvinClient.runVoiceCommand();
		
		marvinClient.runCommand("test");
		marvinClient.runCommand("what does 2 plus 2 equal");
//		marvinClient.runCommand("can you plot wye equals ex squared at 3 ex sad 5 please");
//		marvinClient.runCommand("can you plot y = x ^ 2 + (3x + 5)/x please");
//		Thread.sleep(2000);
//		marvinClient.runCommand("can you plot y = x ^ 2 + (3x + 5)/x please");
//		marvinClient.runCommand("what's 5 plus 6");
//		marvinClient.runCommand("does 2 plus 2 equal 5");
		
//		System.out.println("Just Chillin.");
//		Scanner sc = new Scanner(System.in);
//		while (true) {
//			System.out.println("Press any key to issue a command.");
//			sc.nextLine();
//			
//			marvinClient.runVoiceCommand();
//		}
	}
	
	private Marvin marvin;
	
	public MarvinClient() {
		this.marvin = new Marvin();
	}
	
	public void runVoiceCommand() throws InterruptedException, ExecutionException {
		String heardCommand = listenToCommand();
		runCommand(heardCommand);
	}
	
	public void runCommand(String command) {
		MarvinResponse marvinResponse = marvin.processCommand(command);
		handleResponse(marvinResponse);
	}
	
	private void handleResponse(MarvinResponse response) {
		if (response.getResponseMessage() != null) {
			outputMessage(response.getResponseMessage());
		}
		
		CommandOutcome status = response.getCommandStatus();
		if (status == CommandOutcome.SUCCESS) {
			try {
				ResourceHandler.openResource(response.getResources());
			} catch (InvalidResponseException e) {
				outputMessage("I can not open this resource due to a faulty response, check my logs for more info.");
				e.printStackTrace();
			}
		}
		else if (status == CommandOutcome.FAILED) {
			System.out.println(response.getFailException().getMessage());
		}
		else if (status == CommandOutcome.UNMATCHED) {
			String actionToRun = getActionToRunFromUnmatchedResponse(response);
			handleResponse(marvin.processAction(actionToRun));
		}
	}
	
	private String getActionToRunFromUnmatchedResponse(MarvinResponse response) {
		Scanner actionOptionsScanner = new Scanner(response.getResources().get(0).getContent());
		
		List<String> actionOptions = new ArrayList<>();
		
		int i = 1;
		while (actionOptionsScanner.hasNextLine()) {
			String actionString = actionOptionsScanner.nextLine();
			actionOptions.add(actionString);
			System.out.println(i + " - " + actionString);
			i++;
		}
		int targetAction = new Scanner(System.in).nextInt();
		
		return actionOptions.get(targetAction - 1);
	}
	
	// TODO: There must be a better name for this method, it will be making Marvin talk.
	private void outputMessage(String message) {
		System.out.println(message);
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

}
