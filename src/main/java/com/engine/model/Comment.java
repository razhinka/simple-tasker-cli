package com.engine.model;

import java.time.LocalDateTime;
import java.util.Objects;

public record Comment(long id, User author, String text, LocalDateTime created) {

    public Comment(long id, User author, String text,  LocalDateTime created) {
        this.id = id;
        this.author = Objects.requireNonNull(author, "author must not be null");
        this.text = Objects.requireNonNull(text, "text must not be null");
        this.created = Objects.requireNonNull(created, "created must not be null");
    }

    @Override
    public String toString() {
        return "Comment{" + "id=" + id + ", author=" + author + ", text=" + text + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Comment(long id1, User author1, String text1, LocalDateTime created1))) {
            return false;
        }
        if (this == o) {
            return true;
        }
        return this.id == id1 && author.equals(author1) && text.equals(text1) && created.equals(created1);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + Long.hashCode(id);
        result = 31 * result + author.hashCode();
        result = 31 * result + text.hashCode();
        return result;
    }

    public boolean isWrittenBy(User user) {
        return user.equals(author);
    }
}
