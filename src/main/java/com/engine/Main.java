package com.engine;

import com.engine.model.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        User user = new User(1, "Zahar");
        Project project = new Project(1, "project 1", "cool and rich project", user);
        Tag tag1 = new Tag("tag 1");
        Tag tag2 = new Tag("tag 2");
        Comment comment = new Comment(1, user, "comment");
        Task task = new Task(1, "title","do money", project, user, "in_process", "megahigh"  );
        System.out.println(task);
        System.out.println(task.hasTag(new Tag("java")));
        task.markDone();
        System.out.println(task.isDone());
    }
}