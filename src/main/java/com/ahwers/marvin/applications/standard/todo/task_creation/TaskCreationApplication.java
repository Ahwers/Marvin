package com.ahwers.marvin.applications.standard.todo.task_creation;

import java.util.Map;

import com.ahwers.marvin.framework.application.Application;
import com.ahwers.marvin.framework.application.action.annotations.CommandMatch;
import com.ahwers.marvin.framework.application.annotations.IntegratesApplication;
import com.ahwers.marvin.framework.resource.MarvinApplicationResource;

@IntegratesApplication("Task Creator")
public class TaskCreationApplication extends Application {

    // TODO: Add the method buildResource to Application

    // TODO: Will this be returning the task creation UI or just making one on trust (and as such returning a creation status string)?
	@CommandMatch("remind me to (?<action>.+)(?: on (?<date>.+))?(?: at (?<time>.+))")
    public MarvinApplicationResource createWholeTask(Map<String, String> arguments) {
        return null;
    }

	@ContextInitiator("new_task")
	@CommandMatch("remind me to (?<action>.+)")
	public MarvinApplicationResource initiateTaskCreation(Map<String, String> arguments) {
        return null; 
	}

    @FeatureContext("new_task")
    @CommandMatch("on (?<datetime>.+)")
    public MarvinApplicationResource setDateTimeForTask(Map<String, String> arguments) {
        return null;
    }

    // TODO: Need to return a string saying that the task has been created successfully
    @ContextCloser("new_task")
    @CommandMatch("commit")
    public MarvinApplicationResource commitTaskCreation(Map<String, String> arguments) {
        return null; 
    }

}
