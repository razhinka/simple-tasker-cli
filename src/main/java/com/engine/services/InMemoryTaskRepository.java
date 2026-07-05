package com.engine.services;

import com.engine.model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryTaskRepository {
    private static final InMemoryTaskRepository INSTANCE = new InMemoryTaskRepository();
    private static final List<Task> tasks = new ArrayList<>();

    private InMemoryTaskRepository() {}

    public static InMemoryTaskRepository getInstance() {
        return INSTANCE;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    /**
     * Помечает задачу как выполненную по её идентификатору (или title).
     * @param title название задачи (или id)
     * @return true, если задача найдена и обновлена, false — если не найдена
     */
    public boolean markTaskDone(String title) { //QUESTION: Этот метод public, значит в теории он может быть вызван кем угодно. Нужно переработать архитектуру для безопасности?
        for (Task task : tasks) {
            if (task.getTitle().equals(title)) {
                Task newTask = task.markDone();
                tasks.remove(task);
                tasks.add(newTask);
                return true;
            }
        }
        return false;
    }
}
