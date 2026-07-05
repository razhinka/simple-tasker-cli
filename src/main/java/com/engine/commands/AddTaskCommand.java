package com.engine.commands;

import com.engine.model.*;
import com.engine.services.InMemoryTaskRepository;

import java.util.HashSet;
import java.util.Set;

public class AddTaskCommand implements Command {
    private final InMemoryTaskRepository repository;

    public AddTaskCommand(InMemoryTaskRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 5) {
            throw new IllegalArgumentException("Not enough arguments");
        }
        String title = args[0];
        String description = "";
        Set<Tag> tags = new HashSet<>();
        Project project = null;
        User assignee = null;
        Priority priority = Priority.LOW;
        Status status = Status.NOT_STARTED;


        for (int i = 1; i < args.length - 1; i++) {
            String param = args[i];
            switch (param) {
                case "--project":
                    project = new Project(0, args[++i]);
                    break;
                case "--assignee":
                    assignee = new User(0, args[++i]);
                    break;
                case "--tag":
                    tags.add(new Tag(args[++i]));
                    break;
                case "--priority":
                    priority = Priority.valueOf(args[++i]);
                    break;
                case "--description":
                    description = args[++i];
                    break;
                case "--status": //TODO: ENUMS!!!
                    status = Status.valueOf(args[++i]);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown argument: " + param);
            }
        }
        if (project == null) {
            throw new IllegalArgumentException("Missing project argument");
        }
        if (assignee == null) {
            throw new IllegalArgumentException("Missing assignee argument");
        }

        Task task = new Task.Builder(0, title, project, assignee)
                .tags(tags)
                .description(description)
                .priority(priority)
                .status(status)
                .build();
        repository.addTask(task);
    }

    @Override
    public String commandName() {
        return "add-task";
    }
}
