package com.engine.model;

import java.util.Objects;

public class Project {
    private final long id;
    private final String title;
    private String description = ""; // optional parameter
    private final User user;

    public Project(long id, String title, User user) {
        this.id = id;
        this.title = Objects.requireNonNull(title, "title must not be null");
        this.user = Objects.requireNonNull(user, "user must not be null");
    }

    public Project(long id, String title, User user, String description) {
        this.id = id;
        this.title = Objects.requireNonNull(title, "title must not be null");
        this.user = Objects.requireNonNull(user, "user must not be null");
        this.description = Objects.requireNonNull(description, "description must not be null");
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

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Project)) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        Project project = (Project) obj;
        return id == project.id && title.equals(project.title) && user.equals(project.user) && description.equals(project.description);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + Long.hashCode(id);
        result = 31 * result + title.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + user.hashCode();
        return result;
    }

    public boolean belongsToUser(User user) {
        return this.user.equals(user);
    }
}
