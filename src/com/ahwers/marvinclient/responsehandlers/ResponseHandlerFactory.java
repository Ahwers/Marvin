package com.ahwers.marvinclient.responsehandlers;

import com.ahwers.marvin.CommandStatus;

public class ResponseHandlerFactory {
	
	public static ResponseHandler getHandlerForResponseStatus(CommandStatus responseStatus) {
		ResponseHandler handler = null;
		if (responseStatus == CommandStatus.SUCCESS) {
			handler = new SuccessResponseHandler();
		}
		else if (responseStatus == CommandStatus.FAILED) {
			handler = new FailedResponseHandler();
		}
		else if (responseStatus == CommandStatus.UNMATCHED) {
			handler = new UnmatchedResponseHandler();
		}
		else if (responseStatus == CommandStatus.INVALID) {
			handler = new InvalidResponseHandler();
		}
		
		return handler;
	}

}
