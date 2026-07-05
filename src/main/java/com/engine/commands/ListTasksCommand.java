package com.engine.commands;

import com.engine.model.Task;
import com.engine.services.InMemoryTaskRepository;

public class ListTasksCommand implements Command {
    private final InMemoryTaskRepository repository;

    public ListTasksCommand(InMemoryTaskRepository repository) {
        this.repository = repository;
    }

    @Override
    public String commandName() {
        return "list-tasks";
    }

    @Override
    public void execute(String[] args) {
        for (Task task : repository.getTasks()) {
            System.out.println(task);
        }
    }
}
