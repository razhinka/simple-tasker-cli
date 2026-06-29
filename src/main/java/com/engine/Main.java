package com.engine;

import com.engine.model.*;

public class Main {
    public static void main(String[] args) {
        User user = new User(1, "Zahar");
        Project project = new Project(1, "project 1", user, "description");
        Tag tag1 = new Tag("tag 1");
        Tag tag2 = new Tag("tag 2");
        Comment comment = new Comment(1, user, "comment");
    }
}