package com.engine.repository;

import com.engine.model.Task;

import java.util.Optional;

public interface TaskRepository extends CrudRepository<Task> {
    void markDone(Task task);
    Optional<Task> findByTitle(String title);
    void deleteByTitle(String title);
}
