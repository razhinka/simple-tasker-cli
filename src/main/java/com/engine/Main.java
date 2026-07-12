package com.engine;

import com.engine.commands.*;
import com.engine.model.Task;
import com.engine.services.*;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        CommandParser parser = new CommandParser();
        Repository<Task> repository = new InMemoryTaskRepository();
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
