package com.engine.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class HelpCommand implements Command {
    private final Path helpFilePath;

    public HelpCommand(Path helpFilePath) {
        this.helpFilePath = helpFilePath;
    }

    @Override
    public String commandName() {
        return "help";
    }

    @Override
    public void execute(String[] args) {
        try (Stream<String> stream = Files.lines(helpFilePath)) {
            stream.forEach(System.out::println);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
