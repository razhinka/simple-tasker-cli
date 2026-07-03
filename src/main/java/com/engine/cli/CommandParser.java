package com.engine.cli;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class CommandParser {
    private final String[] args;

    public CommandParser(String[] args) {
        this.args = args;
    }

    public ParsedCommand parse() throws IllegalArgumentException {
        if (args.length == 0) {
            throw new IllegalArgumentException("Empty command line arguments");
        }
        String command = args[0];
        return switch (command) {
            case "add-task" -> handleAddTask();
            case "list-tasks" -> handleListTasks();
            case "done" -> handleDone();
            case "help" -> handleHelp();
            case "exit" -> handleExit(); // Will call the command to terminate the job.
            default -> throw new IllegalArgumentException("Unknown command: " + command);
        };
    }

    private ParsedCommand handleExit() {
        return new ParsedCommand("exit", new  HashMap<>());
    }

    private ParsedCommand handleHelp() {
        return new ParsedCommand("help", new HashMap<>());
    }

    private ParsedCommand handleDone() throws IllegalArgumentException {
        if (args.length < 2) {
            throw new IllegalArgumentException("Invalid command line arguments");
        }
        HashMap<String, List<String>> map = new HashMap<>();
        map.put("title", List.of(args[1]));
        return new ParsedCommand("done", map);
    }

    private ParsedCommand handleAddTask() throws IllegalArgumentException {
        if (args.length < 6) {
            throw new IllegalArgumentException("Not enough arguments");
        }
        HashMap<String, List<String>> arguments = new HashMap<>();
        arguments.put("title", List.of(args[1]));
        HashSet<String> tags = new HashSet<>();
        for (int i = 2; i < args.length - 1; i++) {
            String param = args[i];
            switch (param) {
                case "--project":
                    arguments.put("project", List.of(args[++i]));
                    break;
                case "--assignee":
                    arguments.put("assignee", List.of(args[++i]));
                    break;
                case "--tag":
                    tags.add(args[++i]);
                    break;
                case "--priority":
                    arguments.put("priority", List.of(args[++i]));
                    break;
                case "--description":
                    arguments.put("description", List.of(args[++i]));
                    break;
                default:
                    throw new IllegalArgumentException("Unknown argument: " + param);
            }
        }
        if (!arguments.containsKey("project")) {
            throw new IllegalArgumentException("Missing project argument");
        }
        if (!arguments.containsKey("assignee")) {
            throw new IllegalArgumentException("Missing assignee argument");
        }
        arguments.put("tags", List.copyOf(tags));
        return new ParsedCommand(args[0], arguments);
    }

    private ParsedCommand handleListTasks() throws IllegalArgumentException {
        if (args.length < 1) {
            throw new IllegalArgumentException("Invalid command line arguments");
        }
        if (args.length == 1) {
            return new ParsedCommand(args[0], new HashMap<>());
        }
        HashMap<String, List<String>> arguments = new HashMap<>();
        for (int i = 1; i <  args.length - 1; i++) {
            String param = args[i];
            switch (param) {
                case "--project":
                    arguments.put("project", List.of(args[++i]));
                    break;
                case "--assignee":
                    arguments.put("assignee", List.of(args[++i]));
                    break;
                case "--priority":
                    arguments.put("priority", List.of(args[++i]));
                    break;
                case "--tag":
                    if (!arguments.containsKey("tags")) {
                        arguments.put("tags", List.of(args[++i]));
                    } else {
                        arguments.get("tags").add(args[++i]);
                    }
                    break;
            }
        }
        return new ParsedCommand(args[0], arguments);
    }
}