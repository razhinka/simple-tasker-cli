package com.engine.services;

import com.engine.commands.Command;
import com.engine.commands.ParsedCommand;

import java.io.IOException;

public class CommandHandler {
    private final CommandRegistry registry;

    public CommandHandler(CommandRegistry registry) {
        this.registry = registry;
    }

    public void handle(ParsedCommand parsedCommand) {
        Command command = registry.getCommand(parsedCommand.name());
        if (command == null) {
            throw new IllegalArgumentException("Unknown command: " + parsedCommand.name());
        }
        command.execute(parsedCommand.arguments());
    }
}
