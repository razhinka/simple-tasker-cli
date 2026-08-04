package com.engine.commands;

import com.engine.exceptions.InvalidCommandException;
import com.engine.model.Priority;
import com.engine.model.Status;
import com.engine.model.Tag;
import com.engine.model.Task;
import com.engine.services.TaskRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class ListTasksCommand implements Command {
    private final TaskRepository repository;

    public ListTasksCommand(TaskRepository repository) {
        this.repository = repository;
    }

    @Override
    public String commandName() {
        return "list-tasks";
    }

    @Override
    public void execute(String[] args) {
        List<Predicate<Task>> predicates = new ArrayList<>();
        String order = "asc";
        String sortBy = "id";
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--overdue":
                    predicates.add(task -> !task.isDone() &&
                            task.getDeadline().isPresent() &&
                            task.getDeadline().get().isBefore(LocalDateTime.now()));
                    break;
                case "--status":
                    Status status = Status.valueOf(args[++i].toUpperCase());
                    predicates.add(task -> task.getStatus().equals(status));
                    break;
                case "--priority":
                    Priority priority = Priority.valueOf(args[++i].toUpperCase());
                    predicates.add(task -> task.getPriority().equals(priority));
                    break;
                case "--tag":
                    Tag tag = new Tag(args[++i]);
                    predicates.add(task -> task.hasTag(tag));
                    break;
                case "--sort":
                    sortBy = args[++i];
                    break;
                case "--order":
                    order = args[++i];
                    break;
                default:
                    throw new InvalidCommandException("Unknown command: " + args[i]);
            }
        }

        Predicate<Task> combined = predicates.stream()
                .reduce(Predicate::and)
                .orElse(task -> true);
        Stream<Task> stream = repository.findAll().stream().filter(combined);

        Comparator<Task> comparator = getTaskComparator(sortBy, order);

        List<Task> result = stream.sorted(comparator).toList();

        if (result.isEmpty()) {
            System.out.println("No tasks found.");
        }
        for (Task task : result) {
            System.out.println(task);
        }
    }

    private static Comparator<Task> getTaskComparator(String sortBy, String order) {
        Comparator<Task> comparator = switch (sortBy) {
            case "id" -> Comparator.comparing(Task::getId);
            case "title" -> Comparator.comparing(Task::getTitle);
            case "project" -> Comparator.comparing(Task::getProject);
            case "priority" -> Comparator.comparing(Task::getPriority);
            default -> throw new IllegalArgumentException("Unknown sorting type: " + sortBy);
        };

        if (!(Objects.equals(order, "asc") || Objects.equals(order, "desc"))) {
            throw new InvalidCommandException("Invalid order: " + order + ". Supported orders: asc, desc");
        }
        if (order.equals("desc")) {
            comparator = comparator.reversed();
        }
        return comparator;
    }
}