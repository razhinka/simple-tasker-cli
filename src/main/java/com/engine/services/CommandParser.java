package com.engine.services;

import com.engine.commands.ParsedCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandParser {

    public CommandParser() {
    }

    public ParsedCommand parse(String inputLine) throws IllegalArgumentException {
        String[] tokens = tokenize(inputLine);
        if (tokens.length == 0) {
            throw new IllegalArgumentException("Empty command line arguments");
        }
        String commandName = tokens[0];
        String[] args =  Arrays.copyOfRange(tokens, 1, tokens.length);
        return new ParsedCommand(commandName, args);
    }

    private String[] tokenize(String line) throws IllegalArgumentException {
        List<String> tokens = new ArrayList<>();
        StringBuilder token = new StringBuilder();
        boolean inQuotes = false;
        for (char c : line.toCharArray()) {
            if (Character.isWhitespace(c) && !inQuotes) {
                if (!token.isEmpty()) {
                    tokens.add(token.toString());
                    token = new StringBuilder();
                }
                continue;
            }
            if (c == '"' && !inQuotes) {
                inQuotes = true;
                continue;
            }
            if (c == '"') {
                inQuotes = false;
                continue;
            }
            token.append(c);
        }
        if (!token.isEmpty()) {
            tokens.add(token.toString());
        }
        if (inQuotes) {
            throw new IllegalArgumentException("Invalid token: " + line + ". Quotation may not follow quotes");
        }
        return tokens.toArray(new String[0]);
    }

//    private ParsedCommand handleDone() throws IllegalArgumentException {
//        if (args.length < 2) {
//            throw new IllegalArgumentException("Invalid command line arguments");
//        }
//        HashMap<String, List<String>> map = new HashMap<>();
//        map.put("title", List.of(args[1]));
//        return new ParsedCommand("done", map);
//    }

//    private ParsedCommand handleListTasks() throws IllegalArgumentException {
//        if (args.length < 1) {
//            throw new IllegalArgumentException("Invalid command line arguments");
//        }
//        if (args.length == 1) {
//            return new ParsedCommand(args[0], new HashMap<>());
//        }
//        HashMap<String, List<String>> arguments = new HashMap<>();
//        for (int i = 1; i <  args.length - 1; i++) {
//            String param = args[i];
//            switch (param) {
//                case "--project":
//                    arguments.put("project", List.of(args[++i]));
//                    break;
//                case "--assignee":
//                    arguments.put("assignee", List.of(args[++i]));
//                    break;
//                case "--priority":
//                    arguments.put("priority", List.of(args[++i]));
//                    break;
//                case "--tag":
//                    if (!arguments.containsKey("tags")) {
//                        arguments.put("tags", List.of(args[++i]));
//                    } else {
//                        arguments.get("tags").add(args[++i]);
//                    }
//                    break;
//            }
//        }
//        return new ParsedCommand(args[0], arguments);
//    }
}