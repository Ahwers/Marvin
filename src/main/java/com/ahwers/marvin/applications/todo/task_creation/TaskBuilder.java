package com.ahwers.marvin.applications.todo.task_creation;

import com.ahwers.marvin.framework.application.Application;

public class TaskBuilder extends Application {

    public void newTask(String task) {

    }

    public Task create() {
        return new Task();
    }

}
