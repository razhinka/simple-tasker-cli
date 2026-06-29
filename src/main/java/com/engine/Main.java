package com.engine;

import com.engine.model.*;

import java.util.HashSet;
import java.util.LinkedList;

public class Main {
    public static void main(String[] args) {
        User user = new User(1, "Zahar");
        Project project = new Project(1, "project 1", user, "description");
        Tag tag1 = new Tag("tag 1");
        Tag tag2 = new Tag("tag 2");
        Comment comment = new Comment(1, user, "comment");
        LinkedList<Comment> comments = new LinkedList<>();
        comments.add(comment);
        Task task = new Task.Builder(1, "Task 1", project, user)
                .comments(comments)
                .tags(new HashSet<>(){{add(tag1);add(tag2);}})
                .build();
        System.out.println(task);
        System.out.println(task.hasTag(new Tag("java")));
        task.markDone();
        System.out.println(task.isDone());
    }
}