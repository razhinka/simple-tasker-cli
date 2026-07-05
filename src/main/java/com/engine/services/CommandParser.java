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
}