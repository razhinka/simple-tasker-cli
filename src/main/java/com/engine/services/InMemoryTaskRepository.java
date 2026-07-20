package com.engine.services;

import com.engine.model.Status;
import com.engine.model.Task;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryTaskRepository implements TaskRepository {
    private static final Map<Long, Task> tasks = new HashMap<>();
    AtomicLong id = new AtomicLong();

    @Override
    public void save(Task o) {
        Task task = new Task.Builder(o).id(id.getAndIncrement()).build();
        tasks.put(task.getId(), task);
    }

    @Override
    public void delete(Task o) {
        tasks.remove(o.getId());
    }

    @Override
    public List<Task> findAll() {
        return List.of(tasks.values().toArray(new Task[0]));
    }

    @Override
    public Optional<Task> findById(long id) {
        if (tasks.containsKey(id)) {
            return Optional.of(tasks.get(id));
        }
        return Optional.empty();
    }

    @Override
    public void deleteById(long id) {
        tasks.remove(id);
    }

    @Override
    public long count() {
        return tasks.size();
    }

    @Override
    public void markDone(Task task) {
        task = new Task.Builder(task).status(Status.DONE).build();
        tasks.put(task.getId(), task);
    }

    @Override
    public Optional<Task> findByTitle(String title) {
        List<Task> t = tasks.values().stream().filter(task -> task.getTitle().equals(title)).toList();
        if (t.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(t.getFirst());
    }

    @Override
    public void deleteByTitle(String title) {
        List<Task> t = tasks.values().stream().filter(task -> task.getTitle().equals(title)).toList();
        for (Task task : t) {
            tasks.remove(task.getId());
        }
    }
}
