package com.engine;

import com.engine.cli.CommandLineTokenizer;
import com.engine.cli.CommandParser;
import com.engine.cli.ParsedCommand;
import com.engine.model.Project;
import com.engine.model.Task;
import com.engine.model.User;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
         try (Scanner input = new Scanner(System.in)) {
             while (input.hasNextLine()) {
                 try {
                     String jopa = input.nextLine();
                     String[] tokens = CommandLineTokenizer.tokenize(jopa);
                     CommandParser parser = new CommandParser(tokens);
                     ParsedCommand command = parser.parse();
                     if (command.command().equals("exit")) {
                         System.exit(0);
                     }
                     System.out.println(command);
                 } catch (Exception e) {
                     System.out.println("Error: " + e.getMessage());
                 }
             }
         }
    }
}
