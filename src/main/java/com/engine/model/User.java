package com.engine.model;

import java.util.ArrayList;
import java.util.HashSet;

public class User {
    long id;
    String name;

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        return id == ((User)obj).getId();
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", name=" + name + '}';
    }
}
