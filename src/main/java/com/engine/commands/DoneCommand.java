package com.engine.commands;

import com.engine.model.Task;
import com.engine.services.TaskRepository;

import java.util.Optional;

public class DoneCommand implements Command {
    private final TaskRepository repository;

    public DoneCommand(TaskRepository repository) {
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
        long id  = Long.getLong(args[0]);
        Optional<Task> getTask = repository.findById(id);
        if (getTask.isPresent()) {
            repository.markDone(getTask.get());
            System.out.println("Task  " + id + " marked done.");
        }
        else {
            System.out.println("Task with title \"" + id + "\" not found");
        }
    }
}
