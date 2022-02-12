package com.ahwers.marvin.framework.response;

import com.ahwers.marvin.framework.application.resource.ApplicationResource;
import com.ahwers.marvin.framework.response.enums.InvocationOutcome;

public class MarvinResponse {

	private String message;
	private ApplicationResource resource;
	private String jsonAppState;
	private InvocationOutcome status;
	private Throwable failException;

	public MarvinResponse(InvocationOutcome status, String message) {
		this.status = status;
		this.message = message;
	}
	
	public MarvinResponse(InvocationOutcome status) {
		this.status = status;
	}

	public Throwable getFailException() {
		return failException;
	}

	public void setFailException(Throwable failException) {
		this.failException = failException;
	}

	public InvocationOutcome getStatus() {
		return status;
	}

	public void setStatus(InvocationOutcome status) {
		this.status = status;
	}

	public String getJsonAppState() {
		return jsonAppState;
	}

	public void setJsonAppState(String jsonAppState) {
		this.jsonAppState = jsonAppState;
	}

	public ApplicationResource getResource() {
		return resource;
	}

	public void setResource(ApplicationResource resource) {
		this.resource = resource;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
