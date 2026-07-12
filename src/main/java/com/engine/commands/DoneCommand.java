package com.engine.commands;

import com.engine.model.Status;
import com.engine.model.Task;
import com.engine.services.Repository;

import java.util.Optional;

public class DoneCommand implements Command {
    private final Repository<Task> repository;

    public DoneCommand(Repository<Task> repository) {
        this.repository = repository;
    }

    @Override
    public String commandName() {
        return "done";
    }

    @Override
    public void execute(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("Empty command argument");
        }
        if (args.length > 1) {
            throw new IllegalArgumentException("Too many arguments");
        }
        String title  = args[0];
        Optional<Task> getTask = repository.findById(title);
        if (getTask.isPresent()) {
            Task task = getTask.get();
            if (task.isDone()) {
                System.out.printf("Task %s is already done%n", title);
            }
            else {
                task = new Task.Builder(task).status(Status.DONE).build();
                repository.save(task);
                System.out.println("Task " + title + " is done");
            }
        }
        else {
            System.out.printf("Task %s not found%n", title);
        }
    }
}
