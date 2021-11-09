package com.ahwers.marvin.applications.todo.task_creation;

import java.util.Map;

import com.ahwers.marvin.framework.application.Application;
import com.ahwers.marvin.framework.application.ApplicationAdaptor;
import com.ahwers.marvin.framework.application.CommandMatch;
import com.ahwers.marvin.framework.application.IntegratesApplication;
import com.ahwers.marvin.framework.resource.MarvinApplicationResource;
import com.ahwers.marvin.framework.response.MarvinResponse;
import com.ahwers.marvin.service.MarvinApplication;

@IntegratesApplication("Task Creator")
public class TaskCreationApplicationAdaptor extends ApplicationAdaptor {

    // TODO: So because we need a way for applications sent to the client side to update marvin with any new state, maybe we make this a requirement of all application adaptors.
    //       method such as updateApplicationState()
    // TODO: Also add the method buildResource to ApplicationAdaptor
    @Override
    protected Application instantiateApplication() {
        return new TaskBuilder();
    }

    // TODO: Will this be returning the task creation UI or just making one on trust (and as such returning a creation status string)?
	// @CommandMatch("remind me to (?<action>.+)(?: on (?<date>.+))?(?: at (?<time>.+))")
    // public MarvinResponse createWholeTask(Map<String, String> arguments) {

    // }

    // TODO: Need to return the task creation application UI with the currently filled in values
	@FeatureContext("new_task")
	@CommandMatch("remind me to (?<action>)")
	public MarvinApplicationResource initiateTaskCreation(Map<String, String> arguments) {
        TaskBuilder taskBuilder = (TaskBuilder) getApplication();
        
        String taskAction = arguments.get("action");
        taskBuilder.newTask(taskAction);

        return buildResource("TODO");
	}

    // TODO: Need to return a string saying that the task has been created successfully
    @FeatureContext("new_task")
    @CommandMatch("commit")
    public MarvinApplicationResource commitTaskCreation(Map<String, String> arguments) {
        TaskBuilder taskBuilder = (TaskBuilder) getApplication();
        Task newTask = taskBuilder.create();
        MicrosoftTodoTaskRepository.getInstance().createNewTask(newTask);

        return buildResource("Task created");
    }

}
