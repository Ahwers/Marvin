package com.ahwers.marvinclient;

import com.ahwers.marvin.CommandStatus;
import com.ahwers.marvin.MarvinResponse;

public class NewResponseHandler {
	
	public static void handleResponse(MarvinResponse response) {
		NewResponseHandler responseHandler = new NewResponseHandler(response);
		responseHandler.handleResponse();
	}
	
	private MarvinResponse response;
	
	public NewResponseHandler(MarvinResponse response) {
		this.response = response;
	}
	
	private void handleResponse() {
		if (commandWasSuccessful()) {
			// If there is a resource attached, show it
		}
		else if (commandFailed()) {
			// Output error message and display exception stack trace
		}
		else if (commandWasNotMatched()) {
			// 
		}
		else if(commandWasInvalid()) {
			
		}
	}

}
