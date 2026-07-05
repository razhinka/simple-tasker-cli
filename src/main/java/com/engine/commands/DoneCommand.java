package com.engine.commands;

import com.engine.services.InMemoryTaskRepository;

public class DoneCommand implements Command {
    private final InMemoryTaskRepository repository;

    public DoneCommand(InMemoryTaskRepository repository) {
        this.repository = repository;
    }

    @Override
    public String commandName() {
        return "done";
    }

    @Override
    public void execute(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("Empty command argument");
        }
        if (args.length > 1) {
            throw new IllegalArgumentException("Too many arguments");
        }
        String title  = args[0];
        // Сейчас мы вызываем метод у репозитория и он возвращает boolean. Может сделать его void и оборачивать его вызов в try?
        boolean done = repository.markTaskDone(title);
        if (done) {
            System.out.println("Task has been done");
        }
        else {
            System.out.println("Task is not exist");
        }
    }
}
