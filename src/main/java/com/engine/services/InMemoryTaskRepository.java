package com.engine.services;

import com.engine.model.Task;

import java.util.*;

public class InMemoryTaskRepository implements Repository<Task> {
    private static final Map<String, Task> tasks = new HashMap<>();

    @Override
    public void save(Task o) {
        tasks.put(o.getTitle(), o);
    }

    @Override
    public void delete(Task o) {
        tasks.remove(o.getTitle());
    }

    @Override
    public List<Task> findAll() {
        return List.of(tasks.values().toArray(new Task[0]));
    }

    @Override
    public Optional<Task> findById(String id) {
        if (tasks.containsKey(id)) {
            return Optional.of(tasks.get(id));
        }
        return Optional.empty();
    }

    @Override
    public void deleteById(String id) {
        tasks.remove(id);
    }

    @Override
    public long count() {
        return tasks.size();
    }
}
