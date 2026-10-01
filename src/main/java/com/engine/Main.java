package com.engine;

import com.engine.commands.*;
import com.engine.model.Project;
import com.engine.model.User;
import com.engine.services.*;

import javax.sql.DataSource;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        // connecting database through HikariCP
        DataSource dataSource = DatabasePool.getDataSource();
        // Run migration through Flyway
        DatabaseMigration.runMigrations(dataSource);
        //Creating repositories
        TaskRepository taskRepository = new SQLTaskRepository(dataSource);
        Repository<User> userRepository = new SQLUserRepository(dataSource);
        Repository<Project> projectRepository = new SQLProjectRepository(dataSource);

        CommandParser parser = new CommandParser();
        CommandRegistry registry = new CommandRegistry();
        CommandHandler handler = new CommandHandler(registry);

        Path helpFilePath = Paths.get("resources/help.txt");
        //adding commands to registry
        registry.register(new AddTaskCommand(taskRepository));
        registry.register(new ListTasksCommand(taskRepository));
        registry.register(new ExitCommand());
        registry.register(new DoneCommand(taskRepository));
        registry.register(new HelpCommand(helpFilePath));
        registry.register(new StatsCommand(taskRepository));
        registry.register(new EditTaskCommand(taskRepository));
        registry.register(new DeleteTaskCommand(taskRepository));

        try (Scanner input = new Scanner(System.in)) {
            boolean running = true;
            while (running && input.hasNextLine()) {
                try {
                    String inputLine = input.nextLine();
                    ParsedCommand command = parser.parse(inputLine);
                    if (command.name().equals("exit")) {
                        running = false;
                    }
                    handler.handle(command);
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage() + "\n");
                }
            }
        }
    }
}
