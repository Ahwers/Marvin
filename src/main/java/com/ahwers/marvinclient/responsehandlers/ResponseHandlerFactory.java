package com.ahwers.marvinclient.responsehandlers;

import com.ahwers.marvin.response.RequestOutcome;

public class ResponseHandlerFactory {
	
	public static ResponseHandler getHandlerForResponseStatus(RequestOutcome responseStatus) {
		ResponseHandler handler = null;
		if (responseStatus == RequestOutcome.SUCCESS) {
			handler = new SuccessResponseHandler();
		}
		else if (responseStatus == RequestOutcome.FAILED) {
			handler = new FailedResponseHandler();
		}
		else if (responseStatus == RequestOutcome.UNMATCHED) {
			handler = new UnmatchedResponseHandler();
		}
		else if (responseStatus == RequestOutcome.INVALID) {
			handler = new InvalidResponseHandler();
		}
		
		return handler;
	}

}
