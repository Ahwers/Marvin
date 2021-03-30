package com.ahwers.marvin;

import java.util.List;

import com.ahwers.marvin.applications.ApplicationAction;
import com.ahwers.marvin.applications.ApplicationsManager;

public class Marvin {
	
	private ApplicationsManager applicationsManager = new ApplicationsManager();
	
	public MarvinResponse runCommand(String originalCommand) {
		String command = CommandProcessor.processCommand(originalCommand);
		System.out.println("Command: \"" + command + "\""); // TOOD: Log command recieved
		
		List<ApplicationAction> possibleActions = applicationsManager.getApplicationActionsToConsumeCommand(command);
		
		ApplicationAction actionToInvoke = null;
		if (possibleActions.size() == 1) {
			actionToInvoke = possibleActions.get(0);
		}
		else if (possibleActions.size() > 1) {
			// TODO: Implement decision algorithm, and user selection functionality if a confident decision cannot be made.
			// TODO: User selection functionality will need to developed by individual client applications if i go the route of packaging Marvin libraries and using them. Android could create a dialog box, desktop could create a swing thing. Will need to annotate those classes with @CommandSelector or something and have this method search for that class so we can inject it.
		}
		
		MarvinResponse response = new MarvinResponse(CommandStatus.INVALID);
		if (actionToInvoke != null) {
			response = applicationsManager.executeApplicationAction(actionToInvoke);
		}
		
		// Set up default responses
		if (response.getResponseMessage() == null) {
			CommandStatus commandStatus = response.getCommandStatus();
			if (commandStatus == CommandStatus.INVALID) {
				response.setResponseMessage("Sorry, I have not been programmed to process that command.");
			}
			else if (commandStatus == CommandStatus.FAILED) {
				response.setResponseMessage("Something failed, see my logs for it's cause.");
			}
			else if (commandStatus == CommandStatus.UNMATCHED) {
				response.setResponseMessage("Please be more specific.");
				// TODO: Then the client can display a list of possible commands to choose from.
			}
		}
		
		return response;
	}

	
}
