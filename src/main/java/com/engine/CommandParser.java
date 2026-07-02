package com.engine;

import com.engine.model.ParsedCommand;

import java.util.HashMap;
import java.util.HashSet;

public class CommandParser {
    private final String[] args;

    public CommandParser(String[] args) {
        this.args = args;
    }

    public ParsedCommand parse() {
        if (args.length == 0) {
            System.out.println("Empty string");
            return null;
        }
        String command = args[0];
        switch (command) {
            case "add-task":
                 return handleAddTask();
            case "list-tasks":
                return handleListTasks();
            case "done":
                return handleDone();
            case "help":
                return handleHelp();
            case "exit":
                return handleExit(); // Will call the command to terminate the job.
            default:
                System.out.printf("Unknown command %s\n", command);
                return null;
        }
    }

    private ParsedCommand handleExit() {
        return new ParsedCommand("exit", new  HashMap<>());
    }

    private ParsedCommand handleHelp() {
        return new ParsedCommand("help", new HashMap<>());
    }

    private ParsedCommand handleDone() {
        if (args.length < 2) {
            System.out.println("Not enough arguments");
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("title", args[1]);
        return new ParsedCommand("done", map);
    }

    private ParsedCommand handleAddTask() {
        if (args.length < 6) {
            System.out.println("Not enough arguments");
            return null;
        }
        HashMap<String, String> arguments = new HashMap<>();
        arguments.put("title", args[1]);
        HashSet<String> tags = new HashSet<>();
        for (int i = 2; i < args.length - 1; i++) {
            String param = args[i];
            switch (param) {
                case "--project":
                    arguments.put("project", args[++i]);
                    break;
                case "--assignee":
                    arguments.put("assignee", args[++i]);
                    break;
                case "--tag":
                    tags.add(args[++i]);
                    break;
                case "--priority":
                    arguments.put("priority", args[++i]);
                    break;
                case "--description":
                    arguments.put("description", args[++i]);
                    break;
                default:
                    System.out.println("Unknown argument: " + param);
            }
        }
        if (!arguments.containsKey("project")) {
            System.out.println("Missing project argument");
            return null;
        }
        if (!arguments.containsKey("assignee")) {
            System.out.println("Missing assignee argument");
            return null;
        }
        arguments.put("tags", tags.toString());
        return new ParsedCommand(args[0], arguments);
    }

    private ParsedCommand handleListTasks() {
        if (args.length < 1) {
            System.out.println("Empty string");
            return null;
        }
        if (args.length == 1) {
            return new ParsedCommand(args[0], new HashMap<>());
        }
        HashMap<String, String> arguments = new HashMap<>();
        for (int i = 1; i <  args.length - 1; i++) {
            String param = args[i];
            switch (param) {
                case "--project":
                    arguments.put("project", args[++i]);
                    break;
                case "--assignee":
                    arguments.put("assignee", args[++i]);
                    break;
                case "--priority":
                    arguments.put("priority", args[++i]);
                    break;
                case "--tag":
                    if (!arguments.containsKey("tags")) {
                        arguments.put("tags", args[++i]);
                    } else {
                        arguments.put("tags", args[++i] + ";" + arguments.get("tags"));
                    }
                    break;
            }
        }
        return new ParsedCommand(args[0], arguments);
    }
}