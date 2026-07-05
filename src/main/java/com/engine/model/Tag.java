package com.engine.model;

import java.util.Objects;

public record Tag(String name) {

    @Override
    public String name() {
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

}
