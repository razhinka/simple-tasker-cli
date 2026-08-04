package com.engine.services;

import com.engine.model.Task;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public final class FileTaskRepository implements TaskRepository {
    private final File file;
    private final ObjectMapper mapper;
    AtomicLong idGenerator = new AtomicLong();
    Map<Long, Task> storage = new HashMap<>();

    public FileTaskRepository(File file, ObjectMapper mapper) {
        this.file = file;
        this.mapper = mapper;
        loadFromFile();
    }

    @Override
    public void markDone(Task task) {
        task = task.markDone();
        storage.put(task.getId(), task);
        saveToFile();
    }

    @Override
    public Optional<Task> findByTitle(String title) {
        List<Task> t = storage.values().stream().filter(task -> task.getTitle().equals(title)).toList();
        if (t.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(t.getFirst());
    }

    @Override
    public void deleteByTitle(String title) {
        storage.entrySet().removeIf(entry -> entry.getValue().getTitle().equals(title));
        saveToFile();
    }

    @Override
    public void save(Task o) {
        Task task = new Task.Builder(o)
                .id(idGenerator.getAndIncrement())
                .build();
        storage.put(task.getId(), task);
        saveToFile();
    }

    @Override
    public void delete(Task o) {
        storage.remove(o.getId());
        saveToFile();
    }

    @Override
    public List<Task> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Optional<Task> findById(long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public void deleteById(long id) {
        storage.remove(id);
        saveToFile();
    }

    @Override
    public long count() {
        return storage.size();
    }

    private void loadFromFile() {
        try {
            List<Task> tasks = mapper.readValue(
                    file,
                    new TypeReference<>() {
                    }
            );
            long maxId = 0;
            for (Task task : tasks) {
                storage.put(task.getId(), task);
                if (task.getId() > maxId) {
                    maxId = task.getId();
                }
            }
            idGenerator.set(maxId + 1);
        } catch (IOException e) {
            // Если файл не существует или повреждён, начинаем с пустого хранилища
            // При этом idGenerator остаётся 0 (первый id будет 0)
            if (!file.exists()) {
                idGenerator.set(1);
            } else {
                System.err.println("Error loading tasks from file: " + e.getMessage());
            }
        }
    }

    private void saveToFile() {
        try {
            List<Task> tasks = new ArrayList<>(storage.values());
            mapper.writeValue(file, tasks);
        } catch (IOException e) {
            System.err.println("Ошибка сохранения задач: " + e.getMessage());
        }
    }
}
