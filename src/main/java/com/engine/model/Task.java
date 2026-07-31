package com.engine.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalDateTime;
import java.util.*;

@JsonDeserialize(builder = Task.Builder.class)
public class Task {
    private final long id;
    private final String title;
    private final String description;
    private final Project project;
    private final User assignee;
    private final Status status;
    private final Priority priority;
    private final Set<Tag> tags;
    private final List<Comment> comments;
    private final LocalDateTime deadline;

    private int cachedHashCode;

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
        this.deadline = builder.deadline;
    }

    public static class Builder {
        // required parameters
        private long id;
        private String title;
        private Project project;
        private User assignee;
        // optional parameters
        private String description = "";
        private Status status = Status.NOT_STARTED;
        private Priority priority = Priority.LOW;
        private Set<Tag> tags = new HashSet<>();
        private List<Comment> comments = new LinkedList<>();
        private LocalDateTime deadline;

        @JsonCreator
        public Builder(@JsonProperty("id") long id,
                       @JsonProperty("title") String title,
                       @JsonProperty("project") Project project,
                       @JsonProperty("assignee") User assignee) {
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
            this.tags = new HashSet<>(task.tags);
            this.comments = new LinkedList<>(task.comments);
            this.deadline = task.deadline;
        }

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder title(String title) {
            this.title = Objects.requireNonNull(title, "title must not be null");
            return this;
        }

        public Builder project(Project project) {
            this.project = Objects.requireNonNull(project, "project must not be null");
            return this;
        }

        public Builder assignee(User assignee) {
            this.assignee = Objects.requireNonNull(assignee, "assignee must not be null");
            return this;
        }

        @JsonProperty("description")
        public Builder description(String description) {
            this.description = Objects.requireNonNull(description, "description must not be null");
            return this;
        }

        @JsonProperty("status")
        public Builder status(Status status) {
            this.status = Objects.requireNonNull(status, "status must not be null");
            return this;
        }

        @JsonProperty("priority")
        public Builder priority(Priority priority) {
            this.priority = Objects.requireNonNull(priority, "priority must not be null");
            return this;
        }

        @JsonProperty("tags")
        public Builder tags(Set<Tag> tags) {
            this.tags = Set.copyOf(tags);
            return this;
        }

        @JsonProperty("comments")
        public Builder comments(List<Comment> comments) {
            this.comments = List.copyOf(comments);
            return this;
        }

        @JsonProperty("deadline")
        public Builder deadline(LocalDateTime deadline) {
            this.deadline = deadline;
            return this;
        }

        public Task build() {
            return new Task(this);
        }
    }

    public long getId() {
        return id;
    }

    public Set<Tag> getTags() {
        return new HashSet<>(tags);
    }

    public Project getProject() {
        return project;
    }

    public String getDescription() {
        return description;
    }

    public List<Comment> getComments() {
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

    public Optional<LocalDateTime> getDeadline() {
        return Optional.ofNullable(deadline);
    }

    @Override
    public String toString() {
        return "Task " + id + " \"" + title + "\"\nAssignee: " + assignee.name() + "\nProject: " + project.title() + "\nDescription: " + description
                + "\nStatus: " + status + "\nPriority: " + priority + "\nTags: " + tags + "\nDeadline: " + deadline + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Task other)) {
            return false;
        }
        if (o == this) {
            return true;
        }
        return this.id == other.id && this.project.equals(other.project) && this.assignee.equals(other.assignee) && this.title.equals(other.title);
    }

    @Override
    public int hashCode() {
        if (cachedHashCode == 0) {
            int result = 17;
            result = 31 * result + Long.hashCode(id);
            result = 31 * result + project.hashCode();
            result = 31 * result + assignee.hashCode();
            result = 31 * result + title.hashCode();
            cachedHashCode = result;
            return result;
        } else {
            return cachedHashCode;
        }
    }

    public Task markDone() {
        return new Builder(this).status(Status.DONE).build();
    }
    @JsonIgnore
    public boolean isDone() {
        return status.equals(Status.DONE);
    }
    @JsonIgnore
    public boolean hasTag(Tag tag) {
        return tags.contains(tag);
    }
    @JsonIgnore
    public String getSummary() {
        return title + " - " + description;
    }
}