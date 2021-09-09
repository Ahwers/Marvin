package com.ahwers.marvin.framework;

import com.ahwers.marvin.framework.application.ApplicationsManager;
import com.ahwers.marvin.framework.command.CommandFormatter;
import com.ahwers.marvin.framework.response.MarvinResponse;
import com.ahwers.marvin.framework.response.MarvinResponseFactory;

public class Marvin {
	
	// TODO: Could implement response routing or something, so like issuing a command from my phone but getting the response sent to my desktop. This class could catch the "to my desktop" string or something and route the response that way.
	
	private CommandFormatter commandFormatter;
	private MarvinResponseFactory responseFactory;
	
	public Marvin() {
		this.commandFormatter = new CommandFormatter();
		this.responseFactory = MarvinResponseFactory.getResponseFactoryForApplicationManager(new ApplicationsManager());
	}
	
	public MarvinResponse processCommand(String originalCommand) {
		String command = commandFormatter.formatCommand(originalCommand);
		System.out.println(command);
		
		// TODO: Log command recieved and formatted
		
		return responseFactory.getResponseForCommand(command);
	}
	
	public MarvinResponse processAction(String stringifiedAppAction) {
		// TODO: Log action recieved
		
		return responseFactory.getResponseForAppAction(stringifiedAppAction);
	}
	
}