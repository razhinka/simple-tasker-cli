package com.engine.model;

public class Comment {
    long id;
    User author;
    String text;

    public Comment(long id, User author, String text) {
        this.id = id;
        this.author = author;
        this.text = text;
    }

    public long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public User getAuthor() {
        return author;
    }

    @Override
    public String toString() {
        return "Comment{" + "id=" + id + ", author=" + author + ", text=" + text + '}';
    }

    public boolean isWrittenBy(User user) {
        return user.equals(author);
    }
}
