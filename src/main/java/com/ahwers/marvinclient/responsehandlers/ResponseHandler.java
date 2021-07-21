package com.ahwers.marvinclient.responsehandlers;

import com.ahwers.marvin.response.MarvinResponse;

public abstract class ResponseHandler {
	
	public abstract void handleResponse(MarvinResponse response);
	
	protected void outputResponseMessage(String message) {
		if (message != null) {
			System.out.println(message);
		}
	}

}
