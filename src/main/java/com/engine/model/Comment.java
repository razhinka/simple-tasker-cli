package com.engine.model;

import java.util.Objects;

public record Comment(long id, User author, String text) {
    public Comment(long id, User author, String text) {
        this.id = id;
        this.author = Objects.requireNonNull(author, "author must not be null");
        this.text = Objects.requireNonNull(text, "text must not be null");
    }

    @Override
    public String toString() {
        return "Comment{" + "id=" + id + ", author=" + author + ", text=" + text + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Comment)) {
            return false;
        }
        if (this == o) {
            return true;
        }
        Comment comment = (Comment) o;
        return this.id == comment.id && author.equals(comment.author) && text.equals(comment.text);
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
