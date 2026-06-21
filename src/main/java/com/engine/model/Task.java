package com.engine.model;

import java.util.HashSet;
import java.util.LinkedList;

public class Task {
    long id;
    String title;
    String description;
    Project project;
    User assignee;
    String status;      // TODO: Enum
    String priority;    // TODO: Enum
    HashSet<Tag> tags;
    LinkedList<Comment> comments;

    public Task(long id, String title, String description, Project project, User assignee, String status, String priority) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.project = project;
        this.assignee = assignee;
        this.status = status;
        this.priority = priority;
        this.tags = new HashSet<>();
        this.comments = new LinkedList<>();
    }

    public long getId() {
        return id;
    }

    public HashSet<Tag> getTags() {
        return tags;
    }

    public Project getProject() {
        return project;
    }

    public String getDescription() {
        return description;
    }

    public LinkedList<Comment> getComments() {
        return comments;
    }

    public String getPriority() {
        return priority;
    }

    public String getStatus() {
        return status;
    }

    public String getTitle() {
        return title;
    }

    public User getAssignee() {
        return assignee;
    }

    public void setComments(LinkedList<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "Task{id=" + id + ", title=" + title + ", description=" + description + ", project=" + project;
    }

    public void markDone() {
        status = "done";
    }

    public boolean isDone() {
        return status.equalsIgnoreCase("done");
    }

    public boolean hasTag(Tag tag) {
        return tags.contains(tag);
    }

    public String getSummary() {
        return title + " - " + description;
    }
}
