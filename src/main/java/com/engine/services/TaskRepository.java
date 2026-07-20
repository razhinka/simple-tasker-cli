package com.engine.services;

import com.engine.model.Task;

import java.util.Optional;

public interface TaskRepository extends Repository<Task> {
    void markDone(Task task);
    Optional<Task> findByTitle(String title);
    void deleteByTitle(String title);
}
