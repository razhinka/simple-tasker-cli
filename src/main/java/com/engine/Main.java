package com.engine;

import com.engine.commands.AddTaskCommand;
import com.engine.commands.ListTasksCommand;
import com.engine.services.CommandHandler;
import com.engine.services.CommandParser;
import com.engine.commands.ParsedCommand;
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
                     if (command.name().equals("exit")) {
                         System.exit(0);
                     }
                     InMemoryTaskRepository repository = InMemoryTaskRepository.getInstance();
                     CommandRegistry registry = new CommandRegistry();
                     registry.register(new AddTaskCommand(repository));
                     registry.register(new ListTasksCommand(repository));
                     CommandHandler handler = new CommandHandler(registry);
                     handler.handle(command);
                 } catch (Exception e) {
                     System.out.println("Error: " + e.getMessage());
                 }
             }
         }
    }
}
