package com.engine.cli;

import java.util.ArrayList;
import java.util.List;

public class CommandLineTokenizer {
    public static String[] tokenize(String line) throws IllegalArgumentException {
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
