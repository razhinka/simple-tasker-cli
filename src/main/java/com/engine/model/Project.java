package com.engine.model;

import java.util.ArrayList;

public class Project {
    long id;
    String title;
    String description;
    User user;

    public Project(long id, String title, String description, User user) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "Project{" + "id=" + id + ", title=" + title + ", description=" + description + ", user=" + user + '}';
    }

    public boolean belongsToUser(User user) {
        return this.user.equals(user);
    }
}
