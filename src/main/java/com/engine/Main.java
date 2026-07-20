package com.engine;

import com.engine.commands.*;
import com.engine.services.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.registerModule(new Jdk8Module()); // добавляем поддержку Optional
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        CommandParser parser = new CommandParser();

        File repositoryFile = new File("src/main/resources/repository.json");
        TaskRepository repository = new FileTaskRepository(repositoryFile, mapper);
        CommandRegistry registry = new CommandRegistry();
        CommandHandler handler = new CommandHandler(registry);
        //adding commands to registry
        registry.register(new AddTaskCommand(repository));
        registry.register(new ListTasksCommand(repository));
        registry.register(new ExitCommand());
        registry.register(new DoneCommand(repository));
        registry.register(new HelpCommand());

        try (Scanner input = new Scanner(System.in)) {
            while (input.hasNextLine()) {
                try {
                    String inputLine = input.nextLine();
                    ParsedCommand command = parser.parse(inputLine);
                    handler.handle(command);
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage() + "\n");
                }
            }
        }
    }
}
