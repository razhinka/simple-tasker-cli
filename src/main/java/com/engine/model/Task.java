package com.engine.model;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Objects;

public class Task {
    private final long id;
    private final String title;
    private final String description;
    private final Project project;
    private final User assignee;
    private Status status;
    private final Priority priority;
    private final HashSet<Tag> tags;
    private final LinkedList<Comment> comments;

    private Task(Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.description = builder.description;
        this.project = builder.project;
        this.assignee = builder.assignee;
        this.status = builder.status;
        this.priority = builder.priority;
        this.tags = new HashSet<>(builder.tags);
        this.comments = new LinkedList<>(builder.comments);
    }

    public static class Builder {
        // required parameters
        private final long id;
        private final String title;
        private final Project project;
        private User assignee;
        // optional parameters
        private String description = "";
        private Status status = Status.NOT_STARTED;
        private Priority priority = Priority.LOW;
        private HashSet<Tag> tags = new HashSet<>();
        private LinkedList<Comment> comments = new LinkedList<>();

        public Builder(long id, String title, Project project, User assignee) {
            this.id = id;
            this.title = Objects.requireNonNull(title, "title must not be null");
            this.project = Objects.requireNonNull(project, "project must not be null");
            this.assignee = Objects.requireNonNull(assignee, "assignee must not be null");
        }

        // Constructor for copying object
        public Builder(Task task) {
            this.id = task.id;
            this.title = task.title;
            this.description = task.description;
            this.project = task.project;
            this.assignee = task.assignee;
            this.status = task.status;
            this.priority = task.priority;
            this.tags = task.tags;
            this.comments = task.comments;
        }

        public Builder description(String description) {
            this.description = Objects.requireNonNull(description, "description must not be null");
            return this;
        }

        public Builder assignee(User assignee) {
            this.assignee = Objects.requireNonNull(assignee, "assignee must not be null");
            return this;
        }

        public Builder status(Status status) {
            this.status = Objects.requireNonNull(status, "status must not be null");
            return this;
        }

        public Builder priority(Priority priority) {
            this.priority = Objects.requireNonNull(priority, "priority must not be null");
            return this;
        }

        public Builder tags(HashSet<Tag> tags) {
            this.tags = Objects.requireNonNull(tags, "tags must not be null");
            return this;
        }

        public Builder comments(LinkedList<Comment> comments) {
            this.comments = comments;
            return this;
        }

        public Task build() {
            return new Task(this);
        }
    }

    public long getId() {
        return id;
    }

    public HashSet<Tag> getTags() {
        return new HashSet<>(tags);
    }

    public Project getProject() {
        return project;
    }

    public String getDescription() {
        return description;
    }

    public LinkedList<Comment> getComments() {
        return new LinkedList<>(comments);
    }

    public Priority getPriority() {
        return priority;
    }

    public Status getStatus() {
        return status;
    }

    public String getTitle() {
        return title;
    }

    public User getAssignee() {
        return assignee;
    }

    @Override
    public String toString() {
        return "Task{id=" + id + ", title=" + title + ", project=" + project + ", assignee=" + assignee + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Task)) {
            return false;
        }
        if (o == this) {
            return true;
        }
        Task other = (Task) o;
        return this.id == other.id && this.project.equals(other.project) && this.assignee.equals(other.assignee) && this.title.equals(other.title);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + Long.hashCode(id);
        result = 31 * result + project.hashCode();
        result = 31 * result + assignee.hashCode();
        result = 31 * result + title.hashCode();
        return result;
    }

    public void markDone() {
        status = Status.DONE;
    }

    public boolean isDone() {
        return status.equals(Status.DONE);
    }

    public boolean hasTag(Tag tag) {
        return tags.contains(tag);
    }

    public String getSummary() {
        return title + " - " + description;
    }

    enum Status {
        DONE,
        IN_PROGRESS,
        NOT_STARTED,
    }

    enum Priority {
        LOW,
        MEDIUM,
        HIGH
    }
}
