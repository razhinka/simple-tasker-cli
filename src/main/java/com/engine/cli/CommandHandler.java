package com.engine.cli;

import com.engine.cli.commands.Command;
import com.engine.cli.commands.ParsedCommand;

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
        command.execute(parsedCommand.args());
    }
}
