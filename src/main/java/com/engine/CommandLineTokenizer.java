package com.engine;

import java.util.ArrayList;

public class CommandLineTokenizer {
    public static String[] tokenize(String line) {
        ArrayList<String> tokens = new ArrayList<String>();
        StringBuilder token = new StringBuilder();
        boolean inQuotes = false;
        for (char c : line.toCharArray()) {
            if (Character.isWhitespace(c) && !inQuotes) {
                tokens.add(token.toString());
                token = new StringBuilder();
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
        return tokens.toArray(new String[0]);
    }
}
