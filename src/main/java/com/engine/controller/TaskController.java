package com.engine.controller;

import com.engine.model.Task;
import com.engine.repository.SQLTaskRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final SQLTaskRepository taskRepository;

    public TaskController(SQLTaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @GetMapping
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @GetMapping("/{id}")
    public Task getTaskById(@PathVariable long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task with id " + id + " not found"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task createTask(@RequestBody Task task) {
        if (task.getId() != 0) {
            task = new Task.Builder(task).id(0).build();
        }
        return taskRepository.save(task);
    }

    @PutMapping("/{id}")
    public Task updateTask(@PathVariable long id, @RequestBody Task task) {
        Task taskToSave = new Task.Builder(task).id(id).build();
        return taskRepository.save(taskToSave);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable long id) {
        taskRepository.deleteById(id);
    }
}