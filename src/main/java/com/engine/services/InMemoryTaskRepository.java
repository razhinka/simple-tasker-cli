package com.engine.services;

import com.engine.model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryTaskRepository {
    private static final InMemoryTaskRepository INSTANCE = new InMemoryTaskRepository();
    private static final List<Task> tasks = new ArrayList<>();

    private InMemoryTaskRepository() {

    }

    public static InMemoryTaskRepository getInstance() {
        return INSTANCE;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

}
