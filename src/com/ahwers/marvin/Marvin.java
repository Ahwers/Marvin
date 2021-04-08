package com.ahwers.marvin;

import java.util.List;

import com.ahwers.marvin.applications.ApplicationAction;
import com.ahwers.marvin.applications.ApplicationsManager;

public class Marvin {
	
	private MarvinResponseFactory responseFactory;
	
	public Marvin() {
		this.responseFactory = MarvinResponseFactory.getResponseFactoryForApplicationManager(new ApplicationsManager());
	}
	
	public MarvinResponse command(String originalCommand) {
		String command = formatCommand(originalCommand);
		
		// TODO: Log command recieved and formatted
		
		return responseFactory.getResponseForCommand(command);
	}

	private String formatCommand(String originalCommand) {
		return CommandFormatter.formatCommand(originalCommand);
	}
	
}
