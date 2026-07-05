package com.engine;

import com.engine.commands.*;
import com.engine.services.CommandHandler;
import com.engine.services.CommandParser;
import com.engine.services.CommandRegistry;
import com.engine.services.InMemoryTaskRepository;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
         try (Scanner input = new Scanner(System.in)) {
             while (input.hasNextLine()) {
                 try {
                     String inputLine = input.nextLine();
                     CommandParser parser = new CommandParser();
                     ParsedCommand command = parser.parse(inputLine);
                     InMemoryTaskRepository repository = InMemoryTaskRepository.getInstance();
                     CommandRegistry registry = new CommandRegistry();
                     //adding commands to registry
                     registry.register(new AddTaskCommand(repository));
                     registry.register(new ListTasksCommand(repository));
                     registry.register(new ExitCommand());
                     registry.register(new DoneCommand(repository));
                     registry.register(new HelpCommand());

                     CommandHandler handler = new CommandHandler(registry);
                     handler.handle(command);
                 } catch (Exception e) {
                     System.out.println("Error: " + e.getMessage());
                 }
             }
         }
    }
}
