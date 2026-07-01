package com.engine;

import java.util.HashSet;

public class CommandParser {
    private final String[] args;

    public CommandParser(String[] args) {
        this.args = args;
    }

    public void parse() {
        if (args.length == 0) {
            System.out.println("Empty string");
            return;
        }
        String command = args[0];
        switch (command) {
            case "add-task":
                handleAddTask();
                break;
            case "list-tasks":
                handleListTasks();
                break;
            case "done":
                handleDone();
                break;
            case "help":
                //Write help file
            case "exit":
                // Will call the command to terminate the job.
            default:
                System.out.printf("Unknown command %s\n", command);            
        }
    }

    private void handleDone() {
    }

    private void handleAddTask() {
        if (args.length < 6) {
            System.out.println("Not enough arguments");
            return;
        }
        String title = args[1];
        String project = null, assignee = null; //required parameters
        String priority;
        HashSet<String> tags = new HashSet<>();
        for (int i = 2; i < args.length - 1; i++) {
            String param = args[i];
            switch (param) {
                case "--project":
                    project = args[++i];
                    break;
                case "--assignee":
                    assignee = args[++i];
                    break;
                case "--tag":
                    tags.add(args[++i]);
                    break;
                case "--priority":
                    priority = args[++i];
                    break;
            }
        }
        if (project == null) {
            System.out.println("Invalid command line arguments: required project");
            return;
        }
        if (assignee == null) {
            System.out.println("Invalid command line arguments: required assignee");
            return;
        }
        // TODO: Handle further
        System.out.printf("New task:\nTitle: %s\nProject: %s\nAssignee: %s\n", title, project, assignee);
    }

    private void handleListTasks() {
        if (args.length < 1) {
            System.out.println("Empty string");
            return;
        }
        if (args.length == 1) {
            System.out.println("smth like task list");
            return;
        }
        // TODO: Add sort by parametrs after making a database

    }
}
