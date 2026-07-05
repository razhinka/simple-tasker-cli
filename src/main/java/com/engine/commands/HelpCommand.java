package com.engine.commands;

public class HelpCommand implements Command {
    @Override
    public String commandName() {
        return "help";
    }
    @Override
    public void execute(String[] args) {
        System.out.println("im too lazy to write lmao");
    }
}
