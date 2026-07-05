package com.engine.commands;

public class ExitCommand implements Command {

    @Override
    public String commandName() {
        return "exit";
    }

    @Override
    public void execute(String[] args) {
        System.exit(0);
    }
}
