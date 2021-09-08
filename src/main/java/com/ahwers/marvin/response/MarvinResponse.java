package com.ahwers.marvin.response;

import java.util.ArrayList;
import java.util.List;

import com.ahwers.marvin.response.resource.MarvinApplicationResource;

public class MarvinResponse {

	// TODO: Remove as much stuff that JAX-RS already does from this (status)
	//		 Maybe setting exception analysis documents as the resource for failed requests and not using anything else.
	//		 Maybe just return resources (who have their own messages) and get rid of everything else
	//		 Fail resources could take their message values from the exception message that caused them.
	
	private String message;
	private MarvinApplicationResource resource;
	private RequestOutcome status;
	private Throwable failException;
	
	public MarvinResponse(RequestOutcome status, String responseMessage) {
		this.status = status;
		this.message = responseMessage;
	}
	
	public MarvinResponse(RequestOutcome status) {
		this.status = status;
	}
	
	public MarvinResponse() {}
	
	public void setResource(MarvinApplicationResource resource) {
		this.resource = resource;
	}
	
	public MarvinApplicationResource getResource() {
		return this.resource;
	}
	
	public RequestOutcome getCommandStatus() {
		return this.status;
	}
	
	public void setCommandStatus(RequestOutcome status) {
		this.status = status;
	}
	
	public String getResponseMessage() {
		return this.message;
	}
	
	public void setResponseMessage(String responseMessage) {
		this.message = responseMessage;
	}

	public void setFailException(Throwable failException) {
		this.failException = failException;
	}
	
	public Throwable getFailException() {
		return this.failException;
	}

}
