package com.engine.commands;

public interface Command {
    String commandName();
    void execute(String[] args);
}
