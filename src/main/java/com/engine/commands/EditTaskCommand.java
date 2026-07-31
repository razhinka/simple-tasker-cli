package com.engine.commands;

import com.engine.model.*;
import com.engine.services.TaskRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

public class EditTaskCommand implements Command {
    private final TaskRepository repository;
    private static final DateTimeFormatter DEADLINE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public EditTaskCommand(TaskRepository repository) {
        this.repository = repository;
    }

    @Override
    public String commandName() {
        return "edit-task";
    }

    @Override
    public void execute(String[] args) {
        long id = Long.parseLong(args[0]);
        Task task = repository.findById(id).orElse(null);
        if (task == null) {
            throw new RuntimeException("Task with id " + id + " not found");
        }
        Task.Builder newTask = new Task.Builder(task);
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--title":
                    newTask.title(args[++i]);
                    break;
                case "--project":
                    newTask.project(new Project(0, args[++i]));
                    break;
                case "--assignee":
                    newTask.assignee(new User(0, args[++i]));
                case "--description":
                    newTask.description(args[++i]);
                    break;
                case "--priority":
                    newTask.priority(Priority.valueOf(args[++i].toUpperCase()));
                    break;
                case "--status":
                    newTask.status(Status.valueOf(args[++i].toUpperCase()));
                case "--deadline":
                    try {
                        newTask.deadline(Optional.of(LocalDateTime.parse(args[++i], DEADLINE_FORMATTER)).get());
                    } catch (DateTimeParseException e) {
                        throw new IllegalArgumentException("Invalid date format. Correct format: dd.MM.yyyy HH:mm");
                    }
                    break;
                case "--tag":
                    System.out.println("Sorry, but im really lazy to implement this case");
            }
        }
        repository.delete(task);
        repository.save(newTask.build());
    }
}
