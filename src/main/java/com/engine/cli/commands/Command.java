package com.engine.cli.commands;

public interface Command {
    String commandName();
    void execute(String[] args);
}
