package com.ahwers.marvinclient.responsehandlers;

import com.ahwers.marvin.response.MarvinResponse;
import com.ahwers.marvin.response.resource.ResourceRepresentationType;
import com.ahwers.marvinclient.BrowserDriver;

// TODO: I think that the response handler will be the same no matter the status???
//		 If so, pop this logic in the abstract super class and leave the children until we know for a fact that they will not need unique logic.
public class SuccessResponseHandler extends ResponseHandler {

	@Override
	public void handleResponse(MarvinResponse response) {
		this.outputResponseMessage(response.getResponseMessage());
	}

}
