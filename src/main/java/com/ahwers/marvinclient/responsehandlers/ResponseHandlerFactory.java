package com.ahwers.marvinclient.responsehandlers;

import com.ahwers.marvin.CommandOutcome;

public class ResponseHandlerFactory {
	
	public static ResponseHandler getHandlerForResponseStatus(CommandOutcome responseStatus) {
		ResponseHandler handler = null;
		if (responseStatus == CommandOutcome.SUCCESS) {
			handler = new SuccessResponseHandler();
		}
		else if (responseStatus == CommandOutcome.FAILED) {
			handler = new FailedResponseHandler();
		}
		else if (responseStatus == CommandOutcome.UNMATCHED) {
			handler = new UnmatchedResponseHandler();
		}
		else if (responseStatus == CommandOutcome.INVALID) {
			handler = new InvalidResponseHandler();
		}
		
		return handler;
	}

}
