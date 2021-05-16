package com.ahwers.marvin;

import java.util.ArrayList;
import java.util.List;

public class MarvinResponse {
	
	private String message;
	private Resource resource;
	private CommandOutcome status;
	private Throwable failException;
	
	public MarvinResponse(CommandOutcome status, String responseMessage) {
		this.status = status;
		this.message = responseMessage;
	}
	
	public MarvinResponse(CommandOutcome status) {
		this.status = status;
	}
	
	public MarvinResponse() {}
	
	public void setResource(Resource resource) {
		this.resource = resource;
	}
	
	public Resource getResource() {
		return this.resource;
	}
	
	public CommandOutcome getCommandStatus() {
		return this.status;
	}
	
	public void setCommandStatus(CommandOutcome status) {
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
