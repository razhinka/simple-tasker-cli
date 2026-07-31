package com.engine.model;

import java.util.Objects;

public record Project(long id, String title) implements Comparable<Project> {

    public Project(long id, String title) {
        this.id = id;
        this.title = Objects.requireNonNull(title, "title must not be null");
    }

    @Override
    public String toString() {
        return "Project(" + title + ")";
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
        return id == project.id && title.equals(project.title);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + Long.hashCode(id);
        result = 31 * result + title.hashCode();
        return result;
    }

    @Override
    public int compareTo(Project o) {
        return o.toString().compareTo(this.toString());
    }
}
