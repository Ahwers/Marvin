package com.ahwers.marvin;

// TODO: If we go centralised Marvin server, change this class's name to MarvinResponse
public class CommandExecutionOutcome {
	
	private String responseMessage;
	private CommandStatus status;
	private Throwable failException;
	
	public CommandExecutionOutcome(CommandStatus status, String responseMessage) {
		this.status = status;
		this.responseMessage = responseMessage;
		
		validate();
	}
	
	public CommandExecutionOutcome(CommandStatus status) {
		this.status = status;
		
		validate();
	}
	
	private void validate() {
		if (this.status == null) {
			throw new Error("\"status\" cannot be null.");
		}
	}
	
	public CommandStatus getCommandStatus() {
		return this.status;
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
