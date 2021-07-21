package com.ahwers.marvin.response;

import java.util.ArrayList;
import java.util.List;

import com.ahwers.marvin.response.resource.Resource;

public class MarvinResponse {
	
	private String message;
	private Resource resource;
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
	
	public void setResource(Resource resource) {
		this.resource = resource;
	}
	
	public Resource getResource() {
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