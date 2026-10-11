package com.engine.cli;

import com.engine.cli.commands.Command;

import java.util.HashMap;
import java.util.Map;

public class CommandRegistry {
    private final Map<String, Command> commands;

    public CommandRegistry() {
        commands = new HashMap<>();
    }

    public void register(Command command) {
        commands.put(command.commandName(), command);
    }

    public Command getCommand(String commandName) {
        return commands.get(commandName);
    }
}
