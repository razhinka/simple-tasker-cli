package com.engine.commands;

import com.engine.model.*;
import com.engine.services.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class AddTaskCommand implements Command {
    private final Repository<Task> repository;
    private static final DateTimeFormatter DEADLINE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public AddTaskCommand(Repository<Task> repository) {
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
        Optional<LocalDateTime> deadline = Optional.empty();

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
                    priority = Priority.valueOf(args[++i].toUpperCase());
                    break;
                case "--description":
                    description = args[++i];
                    break;
                case "--status":
                    status = Status.valueOf(args[++i].toUpperCase());
                    break;
                case "--deadline":

                    try {
                        deadline = Optional.of(LocalDateTime.parse(args[++i], DEADLINE_FORMATTER));
                    } catch (DateTimeParseException e) {
                        throw new IllegalArgumentException("Invalid date format. Correct format: dd.MM.yyyy HH:mm");
                    }
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

        Task.Builder task = new Task.Builder(0, title, project, assignee)
                .tags(tags)
                .description(description)
                .priority(priority)
                .status(status);
        deadline.ifPresent(task::deadline);
        repository.save(task.build());
    }

    @Override
    public String commandName() {
        return "add-task";
    }
}
