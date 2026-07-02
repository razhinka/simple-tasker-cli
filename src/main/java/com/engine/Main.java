package com.engine;

import com.engine.model.ParsedCommand;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
         try (Scanner input = new Scanner(System.in)) {
             String jopa = input.nextLine();
             String[] tokens = CommandLineTokenizer.tokenize(jopa);
             for (String token : tokens) {
                 System.out.println(token);
             }
             CommandParser parser = new CommandParser(tokens);
             ParsedCommand command = parser.parse();
             System.out.println(command.toString());
         }
     }
}
