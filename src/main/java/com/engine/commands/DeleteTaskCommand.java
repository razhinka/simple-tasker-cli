package com.engine.commands;

import com.engine.exceptions.TaskNotFoundException;
import com.engine.services.TaskRepository;

public class DeleteTaskCommand implements Command {
    private final TaskRepository repository;

    public DeleteTaskCommand(TaskRepository repository) {
        this.repository = repository;
    }

    @Override
    public String commandName() {
        return "delete-task";
    }

    @Override
    public void execute(String[] args) {
        String arg = args[0];
        if (arg.equals("--title")) {
            if (repository.findByTitle(args[1]).isPresent()) {
                repository.deleteByTitle(args[1]);
            } else {
                throw new TaskNotFoundException("Task with title " + args[1] + " does not exist");
            }
            System.out.println("Task with title " + args[1] + " has been deleted");
            return;
        }
        long id =  Long.parseLong(arg);
        if (repository.findById(id).isPresent()) {
            repository.deleteById(id);
        } else {
            throw new TaskNotFoundException("Task with id " + id + " does not exist");
        }
        repository.deleteById(Long.parseLong(arg));
        System.out.println("Task with id " + id + " has been deleted");
    }
}
