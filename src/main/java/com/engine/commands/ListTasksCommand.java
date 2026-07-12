package com.engine.commands;

import com.engine.model.Priority;
import com.engine.model.Status;
import com.engine.model.Tag;
import com.engine.model.Task;
import com.engine.services.Repository;

import java.time.LocalDateTime;
import java.util.List;

public class ListTasksCommand implements Command {
    private final Repository<Task> repository;

    public ListTasksCommand(Repository<Task> repository) {
        this.repository = repository;
    }

    @Override
    public String commandName() {
        return "list-tasks";
    }

    @Override
    public void execute(String[] args) {
        if (args.length == 0) {
            for (Task task : repository.findAll()) {
                System.out.println(task);
            }
            return;
        }
        List<Task> result = repository.findAll();
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--overdue":
                    for (Task task : List.copyOf(result)) {
                        if (task.isDone() || task.getDeadline().isEmpty()) {
                            result.remove(task);
                            continue;
                        }
                        if (task.getDeadline().isPresent()) {
                            LocalDateTime deadline = task.getDeadline().get();
                            if (deadline.isBefore(LocalDateTime.now())) {
                                result.remove(task);
                            }
                        }
                    }
                    break;
                case "--status":
                    Status status = Status.valueOf(args[++i].toUpperCase());
                    for (Task task : List.copyOf(result)) {
                        if (!task.getStatus().equals(status)) {
                            result.remove(task);
                        }
                    }
                    break;
                case "--priority":
                    Priority priority = Priority.valueOf(args[++i].toUpperCase());
                    for (Task task : List.copyOf(result)) {
                        if (!task.getPriority().equals(priority)) {
                            result.remove(task);
                        }
                    }
                    break;
                case "--tags":
                    Tag tag = new Tag(args[++i]);
                    for (Task task : List.copyOf(result)) {
                        if (!task.getTags().contains(tag)) {
                            result.remove(task);
                        }
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Unknown command: " + args[i]);
            }
        }
        if (result.isEmpty()) {
            System.out.print("No tasks found.\n");
        }
        for  (Task task : result) {
            System.out.println(task);
        }
    }
}
