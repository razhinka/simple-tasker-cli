package com.engine.controller;

import com.engine.model.Task;
import com.engine.repository.TaskRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @GetMapping
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @GetMapping("/{id}")
    public Task getTaskById(@PathVariable long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with id: " + id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task createTask(@RequestBody Task task) {
        return taskRepository.save(task);
    }

    @PutMapping("/{id}")
    public Task updateTask(@PathVariable long id, @RequestBody Task task) {
        if (!taskRepository.existsById(id)) {
            throw new IllegalArgumentException("Task not found with id: " + id);
        }
        task.setId(id);
        return taskRepository.save(task);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable long id) {
        if (!taskRepository.existsById(id)) {
            throw new IllegalArgumentException("Task not found with id: " + id);
        }
        taskRepository.deleteById(id);
    }
}