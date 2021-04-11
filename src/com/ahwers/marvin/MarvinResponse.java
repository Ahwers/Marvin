package com.ahwers.marvin;

import java.util.ArrayList;
import java.util.List;

public class MarvinResponse {
	
	private String responseMessage;
	private List<Resource> responseResources = new ArrayList<>();
	private CommandOutcome status;
	private Throwable failException;
	
	public MarvinResponse(CommandOutcome status, String responseMessage) {
		this.status = status;
		this.responseMessage = responseMessage;
	}
	
	public MarvinResponse(CommandOutcome status) {
		this.status = status;
	}
	
	public MarvinResponse() {}
	
	public void addResource(Resource newResource) {
		responseResources.add(newResource);
	}
	
	public Resource getResource(int resourceIndex) {
		return this.responseResources.get(resourceIndex);
	}
	
	public List<Resource> getResources() {
		return this.responseResources;
	}
	
	public CommandOutcome getCommandStatus() {
		return this.status;
	}
	
	public void setCommandStatus(CommandOutcome status) {
		this.status = status;
	}
	
	public String getResponseMessage() {
		return this.responseMessage;
	}
	
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public void setFailException(Throwable failException) {
		this.failException = failException;
	}
	
	public Throwable getFailException() {
		return this.failException;
	}

}
