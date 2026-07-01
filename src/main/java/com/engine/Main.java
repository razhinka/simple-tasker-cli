package com.engine;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
         try (Scanner input = new Scanner(System.in)) {
             String jopa = input.nextLine();
             String[] tokens = CommandLineTokenizer.tokenize(jopa);
             CommandParser parser = new CommandParser(tokens);
             parser.parse();
         }
     }
}
