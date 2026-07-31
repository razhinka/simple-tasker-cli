package com.engine.commands;

import com.engine.model.Priority;
import com.engine.model.Status;
import com.engine.model.Tag;
import com.engine.model.Task;
import com.engine.services.TaskRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StatsCommand implements Command {
    private final TaskRepository repository;

    public StatsCommand(TaskRepository repository) {
        this.repository = repository;
    }

    @Override
    public String commandName() {
        return "stats";
    }

    @Override
    public void execute(String[] args) {
        List<Task> tasks = repository.findAll();
        System.out.println("=== Task Statistics ===");
        System.out.println("Total number of tasks: " + repository.count());
        Map<Status, Long> statusCounter = tasks.stream()
                .collect(Collectors.groupingBy(Task::getStatus, Collectors.counting()));
        System.out.println("By status:");
        System.out.println("  Done: " + statusCounter.get(Status.DONE));
        System.out.println("  In progress: " + statusCounter.get(Status.IN_PROGRESS));
        System.out.println("  Not started: " + statusCounter.get(Status.NOT_STARTED));
        System.out.println("By priority:");
        Map<Priority, Long> priorityCounter = tasks.stream().collect(Collectors.groupingBy(Task::getPriority, Collectors.counting()));
        System.out.println("  Low: " + priorityCounter.get(Priority.LOW));
        System.out.println("  Medium: " + priorityCounter.get(Priority.MEDIUM));
        System.out.println("  High: " + priorityCounter.get(Priority.HIGH));
        System.out.println("Top 3 tags:");
        Map<Tag, Long> top3Tags = tasks.stream().flatMap(task -> task.getTags().stream())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        List<Map.Entry<Tag, Long>> top3TagsEntries = top3Tags.entrySet().stream()
                .sorted(Map.Entry.<Tag, Long>comparingByValue().reversed())
                .limit(3)
                .toList();
        for (Map.Entry<Tag, Long> entry : top3TagsEntries) {
            System.out.println(" " + entry.getKey() + " : " + entry.getValue());
        }
        List<Task> overdue = tasks.stream().filter(task -> task.getDeadline().isPresent() &&
                !task.isDone() &&
                task.getDeadline().get().isBefore(LocalDateTime.now()))
                .toList();
        System.out.println("Overdue tasks: " + overdue.size());
    }
}
