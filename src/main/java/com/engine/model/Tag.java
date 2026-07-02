package com.engine.model;

import java.util.Objects;

public class Tag {
    private final String name;

    public Tag(String name) {
        this.name = name;
    }

    public String getName() {
        return Objects.requireNonNull(name);
    }

    @Override
    public String toString() {
        return "Tag{tag=" + name + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Tag)) return false;
        Tag tag = (Tag) o;
        return Objects.equals(name, tag.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
